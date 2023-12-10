package com.webservice.errors;

public class CustomerNotFound extends RuntimeException {
    public CustomerNotFound(int id) {
        super("Customer no found id: " + id);
    }
}
