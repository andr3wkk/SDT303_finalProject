package com.example.cinema.service;

import com.example.cinema.dto.BookingCreateRequest;
import com.example.cinema.exception.BookingConflictException;
import com.example.cinema.exception.NotFoundException;
import com.example.cinema.model.Booking;
import com.example.cinema.model.BookingStatus;
import com.example.cinema.model.Screening;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BookingService {

    private final ConcurrentHashMap<UUID, Booking> bookings = new ConcurrentHashMap<>();

    private final CustomerService customerService;
    private final ScreeningService screeningService;

    public BookingService(CustomerService customerService, ScreeningService screeningService) {
        this.customerService = customerService;
        this.screeningService = screeningService;
    }

    public Booking create(BookingCreateRequest request) {
        customerService.findById(request.getCustomerId());

        Screening screening = screeningService.findById(request.getScreeningId());

        if (screening.getAvailableSeats() < request.getSeats()) {
            throw new BookingConflictException("Not enough seats available");
        }

        screeningService.reduceAvailableSeats(request.getScreeningId(), request.getSeats());

        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Booking booking = new Booking(
                id,
                request.getCustomerId(),
                request.getScreeningId(),
                request.getSeats(),
                BookingStatus.CONFIRMED,
                now,
                now
        );

        bookings.put(id, booking);
        return booking;
    }

    public List<Booking> findAll(UUID customerId, UUID screeningId, BookingStatus status, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);

        return bookings.values()
                .stream()
                .filter(booking -> customerId == null || booking.getCustomerId().equals(customerId))
                .filter(booking -> screeningId == null || booking.getScreeningId().equals(screeningId))
                .filter(booking -> status == null || booking.getStatus() == status)
                .skip((long) safePage * safeSize)
                .limit(safeSize)
                .toList();
    }

    public Booking findById(UUID id) {
        Booking booking = bookings.get(id);

        if (booking == null) {
            throw new NotFoundException("Booking not found");
        }

        return booking;
    }

    public void cancel(UUID id) {
        Booking booking = findById(id);

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            booking.setStatus(BookingStatus.CANCELLED);
            booking.setUpdatedAt(LocalDateTime.now());

            screeningService.restoreAvailableSeats(booking.getScreeningId(), booking.getSeats());
        }
    }

    public void clearAll() {
        bookings.clear();
    }
}