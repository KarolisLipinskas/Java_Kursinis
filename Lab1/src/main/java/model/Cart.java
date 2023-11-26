package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String status;
    private double price;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart")
    //@OrderBy("id")
    private List<Product> products;
    @OneToOne(mappedBy = "cart")
    private CartComment cartComment;
    @ManyToOne
    private Customer customer;

    public Cart() {
    }

    public Cart(String status, Customer customer) {
        this.status = status;
        this.products = new ArrayList<>();
        this.customer = customer;
    }

    public Cart(int id, String status, double price, List<Product> products, CartComment cartComment) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.products = products;
        this.cartComment = cartComment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public CartComment getCartComment() {
        return cartComment;
    }

    public void setCartComment(CartComment cartComment) {
        this.cartComment = cartComment;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }

    public void updateCart(Cart cart) {
        this.setStatus(cart.getStatus());
        this.setPrice(cart.getPrice());
        this.setProducts(cart.getProducts());
        this.setCartComment(cart.getCartComment());
    }

    public void removeCart(HibernateCart hibernateCart, HibernateProduct hibernateProduct) {
        removeAllItems(hibernateProduct);
        //...
    }

    public void removeAllItems(HibernateProduct hibernateProduct) {
        for (Product product : products) {
            hibernateProduct.delete(product);
        }
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", price='" + price + '\'' +
                ", products=" + products +
                ", comment=" + cartComment +
                '}';
    }
}
