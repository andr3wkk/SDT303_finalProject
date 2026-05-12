package com.example.cinema.service;

import com.example.cinema.dto.ScreeningCreateRequest;
import com.example.cinema.dto.ScreeningUpdateRequest;
import com.example.cinema.exception.BadRequestException;
import com.example.cinema.exception.NotFoundException;
import com.example.cinema.model.Screening;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ScreeningService {

    private final ConcurrentHashMap<UUID, Screening> screenings = new ConcurrentHashMap<>();
    private final MovieService movieService;

    public ScreeningService(MovieService movieService) {
        this.movieService = movieService;
    }

    public Screening create(ScreeningCreateRequest request) {
        movieService.findById(request.getMovieId());

        if (request.getScreeningTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Screening time must be in the future");
        }

        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Screening screening = new Screening(
                id,
                request.getMovieId(),
                request.getScreeningTime(),
                request.getHallName(),
                request.getTotalSeats(),
                request.getTotalSeats(),
                now,
                now
        );

        screenings.put(id, screening);
        return screening;
    }

    public List<Screening> findAll(UUID movieId, String hallName, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);

        return screenings.values()
                .stream()
                .filter(screening -> movieId == null || screening.getMovieId().equals(movieId))
                .filter(screening -> hallName == null || screening.getHallName().equalsIgnoreCase(hallName))
                .skip((long) safePage * safeSize)
                .limit(safeSize)
                .toList();
    }

    public Screening findById(UUID id) {
        Screening screening = screenings.get(id);

        if (screening == null) {
            throw new NotFoundException("Screening not found");
        }

        return screening;
    }

    public Screening update(UUID id, ScreeningUpdateRequest request) {
        Screening screening = findById(id);

        if (request.getScreeningTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Screening time must be in the future");
        }

        int bookedSeats = screening.getTotalSeats() - screening.getAvailableSeats();

        if (request.getTotalSeats() < bookedSeats) {
            throw new BadRequestException("Total seats cannot be smaller than already booked seats");
        }

        screening.setScreeningTime(request.getScreeningTime());
        screening.setHallName(request.getHallName());
        screening.setTotalSeats(request.getTotalSeats());
        screening.setAvailableSeats(request.getTotalSeats() - bookedSeats);
        screening.setUpdatedAt(LocalDateTime.now());

        return screening;
    }

    public void reduceAvailableSeats(UUID screeningId, int seats) {
        Screening screening = findById(screeningId);

        if (screening.getAvailableSeats() < seats) {
            throw new BadRequestException("Not enough seats available");
        }

        screening.setAvailableSeats(screening.getAvailableSeats() - seats);
        screening.setUpdatedAt(LocalDateTime.now());
    }

    public void restoreAvailableSeats(UUID screeningId, int seats) {
        Screening screening = findById(screeningId);

        screening.setAvailableSeats(screening.getAvailableSeats() + seats);
        screening.setUpdatedAt(LocalDateTime.now());
    }

    public void delete(UUID id) {
        Screening removed = screenings.remove(id);

        if (removed == null) {
            throw new NotFoundException("Screening not found");
        }
    }

    public void clearAll() {
        screenings.clear();
    }
}