package model;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Manager extends User {

    public Manager() {
    }

    public Manager(int id, String username, String password, String name, String surname, String gmail, LocalDate birthDate) {
        super(id, username, password, name, surname, gmail, birthDate);
    }
}
