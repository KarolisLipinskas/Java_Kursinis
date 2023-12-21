package model.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Manager extends User {
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "manager")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Cart> carts;

    public Manager() {
    }

    public Manager(int id, String username, String password, String name, String surname, String gmail, LocalDate birthDate) {
        super(id, username, password, name, surname, gmail, birthDate);
    }

    public Manager(String username, String password, String name, String surname, String gmail, LocalDate birthDate) {
        super(username, password, name, surname, gmail, birthDate);
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
}
