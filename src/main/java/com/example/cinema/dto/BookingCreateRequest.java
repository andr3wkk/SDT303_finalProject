package com.example.cinema.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class BookingCreateRequest {

    @NotNull(message = "Customer id is required")
    private UUID customerId;

    @NotNull(message = "Screening id is required")
    private UUID screeningId;

    @Min(value = 1, message = "Seats must be at least 1")
    private int seats;

    public BookingCreateRequest() {
    }

    public BookingCreateRequest(UUID customerId, UUID screeningId, int seats) {
        this.customerId = customerId;
        this.screeningId = screeningId;
        this.seats = seats;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getScreeningId() {
        return screeningId;
    }

    public int getSeats() {
        return seats;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public void setScreeningId(UUID screeningId) {
        this.screeningId = screeningId;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}