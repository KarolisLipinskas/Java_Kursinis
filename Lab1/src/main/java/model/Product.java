package model;

import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String type;
    private double price;
    private int warrantyYears;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    @OrderBy("rating")
    private List<Review> reviews;

    public Product() {
    }

    public Product(String name, String type, double price, int warrantyYears) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.warrantyYears = warrantyYears;
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

    @Override
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
    }
}
