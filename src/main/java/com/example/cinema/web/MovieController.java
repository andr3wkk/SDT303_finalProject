package com.example.cinema.web;

import com.example.cinema.dto.MovieCreateRequest;
import com.example.cinema.dto.MovieUpdateRequest;
import com.example.cinema.model.Movie;
import com.example.cinema.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Movie createMovie(@Valid @RequestBody MovieCreateRequest request) {
        return movieService.create(request);
    }

    @GetMapping
    public List<Movie> getMovies(
            @RequestParam(name = "genre", required = false) String genre,
            @RequestParam(name = "active", required = false) Boolean active,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return movieService.findAll(genre, active, page, size);
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable("id") UUID id) {
        return movieService.findById(id);
    }

    @PutMapping("/{id}")
    public Movie updateMovie(
            @PathVariable("id") UUID id,
            @Valid @RequestBody MovieUpdateRequest request
    ) {
        return movieService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable("id") UUID id) {
        movieService.delete(id);
    }
}