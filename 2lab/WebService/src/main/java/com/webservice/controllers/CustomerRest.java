package com.webservice.controllers;

import com.google.gson.Gson;
import com.webservice.entities.Customer;
import com.webservice.errors.CustomerNotFound;
import com.webservice.repos.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.aspectj.weaver.Iterators.one;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CustomerRest {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping(value = "/getAllCustomers")
    public @ResponseBody Iterable<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping(value = "/getCustomer/{id}")
    public Customer getCustomer(@PathVariable int id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFound(id));
        return customer;
    }

    @PostMapping(value = "/getCustomerByLogin")
    public @ResponseBody Customer getCustomerByLogin(@RequestBody String jsonData) {
        List<Customer> customers = customerRepository.findAll();

        Gson parser = new Gson();
        Properties properties = parser.fromJson(jsonData, Properties.class);

        for (Customer customer: customers) {
            if (customer.getPassword().equals(properties.getProperty("password")) && customer.getUsername().equals(properties.getProperty("username"))) {
                return customer;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/insertCustomer")
    public @ResponseBody Customer saveCustomer(@RequestBody Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @PutMapping(value = "/updateCustomer")
    public @ResponseBody Optional<Customer> updateCustomer(@RequestBody Customer customer) {
        Customer tempCustomer = customerRepository.findById(customer.getId()).orElse(null);
        if (tempCustomer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        System.out.println(tempCustomer.getName());
        if (customer.getCardNo() == null) customer.setCardNo(tempCustomer.getCardNo());
        if (customer.getBirthDate() == null) customer.setBirthDate(tempCustomer.getBirthDate());
        if (customer.getGmail() == null) customer.setGmail(tempCustomer.getGmail());
        if (customer.getName() == null) customer.setName(tempCustomer.getName());
        if (customer.getPassword() == null) customer.setPassword(tempCustomer.getPassword());
        if (customer.getSurname() == null) customer.setSurname(tempCustomer.getSurname());
        if (customer.getUsername() == null) customer.setUsername(tempCustomer.getUsername());
        if (customer.getCartList() == null) {
            if (tempCustomer.getCartList().isEmpty()) customer.setCartList(new ArrayList<>());
            else customer.setCartList(tempCustomer.getCartList());
        }
        customerRepository.save(customer);
        return customerRepository.findById(customer.getId());
    }

    @DeleteMapping(value = "/deleteCustomer/{id}")
    public @ResponseBody String deleteCustomer(@PathVariable int id) {
        //add delete product
        //add delete cart
        customerRepository.deleteById(id);
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) return ("Deleted user id: " + id);
        else return "Error";
    }
}
