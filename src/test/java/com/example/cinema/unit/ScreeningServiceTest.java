package com.example.cinema.unit;

import com.example.cinema.dto.MovieCreateRequest;
import com.example.cinema.dto.ScreeningCreateRequest;
import com.example.cinema.exception.BadRequestException;
import com.example.cinema.exception.NotFoundException;
import com.example.cinema.model.Movie;
import com.example.cinema.model.Screening;
import com.example.cinema.service.MovieService;
import com.example.cinema.service.ScreeningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ScreeningServiceTest {

    private MovieService movieService;
    private ScreeningService screeningService;

    @BeforeEach
    void setUp() {
        movieService = new MovieService();
        screeningService = new ScreeningService(movieService);
    }

    @Test
    void createScreeningSuccessfully() {
        Movie movie = movieService.create(new MovieCreateRequest(
                "Interstellar",
                "Sci-Fi",
                169,
                "PG-13"
        ));

        ScreeningCreateRequest request = new ScreeningCreateRequest(
                movie.getId(),
                LocalDateTime.now().plusDays(10),
                "Hall A",
                80
        );

        Screening screening = screeningService.create(request);

        assertNotNull(screening.getId());
        assertEquals(movie.getId(), screening.getMovieId());
        assertEquals(80, screening.getTotalSeats());
        assertEquals(80, screening.getAvailableSeats());
    }

    @Test
    void throwExceptionWhenMovieDoesNotExist() {
        ScreeningCreateRequest request = new ScreeningCreateRequest(
                UUID.randomUUID(),
                LocalDateTime.now().plusDays(10),
                "Hall A",
                80
        );

        assertThrows(NotFoundException.class, () ->
                screeningService.create(request)
        );
    }

    @Test
    void throwExceptionWhenScreeningTimeIsPast() {
        Movie movie = movieService.create(new MovieCreateRequest(
                "Interstellar",
                "Sci-Fi",
                169,
                "PG-13"
        ));

        ScreeningCreateRequest request = new ScreeningCreateRequest(
                movie.getId(),
                LocalDateTime.now().minusDays(1),
                "Hall A",
                80
        );

        assertThrows(BadRequestException.class, () ->
                screeningService.create(request)
        );
    }

    @Test
    void reduceAvailableSeatsSuccessfully() {
        Movie movie = movieService.create(new MovieCreateRequest(
                "Interstellar",
                "Sci-Fi",
                169,
                "PG-13"
        ));

        Screening screening = screeningService.create(new ScreeningCreateRequest(
                movie.getId(),
                LocalDateTime.now().plusDays(10),
                "Hall A",
                80
        ));

        screeningService.reduceAvailableSeats(screening.getId(), 2);

        Screening updated = screeningService.findById(screening.getId());

        assertEquals(78, updated.getAvailableSeats());
    }
}