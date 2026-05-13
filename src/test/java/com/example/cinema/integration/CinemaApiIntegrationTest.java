package com.example.cinema.integration;

import com.example.cinema.service.BookingService;
import com.example.cinema.service.CustomerService;
import com.example.cinema.service.MovieService;
import com.example.cinema.service.ScreeningService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CinemaApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieService movieService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ScreeningService screeningService;

    @Autowired
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService.clearAll();
        screeningService.clearAll();
        customerService.clearAll();
        movieService.clearAll();
    }

    @Test
    void createMovieReturnsCreated() throws Exception {
        String requestBody = """
                {
                  "title": "Interstellar",
                  "genre": "Sci-Fi",
                  "durationMinutes": 169,
                  "ageRating": "PG-13"
                }
                """;

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Interstellar"))
                .andExpect(jsonPath("$.genre").value("Sci-Fi"));
    }

    @Test
    void listMoviesReturnsOk() throws Exception {
        createMovie();

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Interstellar"));
    }

    @Test
    void invalidMovieReturnsBadRequest() throws Exception {
        String requestBody = """
                {
                  "title": "",
                  "genre": "",
                  "durationMinutes": 0,
                  "ageRating": ""
                }
                """;

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }

    @Test
    void missingMovieReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/movies/11111111-1111-1111-1111-111111111111"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Movie not found"));
    }

    @Test
    void createBookingReturnsCreated() throws Exception {
        UUID movieId = createMovie();
        UUID customerId = createCustomer();
        UUID screeningId = createScreening(movieId, 80);

        String requestBody = """
                {
                  "customerId": "%s",
                  "screeningId": "%s",
                  "seats": 2
                }
                """.formatted(customerId, screeningId);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.customerId").value(customerId.toString()))
                .andExpect(jsonPath("$.screeningId").value(screeningId.toString()))
                .andExpect(jsonPath("$.seats").value(2))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void bookingNotEnoughSeatsReturnsConflict() throws Exception {
        UUID movieId = createMovie();
        UUID customerId = createCustomer();
        UUID screeningId = createScreening(movieId, 2);

        String requestBody = """
                {
                  "customerId": "%s",
                  "screeningId": "%s",
                  "seats": 100
                }
                """.formatted(customerId, screeningId);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Not enough seats available"));
    }

    private UUID createMovie() throws Exception {
        String requestBody = """
                {
                  "title": "Interstellar",
                  "genre": "Sci-Fi",
                  "durationMinutes": 169,
                  "ageRating": "PG-13"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        return getIdFromResponse(result);
    }

    private UUID createCustomer() throws Exception {
        String requestBody = """
                {
                  "firstName": "Andrii",
                  "lastName": "Kovalenko",
                  "email": "andrii@example.com",
                  "phone": "+123456789"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        return getIdFromResponse(result);
    }

    private UUID createScreening(UUID movieId, int totalSeats) throws Exception {
        String requestBody = """
                {
                  "movieId": "%s",
                  "screeningTime": "2099-06-01T18:30:00",
                  "hallName": "Hall A",
                  "totalSeats": %d
                }
                """.formatted(movieId, totalSeats);

        MvcResult result = mockMvc.perform(post("/api/screenings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        return getIdFromResponse(result);
    }

    private UUID getIdFromResponse(MvcResult result) throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        JsonNode json = objectMapper.readTree(responseBody);
        return UUID.fromString(json.get("id").asText());
    }
}