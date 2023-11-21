package model;

import java.time.LocalDate;
import java.util.List;

public class Manager extends User {
    private List<Cart> cartList;

    public Manager(int id, String username, String password, String name, String surname, String gmail, LocalDate birthDate) {
        super(id, username, password, name, surname, gmail, birthDate);
    }

    public Manager(int id, String username, String password, String name, String surname, String gmail, LocalDate birthDate, List<Cart> cartList) {
        super(id, username, password, name, surname, gmail, birthDate);
        this.cartList = cartList;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }
}
