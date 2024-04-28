package com.androidapp.entities;

//import javax.persistence.Entity;
//import javax.persistence.ManyToOne;

public class Review extends Comment {
    private int rating;
    private Product product;

    public Review() {
    }

    public Review(String text, int rating) {
        super(text);
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Review{" +
                "rating=" + rating +
                '}';
    }
}
