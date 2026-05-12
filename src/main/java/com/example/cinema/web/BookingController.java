package com.example.cinema.web;

import com.example.cinema.dto.BookingCreateRequest;
import com.example.cinema.model.Booking;
import com.example.cinema.model.BookingStatus;
import com.example.cinema.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking createBooking(@Valid @RequestBody BookingCreateRequest request) {
        return bookingService.create(request);
    }

    @GetMapping
    public List<Booking> getBookings(
            @RequestParam(name = "customerId", required = false) UUID customerId,
            @RequestParam(name = "screeningId", required = false) UUID screeningId,
            @RequestParam(name = "status", required = false) BookingStatus status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return bookingService.findAll(customerId, screeningId, status, page, size);
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable("id") UUID id) {
        return bookingService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(@PathVariable("id") UUID id) {
        bookingService.cancel(id);
    }
}