package com.example.cinema.unit;

import com.example.cinema.dto.*;
import com.example.cinema.exception.BookingConflictException;
import com.example.cinema.exception.NotFoundException;
import com.example.cinema.model.*;
import com.example.cinema.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    private MovieService movieService;
    private CustomerService customerService;
    private ScreeningService screeningService;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        movieService = new MovieService();
        customerService = new CustomerService();
        screeningService = new ScreeningService(movieService);
        bookingService = new BookingService(customerService, screeningService);
    }

    @Test
    void createBookingSuccessfully() {
        Movie movie = createMovie();
        Customer customer = createCustomer();
        Screening screening = createScreening(movie);

        BookingCreateRequest request = new BookingCreateRequest(
                customer.getId(),
                screening.getId(),
                2
        );

        Booking booking = bookingService.create(request);

        assertNotNull(booking.getId());
        assertEquals(customer.getId(), booking.getCustomerId());
        assertEquals(screening.getId(), booking.getScreeningId());
        assertEquals(2, booking.getSeats());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());

        Screening updatedScreening = screeningService.findById(screening.getId());
        assertEquals(78, updatedScreening.getAvailableSeats());
    }

    @Test
    void throwExceptionWhenCustomerDoesNotExist() {
        Movie movie = createMovie();
        Screening screening = createScreening(movie);

        BookingCreateRequest request = new BookingCreateRequest(
                UUID.randomUUID(),
                screening.getId(),
                2
        );

        assertThrows(NotFoundException.class, () ->
                bookingService.create(request)
        );
    }

    @Test
    void throwExceptionWhenScreeningDoesNotExist() {
        Customer customer = createCustomer();

        BookingCreateRequest request = new BookingCreateRequest(
                customer.getId(),
                UUID.randomUUID(),
                2
        );

        assertThrows(NotFoundException.class, () ->
                bookingService.create(request)
        );
    }

    @Test
    void throwExceptionWhenNotEnoughSeats() {
        Movie movie = createMovie();
        Customer customer = createCustomer();
        Screening screening = createScreening(movie);

        BookingCreateRequest request = new BookingCreateRequest(
                customer.getId(),
                screening.getId(),
                1000
        );

        assertThrows(BookingConflictException.class, () ->
                bookingService.create(request)
        );
    }

    @Test
    void cancelBookingSuccessfully() {
        Movie movie = createMovie();
        Customer customer = createCustomer();
        Screening screening = createScreening(movie);

        Booking booking = bookingService.create(new BookingCreateRequest(
                customer.getId(),
                screening.getId(),
                5
        ));

        bookingService.cancel(booking.getId());

        Booking cancelled = bookingService.findById(booking.getId());
        Screening updatedScreening = screeningService.findById(screening.getId());

        assertEquals(BookingStatus.CANCELLED, cancelled.getStatus());
        assertEquals(80, updatedScreening.getAvailableSeats());
    }

    private Movie createMovie() {
        return movieService.create(new MovieCreateRequest(
                "Interstellar",
                "Sci-Fi",
                169,
                "PG-13"
        ));
    }

    private Customer createCustomer() {
        return customerService.create(new CustomerCreateRequest(
                "Andrii",
                "Kovalenko",
                "andrii@example.com",
                "+123456789"
        ));
    }

    private Screening createScreening(Movie movie) {
        return screeningService.create(new ScreeningCreateRequest(
                movie.getId(),
                LocalDateTime.now().plusDays(10),
                "Hall A",
                80
        ));
    }
}