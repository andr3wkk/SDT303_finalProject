# Cinema Booking API

## Selected Option

Option 2: REST API Service for a Complex Domain

## Description

Cinema Booking API is a Spring Boot REST API for managing a simple cinema seat reservation system.

The system manages movies, customers, screenings, and bookings. A customer can reserve seats for a movie screening. The API checks available seats before confirming a booking.

## Technologies Used

- Java 21
- Spring Boot
- Maven
- JUnit 5
- Swagger / OpenAPI
- Postman
- In-memory storage using ConcurrentHashMap

## Features

- Movie CRUD operations
- Customer CRUD operations
- Screening CRUD operations
- Booking creation and cancellation
- Relationship between movies and screenings
- Relationship between customers, screenings, and bookings
- Filtering and search
- Pagination
- Request validation
- JSON error responses
- Automated unit and integration tests

## How to Build

```bash
mvn clean package
```

## How to Run

You can run the application using Maven:

```bash
mvn spring-boot:run
```

Or build and run the executable JAR:

```bash
mvn clean package
java -jar target/cinema-booking-api-1.0.0.jar
```

The API runs at:

```text
http://localhost:8080
```

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

## How to Test

```bash
mvn test
```

Successful test result:

```text
Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

The project includes unit tests for service logic and integration tests for REST endpoints.

Test classes:

- MovieServiceTest
- CustomerServiceTest
- ScreeningServiceTest
- BookingServiceTest
- CinemaApiIntegrationTest

## Main API Endpoints

### Movies

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/movies` | Create movie |
| GET | `/api/movies` | List movies |
| GET | `/api/movies/{id}` | Get movie by ID |
| PUT | `/api/movies/{id}` | Update movie |
| DELETE | `/api/movies/{id}` | Delete movie |

### Customers

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/customers` | Create customer |
| GET | `/api/customers` | List customers |
| GET | `/api/customers/{id}` | Get customer by ID |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |

### Screenings

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/screenings` | Create screening |
| GET | `/api/screenings` | List screenings |
| GET | `/api/screenings/{id}` | Get screening by ID |
| PUT | `/api/screenings/{id}` | Update screening |
| DELETE | `/api/screenings/{id}` | Delete screening |

### Bookings

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/bookings` | Create booking |
| GET | `/api/bookings` | List bookings |
| GET | `/api/bookings/{id}` | Get booking by ID |
| DELETE | `/api/bookings/{id}` | Cancel booking |

## Filtering and Pagination Examples

```text
GET /api/movies?genre=Sci-Fi
GET /api/customers?lastName=Kovalenko
GET /api/screenings?movieId={movieId}
GET /api/bookings?status=CONFIRMED
GET /api/movies?page=0&size=5
```

## Example Usage Flow

The project uses in-memory storage, so data resets after restarting the application.

Recommended order:

1. Create a movie.
2. Create a customer.
3. Create a screening using the movie ID.
4. Create a booking using the customer ID and screening ID.
5. Cancel the booking.

### Create Movie

```http
POST /api/movies
Content-Type: application/json
```

```json
{
  "title": "Interstellar",
  "genre": "Sci-Fi",
  "durationMinutes": 169,
  "ageRating": "PG-13"
}
```

### Create Customer

```http
POST /api/customers
Content-Type: application/json
```

```json
{
  "firstName": "Andrii",
  "lastName": "Kovalenko",
  "email": "andrii@example.com",
  "phone": "+123456789"
}
```

### Create Screening

```http
POST /api/screenings
Content-Type: application/json
```

```json
{
  "movieId": "PASTE_MOVIE_ID_HERE",
  "screeningTime": "2026-06-01T18:30:00",
  "hallName": "Hall A",
  "totalSeats": 80
}
```

### Create Booking

```http
POST /api/bookings
Content-Type: application/json
```

```json
{
  "customerId": "PASTE_CUSTOMER_ID_HERE",
  "screeningId": "PASTE_SCREENING_ID_HERE",
  "seats": 2
}
```

## Error Handling

The API returns JSON error responses.

Common errors:

```text
400 Bad Request - invalid request body
404 Not Found - resource does not exist
409 Conflict - not enough seats available
```

Example error response:

```json
{
  "timestamp": "2026-05-13T01:30:00",
  "status": 409,
  "error": "Not enough seats available"
}
```

## Data Storage

The project uses in-memory storage with ConcurrentHashMap.

Data is lost when the application stops. This is acceptable for this project because database integration is optional.

## Project Structure

```text
cinema-booking-api/
├── pom.xml
├── README.md
├── docs/
│   └── api-design.md
├── postman/
│   └── Cinema-Booking-API.postman_collection.json
├── screenshots/
└── src/
    ├── main/java/com/example/cinema/
    │   ├── CinemaApplication.java
    │   ├── dto/
    │   ├── exception/
    │   ├── model/
    │   ├── service/
    │   └── web/
    └── test/java/com/example/cinema/
        ├── unit/
        └── integration/
```

## Optional Oracle Cloud Deployment

The Cinema Booking API was also deployed on Oracle Cloud Infrastructure as optional execution evidence.

The application runs on an Oracle Cloud Ubuntu VM with:

- Java 21
- Maven
- Git
- Spring Boot application running on port 8080

Public API base URL:

```text
http://79.76.55.31:8080
```

Swagger UI:

```text
http://79.76.55.31:8080/swagger-ui.html
```

Example public endpoints:

```text
GET http://79.76.55.31:8080/api/movies
POST http://79.76.55.31:8080/api/movies
GET http://79.76.55.31:8080/api/customers
GET http://79.76.55.31:8080/api/screenings
GET http://79.76.55.31:8080/api/bookings
```

The project was deployed by cloning the GitHub repository on the VM, running the automated tests, building the executable JAR, and starting the application with:

```bash
java -jar target/cinema-booking-api-1.0.0.jar
```

This cloud deployment demonstrates that the application can run outside the local development environment and can be tested through a public IP using Postman or Swagger UI.

Note: The public IP is used for demonstration purposes and may be removed after grading to avoid unnecessary cloud resource usage.

## Screenshots

Screenshots are included in the `screenshots` folder and show:

- application running
- Swagger UI
- Postman requests and responses
- successful test execution
- optional Oracle Cloud deployment evidence

## Limitations

- No database persistence
- No authentication
- No payment system
- No individual seat numbers
- Oracle Cloud public IP is for demonstration and may be removed after grading

## Possible Improvements

- Add database storage with H2 or PostgreSQL
- Add user authentication
- Add individual seat selection
- Add payment status
- Add role-based access for customers and administrators