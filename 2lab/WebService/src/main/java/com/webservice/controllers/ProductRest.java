package com.webservice.controllers;

import com.webservice.entities.Cart;
import com.webservice.entities.Product;
import com.webservice.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class ProductRest {
    @Autowired
    private ProductRepository productRepository;

    ProductRest(ProductRepository productRepository){};

    @GetMapping(value = "/getAllProducts")
    public @ResponseBody Iterable<Product> getAllProducts() {
        return productRepository.findByCart_idIsNull();
    }

    @GetMapping(value = "/getProduct/{id}")
    public Product getProduct(@PathVariable int id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return product;
    }

    @PostMapping(value = "/insertProduct")
    public @ResponseBody Product saveProduct(@RequestBody Product product) {
        Product tempProduct = productRepository.findById(product.getId()).orElse(null);
        if (tempProduct != null) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        return productRepository.saveAndFlush(product);
    }

    @PutMapping(value = "/updateProduct")
    public @ResponseBody Optional<Product> updateProduct(@RequestBody Product product) {
        Product tempProduct = productRepository.findById(product.getId()).orElse(null);
        if (tempProduct == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if (product.getName() == null) product.setName(tempProduct.getName());
        if (product.getType() == null) product.setType(tempProduct.getType());
        if (product.getPrice() == 0) product.setPrice(tempProduct.getPrice());
        if (product.getWarrantyYears() == 0) product.setWarrantyYears(tempProduct.getWarrantyYears());
        if (product.getReviews() == null) {
            if (tempProduct.getReviews().isEmpty()) product.setReviews(new ArrayList<>());
            else product.setReviews(tempProduct.getReviews());
        }

        productRepository.save(product);
        return productRepository.findById(product.getId());
    }

    @DeleteMapping(value = "/deleteProduct/{id}")
    public @ResponseBody String deleteProduct(@PathVariable int id) {
        Product tempProduct = productRepository.findById(id).orElse(null);
        if (tempProduct == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        productRepository.deleteById(id);
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return ("Deleted cart id: " + id);
        else return "Error";
    }
}
