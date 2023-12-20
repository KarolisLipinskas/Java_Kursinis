package com.webservice.entities;

//import javax.persistence.Entity;
//import javax.persistence.OneToOne;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class CartComment extends Comment {
    @JsonBackReference
    @OneToOne
    private Cart cart;

    public CartComment() {
    }

    public CartComment(String text) {
        super(text);
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
