package model;

public class Product {
    private int id;
    private String name;
    private double price;
    private int warrantyYears;
    private Review review;

    public Product(int id, String name, double price, int warrantyYears) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.warrantyYears = warrantyYears;
    }

    public Product(int id, String name, double price, int warrantyYears, Review review) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.warrantyYears = warrantyYears;
        this.review = review;
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

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", warrantyYears=" + warrantyYears +
                ", review=" + review +
                '}';
    }
}
