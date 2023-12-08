package com.webservice.controllers;

import com.webservice.entities.Cart;
import com.webservice.repos.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartRest {
    @Autowired
    private CartRepository cartRepository;

    @GetMapping(value = "/getAllCarts")
    public @ResponseBody Iterable<Cart> getAllProducts() {
        return cartRepository.findAll();
    }
}
