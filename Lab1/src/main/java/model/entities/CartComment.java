package model.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class CartComment extends Comment {
    @ManyToOne
    private Cart cart;
    @OneToOne
    private CartComment cartComment;

    public CartComment() {
    }

    public CartComment(String text, Cart cart) {
        super(text);
        this.cart = cart;
    }

    public CartComment(String text, Cart cart, CartComment cartComment) {
        super(text);
        this.cart = cart;
        this.cartComment = cartComment;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public CartComment getCartComment() {
        return cartComment;
    }

    public void setCartComment(CartComment cartComment) {
        this.cartComment = cartComment;
    }
}
