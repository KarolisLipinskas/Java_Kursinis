package model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int id;
    private String status;
    private List<Product> products;
    private Comment comment;

    public Cart(int id) {
        this.id = id;
        this.status = "newCart";
        this.products = new ArrayList<>();
    }

    public Cart(int id, String status, List<Product> products, Comment comment) {
        this.id = id;
        this.status = status;
        this.products = products;
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", products=" + products +
                ", comment=" + comment +
                '}';
    }
}
