package com.webservice.entities;

//import javax.persistence.Entity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Manager extends User {

    public Manager() {
    }

    public Manager(int id, String username, String password, String name, String surname, String gmail, LocalDate birthDate) {
        super(id, username, password, name, surname, gmail, birthDate);
    }
}
