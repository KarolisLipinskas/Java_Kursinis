package com.webservice.controllers;

import com.webservice.entities.Product;
import com.webservice.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRest {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping(value = "/getAllProducts")
    public @ResponseBody Iterable<Product> getAllProducts() {
        /*List<Product> products = productRepository.findAll();
        List<Product> output = new ArrayList<>();
        for (Product product : products) {
            if (product.getCart() == null) output.add(product);
        }*/
        return productRepository.findAll();
    }
}
