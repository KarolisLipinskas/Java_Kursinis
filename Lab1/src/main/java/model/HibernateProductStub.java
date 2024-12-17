package model;

import model.entities.Product;

import java.util.List;

public class HibernateProductStub extends HibernateProduct {
    private final List<Product> products;

    public HibernateProductStub(List<Product> products) {
        super(null); // No EntityManagerFactory needed
        this.products = products;
    }

    @Override
    public List<Product> getAllProducts() {
        return products;
    }

    public List<Object> getProducts() {
        return List.of();
    }
}