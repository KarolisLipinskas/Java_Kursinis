package model.entities;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Administrator extends User{
    public Administrator() {
    }

    public Administrator(int id, String username, String password, String name, String surname, String gmail, LocalDate birthDate) {
        super(id, username, password, name, surname, gmail, birthDate);
    }

    public Administrator(String username, String password, String name, String surname, String gmail, LocalDate birthDate) {
        super(username, password, name, surname, gmail, birthDate);
    }
}
