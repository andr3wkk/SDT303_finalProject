package com.example.cinema.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class MovieUpdateRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Genre is required")
    private String genre;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private int durationMinutes;

    @NotBlank(message = "Age rating is required")
    private String ageRating;

    private boolean active;

    public MovieUpdateRequest() {
    }

    public MovieUpdateRequest(String title, String genre, int durationMinutes, String ageRating, boolean active) {
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.ageRating = ageRating;
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public boolean isActive() {
        return active;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}