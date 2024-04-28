package com.androidapp.entities;

//import javax.persistence.Entity;
//import javax.persistence.OneToOne;

public class CartComment extends Comment {
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
