package com.example.cinema.web;

import com.example.cinema.dto.ScreeningCreateRequest;
import com.example.cinema.dto.ScreeningUpdateRequest;
import com.example.cinema.model.Screening;
import com.example.cinema.service.ScreeningService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Screening createScreening(@Valid @RequestBody ScreeningCreateRequest request) {
        return screeningService.create(request);
    }

    @GetMapping
    public List<Screening> getScreenings(
            @RequestParam(name = "movieId", required = false) UUID movieId,
            @RequestParam(name = "hallName", required = false) String hallName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return screeningService.findAll(movieId, hallName, page, size);
    }

    @GetMapping("/{id}")
    public Screening getScreeningById(@PathVariable("id") UUID id) {
        return screeningService.findById(id);
    }

    @PutMapping("/{id}")
    public Screening updateScreening(
            @PathVariable("id") UUID id,
            @Valid @RequestBody ScreeningUpdateRequest request
    ) {
        return screeningService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScreening(@PathVariable("id") UUID id) {
        screeningService.delete(id);
    }
}