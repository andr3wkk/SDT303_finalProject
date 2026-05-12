package com.example.cinema.service;

import com.example.cinema.dto.MovieCreateRequest;
import com.example.cinema.dto.MovieUpdateRequest;
import com.example.cinema.exception.NotFoundException;
import com.example.cinema.model.Movie;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MovieService {

    private final ConcurrentHashMap<UUID, Movie> movies = new ConcurrentHashMap<>();

    public Movie create(MovieCreateRequest request) {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Movie movie = new Movie(
                id,
                request.getTitle(),
                request.getGenre(),
                request.getDurationMinutes(),
                request.getAgeRating(),
                true,
                now,
                now
        );

        movies.put(id, movie);
        return movie;
    }

    public List<Movie> findAll(String genre, Boolean active, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);

        return movies.values()
                .stream()
                .filter(movie -> genre == null || movie.getGenre().equalsIgnoreCase(genre))
                .filter(movie -> active == null || movie.isActive() == active)
                .skip((long) safePage * safeSize)
                .limit(safeSize)
                .toList();
    }

    public Movie findById(UUID id) {
        Movie movie = movies.get(id);

        if (movie == null) {
            throw new NotFoundException("Movie not found");
        }

        return movie;
    }

    public Movie update(UUID id, MovieUpdateRequest request) {
        Movie movie = findById(id);

        movie.setTitle(request.getTitle());
        movie.setGenre(request.getGenre());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setAgeRating(request.getAgeRating());
        movie.setActive(request.isActive());
        movie.setUpdatedAt(LocalDateTime.now());

        return movie;
    }

    public void delete(UUID id) {
        Movie removed = movies.remove(id);

        if (removed == null) {
            throw new NotFoundException("Movie not found");
        }
    }

    public void clearAll() {
        movies.clear();
    }
}