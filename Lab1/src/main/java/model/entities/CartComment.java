package model.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class CartComment extends Comment {
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
