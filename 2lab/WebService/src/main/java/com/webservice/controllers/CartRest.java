package com.webservice.controllers;

import com.webservice.entities.Cart;
import com.webservice.entities.Product;
import com.webservice.repos.CartRepository;
import com.webservice.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class CartRest {
    @Autowired
    private CartRepository cartRepository;

    @GetMapping(value = "/getAllCarts")
    public @ResponseBody Iterable<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @GetMapping(value = "/getCart/{id}")
    public Cart getCart(@PathVariable int id) {
        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return cart;
    }

    @PostMapping(value = "/insertCart")
    public @ResponseBody Cart saveCart(@RequestBody Cart cart) {
        if (cart.getCustomer() == null) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        Cart tempCart = cartRepository.findById(cart.getId()).orElse(null);
        if (tempCart != null) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        return cartRepository.saveAndFlush(cart);
    }

    @PutMapping(value = "/updateCart")
    public @ResponseBody Optional<Cart> updateCart(@RequestBody Cart cart) {
        Cart tempCart = cartRepository.findById(cart.getId()).orElse(null);
        if (tempCart == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        double price = 0.0;
        for (Product product: tempCart.getProducts()) {
            price += product.getPrice();
        }

        if (cart.getCartComment() == null) cart.setCartComment(tempCart.getCartComment());
        cart.setPrice(price);
        if (cart.getStatus() == null) cart.setStatus(tempCart.getStatus());
        if (tempCart.getProducts().isEmpty()) cart.setProducts(new ArrayList<>());
        else cart.setProducts(tempCart.getProducts());
        if (cart.getCustomer() == null && tempCart.getCustomer() == null) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        else cart.setCustomer(tempCart.getCustomer());
        cartRepository.save(cart);
        return cartRepository.findById(cart.getId());
    }

    @DeleteMapping(value = "/deleteCart/{id}")
    public @ResponseBody String deleteCart(@PathVariable int id) {
        Cart tempCart = cartRepository.findById(id).orElse(null);
        if (tempCart == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        cartRepository.deleteById(id);
        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart == null) return ("Deleted cart id: " + id);
        else return "Error";
    }
}
