package model;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer extends User {
    private String cardNo;
    //private List<Cart> cartList;    // kazkaip sutvarkyti


    public Customer() {
    }

    public Customer(int id, String username, String password, String name, String surname, String gmail, LocalDate birthDate, String cardNo) {
        super(id, username, password, name, surname, gmail, birthDate);
        this.cardNo = cardNo;
        //this.cartList = new ArrayList<>();
    }

    public Customer(String username, String password, String name, String surname, String gmail, LocalDate birthDate, String cardNo) {
        super(username, password, name, surname, gmail, birthDate);
        this.cardNo = cardNo;
        //this.cartList = new ArrayList<>();
    }

    public Customer(int id, String username, String password, String name, String surname, String gmail, LocalDate birthDate, String cardNo, List<Cart> cartList) {
        super(id, username, password, name, surname, gmail, birthDate);
        this.cardNo = cardNo;
        //this.cartList = cartList;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /*public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }
    public void addCart(Cart cart) {
        this.cartList.add(cart);
    }*/

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
                //", cartList=" + cartList +
                '}';
    }
}
