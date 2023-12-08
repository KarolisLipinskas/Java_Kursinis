package com.webservice.controllers;

import com.webservice.entities.Customer;
import com.webservice.repos.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRest {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping(value = "/getAllCustomers")
    public @ResponseBody Iterable<Customer> getAllProducts() {
        return customerRepository.findAll();
    }
}
