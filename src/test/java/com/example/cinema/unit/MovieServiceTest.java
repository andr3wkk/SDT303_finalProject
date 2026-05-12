package com.example.cinema.unit;

import com.example.cinema.dto.MovieCreateRequest;
import com.example.cinema.dto.MovieUpdateRequest;
import com.example.cinema.exception.NotFoundException;
import com.example.cinema.model.Movie;
import com.example.cinema.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieService();
    }

    @Test
    void createMovieSuccessfully() {
        MovieCreateRequest request = new MovieCreateRequest(
                "Interstellar",
                "Sci-Fi",
                169,
                "PG-13"
        );

        Movie movie = movieService.create(request);

        assertNotNull(movie.getId());
        assertEquals("Interstellar", movie.getTitle());
        assertEquals("Sci-Fi", movie.getGenre());
        assertTrue(movie.isActive());
    }

    @Test
    void findMovieByIdSuccessfully() {
        Movie movie = movieService.create(new MovieCreateRequest(
                "Inception",
                "Sci-Fi",
                148,
                "PG-13"
        ));

        Movie found = movieService.findById(movie.getId());

        assertEquals(movie.getId(), found.getId());
        assertEquals("Inception", found.getTitle());
    }

    @Test
    void throwExceptionWhenMovieNotFound() {
        assertThrows(NotFoundException.class, () ->
                movieService.findById(java.util.UUID.randomUUID())
        );
    }

    @Test
    void filterMoviesByGenre() {
        movieService.create(new MovieCreateRequest("Interstellar", "Sci-Fi", 169, "PG-13"));
        movieService.create(new MovieCreateRequest("Titanic", "Drama", 195, "PG-13"));

        List<Movie> result = movieService.findAll("Sci-Fi", null, 0, 10);

        assertEquals(1, result.size());
        assertEquals("Interstellar", result.getFirst().getTitle());
    }

    @Test
    void updateMovieSuccessfully() {
        Movie movie = movieService.create(new MovieCreateRequest(
                "Old Title",
                "Drama",
                100,
                "PG"
        ));

        MovieUpdateRequest updateRequest = new MovieUpdateRequest(
                "New Title",
                "Action",
                120,
                "PG-13",
                true
        );

        Movie updated = movieService.update(movie.getId(), updateRequest);

        assertEquals("New Title", updated.getTitle());
        assertEquals("Action", updated.getGenre());
        assertEquals(120, updated.getDurationMinutes());
    }

    @Test
    void deleteMovieSuccessfully() {
        Movie movie = movieService.create(new MovieCreateRequest(
                "Movie To Delete",
                "Action",
                110,
                "PG-13"
        ));

        movieService.delete(movie.getId());

        assertThrows(NotFoundException.class, () ->
                movieService.findById(movie.getId())
        );
    }
}