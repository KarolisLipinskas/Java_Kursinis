package model.entities;

import model.HibernateCart;
import model.HibernateCartComments;
import model.HibernateProduct;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String status;
    private double price;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart")
    @Fetch(value = FetchMode.SUBSELECT)
    //@OrderBy("id")
    private List<Product> products;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<CartComment> cartComment;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Manager manager;

    public Cart() {
    }

    public Cart(String status, Customer customer) {
        this.status = status;
        this.products = new ArrayList<>();
        this.customer = customer;
        this.creationDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

    public Cart(int id, String status, double price, List<Product> products, List<CartComment> cartComment) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.products = products;
        this.cartComment = cartComment;
        this.creationDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
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

    public List<CartComment> getCartComment() {
        return cartComment;
    }

    public void setCartComment(List<CartComment> cartComment) {
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void update(Cart cart) {
        this.setStatus(cart.getStatus());
        this.setPrice(cart.getPrice());
        this.setProducts(cart.getProducts());
        this.setCartComment(cart.getCartComment());
        this.setManager(cart.getManager());
        this.setUpdateDate(cart.getUpdateDate());
    }

    public void updateCart(HibernateCart hibernateCart) {
        hibernateCart.update(this);
    }

    public void removeCart(HibernateCart hibernateCart, HibernateProduct hibernateProduct, HibernateCartComments hibernateCartComments) {
        removeAllItems(hibernateProduct);
        removeAllComments(hibernateCartComments);
        hibernateCart.delete(this);
    }

    public void removeAllItems(HibernateProduct hibernateProduct) {
        for (Product product : products) {
            hibernateProduct.delete(product);
        }
    }

    public void removeAllComments(HibernateCartComments hibernateCartComments) {
        for (CartComment comment : cartComment.reversed()) {
            hibernateCartComments.delete(comment);
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
