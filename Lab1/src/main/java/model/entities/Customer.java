package model.entities;

import model.HibernateCart;
import model.HibernateCustomer;
import model.HibernateProduct;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer extends User {
    private String cardNo;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customer")
    private List<Cart> cartList;


    public Customer() {
    }

    public Customer(int id, String username, String password, String name, String surname, String gmail, LocalDate birthDate, String cardNo) {
        super(id, username, password, name, surname, gmail, birthDate);
        this.cardNo = cardNo;
        this.cartList = new ArrayList<>();
    }

    public Customer(String username, String password, String name, String surname, String gmail, LocalDate birthDate, String cardNo) {
        super(username, password, name, surname, gmail, birthDate);
        this.cardNo = cardNo;
        this.cartList = new ArrayList<>();
    }

    public Customer(int id, String username, String password, String name, String surname, String gmail, LocalDate birthDate, String cardNo, List<Cart> cartList) {
        super(id, username, password, name, surname, gmail, birthDate);
        this.cardNo = cardNo;
        this.cartList = cartList;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }
    public void addCart(Cart cart) {
        this.cartList.add(cart);
    }

    public void update(Customer customer) {
        this.setUsername(customer.getUsername());
        this.setPassword(customer.getPassword());
        this.setName(customer.getName());
        this.setSurname(customer.getSurname());
        this.setGmail(customer.getGmail());
        this.setBirthDate(customer.getBirthDate());
        this.setCardNo(customer.getCardNo());
        this.setCartList(customer.getCartList());
    }

    public void updateCustomer(HibernateCustomer hibernateCustomer) {
        hibernateCustomer.update(this);
    }

    public void removeCustomer(HibernateCustomer hibernateCustomer, HibernateCart hibernateCart, HibernateProduct hibernateProduct) {
        removeAllCarts(hibernateCart, hibernateProduct);
        hibernateCustomer.delete(this);
    }

    public void removeAllCarts(HibernateCart hibernateCart, HibernateProduct hibernateProduct) {
        for (Cart cart : cartList) {
            cart.removeCart(hibernateCart, hibernateProduct);
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "cardNo='" + cardNo + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gmail='" + gmail + '\'' +
                ", birthDate=" + birthDate +
                ", cartList=" + cartList +
                '}';
    }
}
