package com.androidapp.entities;

//import model.HibernateProduct;



import java.util.List;

public class Product {
    private int id;
    private String name;
    private String type;
    private double price;
    private int warrantyYears;

    private List<Review> reviews;
    private Cart cart;

    public Product() {
    }

    public Product(String name, String type, double price, int warrantyYears) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.warrantyYears = warrantyYears;
    }

    public Product(Product product, Cart cart) {
        this.name = product.getName();
        this.type = product.getType();
        this.price = product.getPrice();
        this.warrantyYears = product.getWarrantyYears();
        this.cart = cart;
    }

    public Product(String name, String type, double price, int warrantyYears, List<Review> reviews) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.warrantyYears = warrantyYears;
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getWarrantyYears() {
        return warrantyYears;
    }

    public void setWarrantyYears(int warrantyYears) {
        this.warrantyYears = warrantyYears;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void update(Product product) {
        this.setName(product.getName());
        this.setType(product.getType());
        this.setPrice(product.getPrice());
        this.setWarrantyYears(product.getWarrantyYears());
        this.setReviews(product.getReviews());
    }

    /*public void updateProduct(HibernateProduct hibernateProduct) {
        hibernateProduct.update(this);
    }

    public void removeProduct(HibernateProduct hibernateProduct) {
        hibernateProduct.delete(this);
    }*/

    @Override
    public String toString() {
        return name + '\n' + type + "     " + price + "€";
    }

    /*@Override
    public String toString() {
        String r = "{";
        if (reviews.isEmpty()) r += "null";
        else {
            for (Review review : reviews) {
                r += review + ", ";
            }
        }
        r += "}";
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", warrantyYears=" + warrantyYears +
                ", reviews=" + r +
                '}';
    }*/
}
