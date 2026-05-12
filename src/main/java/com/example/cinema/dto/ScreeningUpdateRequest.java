package com.example.cinema.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ScreeningUpdateRequest {

    @NotNull(message = "Screening time is required")
    @Future(message = "Screening time must be in the future")
    private LocalDateTime screeningTime;

    @NotBlank(message = "Hall name is required")
    private String hallName;

    @Min(value = 1, message = "Total seats must be at least 1")
    private int totalSeats;

    public ScreeningUpdateRequest() {
    }

    public ScreeningUpdateRequest(LocalDateTime screeningTime, String hallName, int totalSeats) {
        this.screeningTime = screeningTime;
        this.hallName = hallName;
        this.totalSeats = totalSeats;
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

    public void setScreeningTime(LocalDateTime screeningTime) {
        this.screeningTime = screeningTime;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}