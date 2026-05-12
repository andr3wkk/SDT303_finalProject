package com.example.cinema.unit;

import com.example.cinema.dto.CustomerCreateRequest;
import com.example.cinema.dto.CustomerUpdateRequest;
import com.example.cinema.exception.NotFoundException;
import com.example.cinema.model.Customer;
import com.example.cinema.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService();
    }

    @Test
    void createCustomerSuccessfully() {
        CustomerCreateRequest request = new CustomerCreateRequest(
                "Andrii",
                "Kovalenko",
                "andrii@example.com",
                "+123456789"
        );

        Customer customer = customerService.create(request);

        assertNotNull(customer.getId());
        assertEquals("Andrii", customer.getFirstName());
        assertEquals("Kovalenko", customer.getLastName());
        assertEquals("andrii@example.com", customer.getEmail());
    }

    @Test
    void findCustomerByIdSuccessfully() {
        Customer customer = customerService.create(new CustomerCreateRequest(
                "John",
                "Smith",
                "john@example.com",
                "+111111111"
        ));

        Customer found = customerService.findById(customer.getId());

        assertEquals(customer.getId(), found.getId());
        assertEquals("Smith", found.getLastName());
    }

    @Test
    void throwExceptionWhenCustomerNotFound() {
        assertThrows(NotFoundException.class, () ->
                customerService.findById(java.util.UUID.randomUUID())
        );
    }

    @Test
    void searchCustomerByLastName() {
        customerService.create(new CustomerCreateRequest(
                "Andrii",
                "Kovalenko",
                "andrii@example.com",
                "+123456789"
        ));

        customerService.create(new CustomerCreateRequest(
                "Anna",
                "Brown",
                "anna@example.com",
                "+987654321"
        ));

        List<Customer> result = customerService.findAll("Kovalenko", null, 0, 10);

        assertEquals(1, result.size());
        assertEquals("Andrii", result.getFirst().getFirstName());
    }

    @Test
    void updateCustomerSuccessfully() {
        Customer customer = customerService.create(new CustomerCreateRequest(
                "Andrii",
                "Kovalenko",
                "andrii@example.com",
                "+123456789"
        ));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Andrii",
                "Kovalenko",
                "andrii.updated@example.com",
                "+999999999"
        );

        Customer updated = customerService.update(customer.getId(), updateRequest);

        assertEquals("andrii.updated@example.com", updated.getEmail());
        assertEquals("+999999999", updated.getPhone());
    }

    @Test
    void deleteCustomerSuccessfully() {
        Customer customer = customerService.create(new CustomerCreateRequest(
                "Delete",
                "Me",
                "delete@example.com",
                "+111111111"
        ));

        customerService.delete(customer.getId());

        assertThrows(NotFoundException.class, () ->
                customerService.findById(customer.getId())
        );
    }
}