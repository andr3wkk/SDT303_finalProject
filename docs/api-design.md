# Cinema Booking API Design Notes

## Architecture Overview

The project uses a simple layered Spring Boot architecture:

```text
Controller Layer → Service Layer → In-Memory Storage
```

Controllers handle HTTP requests and responses.

Services contain the main business logic.

Data is stored in memory using ConcurrentHashMap.

## Key Components

### Controllers

- MovieController
- CustomerController
- ScreeningController
- BookingController

These classes expose the REST endpoints.

### Services

- MovieService
- CustomerService
- ScreeningService
- BookingService

These classes handle the main application logic.

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
- ScreeningCreateRequest
- BookingCreateRequest

### Exceptions

- NotFoundException
- BadRequestException
- BookingConflictException

GlobalExceptionHandler converts exceptions into JSON error responses.

## API Design

The API uses standard REST methods:

- GET for retrieving data
- POST for creating data
- PUT for updating data
- DELETE for deleting or cancelling data

The API uses JSON request and response bodies.

## Entity Relationships

A movie can have many screenings.

A customer can have many bookings.

A screening can have many bookings.

A booking connects a customer with a screening.

## Business Rules

- A screening must reference an existing movie.
- A booking must reference an existing customer and screening.
- Screening time must be in the future.
- Booking seats must be at least 1.
- A booking cannot be created if there are not enough available seats.
- Creating a booking reduces available seats.
- Cancelling a booking restores available seats.

## Validation and Error Handling

The project uses Jakarta validation annotations:

- @NotBlank
- @NotNull
- @Min
- @Email
- @Future

The API returns:

- 400 Bad Request for invalid input
- 404 Not Found for missing resources
- 409 Conflict for booking conflicts

## Important Design Decisions

In-memory storage was used to keep the project simple and focused on REST API behavior.

DTOs were used to separate request validation from model classes.

Service classes were used to keep business logic separate from controllers.

Cancelled bookings are kept in the system with status CANCELLED instead of being removed.

## Testing

The project includes automated unit tests for service logic.

Test classes:

- MovieServiceTest
- CustomerServiceTest
- ScreeningServiceTest
- BookingServiceTest

The tests cover both successful and unsuccessful scenarios.

Successful test result:

```text
Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Limitations

- Data resets when the application restarts.
- No authentication is included.
- No database is included.
- Seat numbers are not selected individually.

## Possible Improvements

- Add database storage.
- Add authentication.
- Add individual seat selection.
- Add payment support.