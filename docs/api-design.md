# Cinema Booking API Design Notes

## Architecture Overview

The project uses a simple layered Spring Boot architecture:

```text
Controller Layer → Service Layer → In-Memory Storage
```

Controllers handle HTTP requests and responses.

Services contain the main business logic.

Data is stored in memory using `ConcurrentHashMap`.

This structure keeps the project simple, readable, and easy to test.

## Key Components

### Controllers

- MovieController
- CustomerController
- ScreeningController
- BookingController

These classes expose the REST endpoints and handle HTTP requests from clients such as Postman, Swagger UI, or curl.

### Services

- MovieService
- CustomerService
- ScreeningService
- BookingService

These classes handle the main application logic, including creating records, updating data, checking relationships between entities, and applying booking rules.

### Models

- Movie
- Customer
- Screening
- Booking
- BookingStatus

These classes represent the main domain objects.

### DTOs

DTO classes are used for request bodies and validation.

Examples:

- MovieCreateRequest
- MovieUpdateRequest
- CustomerCreateRequest
- CustomerUpdateRequest
- ScreeningCreateRequest
- ScreeningUpdateRequest
- BookingCreateRequest

DTOs help separate incoming request data from the internal model classes.

### Exceptions

- NotFoundException
- BadRequestException
- BookingConflictException

`GlobalExceptionHandler` converts exceptions into clear JSON error responses.

## API Design

The API uses standard REST methods:

- GET for retrieving data
- POST for creating data
- PUT for updating data
- DELETE for deleting or cancelling data

The API uses JSON request and response bodies.

The API also uses appropriate HTTP status codes:

- 200 OK for successful retrieval or update
- 201 Created for successful creation
- 204 No Content for successful deletion or cancellation
- 400 Bad Request for invalid input
- 404 Not Found for missing resources
- 409 Conflict for booking conflicts

## Entity Relationships

A movie can have many screenings.

A customer can have many bookings.

A screening can have many bookings.

A booking connects a customer with a screening.

This means the project models a realistic domain with multiple related entities instead of only one simple object.

## Business Rules

- A screening must reference an existing movie.
- A booking must reference an existing customer and screening.
- Screening time must be in the future.
- Booking seats must be at least 1.
- A booking cannot be created if there are not enough available seats.
- Creating a booking reduces available seats.
- Cancelling a booking restores available seats.
- Cancelled bookings are kept in the system with status `CANCELLED` instead of being removed completely.

## Filtering and Pagination

The API includes filtering and search options for several resources.

Examples:

```text
GET /api/movies?genre=Sci-Fi
GET /api/customers?lastName=Kovalenko
GET /api/screenings?movieId={movieId}
GET /api/bookings?status=CONFIRMED
```

Pagination is also supported for list endpoints using `page` and `size` query parameters.

Example:

```text
GET /api/movies?page=0&size=5
```

## Validation and Error Handling

The project uses Jakarta validation annotations:

- `@NotBlank`
- `@NotNull`
- `@Min`
- `@Email`
- `@Future`

Invalid request bodies return `400 Bad Request`.

Missing resources return `404 Not Found`.

Booking conflicts, such as not enough available seats, return `409 Conflict`.

The API returns structured JSON error responses so the client can understand what went wrong.

## Important Design Decisions

In-memory storage was used to keep the project simple and focused on REST API behavior. Database integration was optional for this final project, so `ConcurrentHashMap` was enough for demonstrating the required functionality.

DTOs were used to separate request validation from model classes.

Service classes were used to keep business logic separate from controllers.

Cancelled bookings are kept in the system with status `CANCELLED` instead of being removed. This makes the booking history easier to understand.

The application was also packaged as an executable Spring Boot JAR so it can run from the command line and be deployed on a cloud VM.

## Testing

The project includes automated tests for both service logic and REST endpoints.

Unit test classes:

- MovieServiceTest
- CustomerServiceTest
- ScreeningServiceTest
- BookingServiceTest

Integration test class:

- CinemaApiIntegrationTest

The service tests verify the main business logic, including successful creation, updates, deletion, validation, not-found cases, and booking conflict cases.

The integration tests verify REST API behavior using HTTP requests and responses. They check status codes, JSON responses, validation errors, not-found responses, and conflict responses.

The tests cover both positive and negative scenarios.

Successful test result:

```text
Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Optional Cloud Deployment

The application was also deployed on Oracle Cloud Infrastructure as optional execution evidence.

The cloud deployment uses:

- Oracle Cloud VM
- Ubuntu 24.04
- Java 21
- Maven
- Git
- Spring Boot JAR running on port 8080

This demonstrates that the project can run outside the local development environment and can be tested using a public IP address through Postman or Swagger UI.

## Limitations

- Data resets when the application restarts.
- No authentication is included.
- No database is included.
- Seat numbers are not selected individually.
- No payment processing is included.

## Possible Improvements

- Add database storage with H2 or PostgreSQL.
- Add authentication.
- Add individual seat selection.
- Add payment support.
- Add user roles such as customer and administrator.
- Add persistent booking history.