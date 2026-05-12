package com.example.cinema.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Screening {

    private UUID id;
    private UUID movieId;
    private LocalDateTime screeningTime;
    private String hallName;
    private int totalSeats;
    private int availableSeats;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Screening() {
    }

    public Screening(UUID id, UUID movieId, LocalDateTime screeningTime, String hallName,
                     int totalSeats, int availableSeats, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.movieId = movieId;
        this.screeningTime = screeningTime;
        this.hallName = hallName;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getMovieId() {
        return movieId;
    }

    public LocalDateTime getScreeningTime() {
        return screeningTime;
    }

    public String getHallName() {
        return hallName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setMovieId(UUID movieId) {
        this.movieId = movieId;
    }

    public void setScreeningTime(LocalDateTime screeningTime) {
        this.screeningTime = screeningTime;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}