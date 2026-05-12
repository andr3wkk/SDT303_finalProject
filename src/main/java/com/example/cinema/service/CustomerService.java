package com.example.cinema.service;

import com.example.cinema.dto.CustomerCreateRequest;
import com.example.cinema.dto.CustomerUpdateRequest;
import com.example.cinema.exception.NotFoundException;
import com.example.cinema.model.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomerService {

    private final ConcurrentHashMap<UUID, Customer> customers = new ConcurrentHashMap<>();

    public Customer create(CustomerCreateRequest request) {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Customer customer = new Customer(
                id,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                now,
                now
        );

        customers.put(id, customer);
        return customer;
    }

    public List<Customer> findAll(String lastName, String email, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);

        return customers.values()
                .stream()
                .filter(customer -> lastName == null || customer.getLastName().equalsIgnoreCase(lastName))
                .filter(customer -> email == null || customer.getEmail().equalsIgnoreCase(email))
                .skip((long) safePage * safeSize)
                .limit(safeSize)
                .toList();
    }

    public Customer findById(UUID id) {
        Customer customer = customers.get(id);

        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }

        return customer;
    }

    public Customer update(UUID id, CustomerUpdateRequest request) {
        Customer customer = findById(id);

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setUpdatedAt(LocalDateTime.now());

        return customer;
    }

    public void delete(UUID id) {
        Customer removed = customers.remove(id);

        if (removed == null) {
            throw new NotFoundException("Customer not found");
        }
    }

    public void clearAll() {
        customers.clear();
    }
}