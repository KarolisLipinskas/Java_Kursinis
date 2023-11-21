package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Customer;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class LoginController implements Initializable {
    public TextField loginName;
    public PasswordField loginPass;
    public TextField registerName;
    public PasswordField registerPass;
    public PasswordField registerPassR;
    public TextField name;
    public TextField surname;
    public TextField gmail;
    public TextField birthdate;
    public TextField cardNo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Nice");
    }

    public void createNewUser(ActionEvent actionEvent) {
        if (!checkInput()) return;
        Customer customer = new Customer(1, registerName.getText(), registerPass.getText(), name.getText(), surname.getText(), gmail.getText(), LocalDate.parse(birthdate.getText()), cardNo.getText());
        System.out.println(customer);
    }

    private boolean checkInput() {
        boolean check = true;
        if (!registerPass.getText().equals(registerPassR.getText())) {
            check = false;
            System.out.println("Passwords do not mach");
        }
        if (!Pattern.matches(".+@.+\\..+", gmail.getText())) { //pakeisti
            check = false;
            System.out.println("Wrong gmail");
        }
        if (!Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", birthdate.getText())) {
            check = false;
            System.out.println("Wrong date");
        }
        return check;
    }

    public void login(ActionEvent actionEvent) {
        Customer customer = new Customer(1, "C1", "Pass", "Name", "Surname", "1@gmail.com", LocalDate.parse("2020-01-01"), "1111");
        if (loginName.getText().equals(customer.getUsername()) && loginPass.getText().equals(customer.getPassword())) {
            System.out.println("Successfully logged in");
        }
        else {
            System.out.println("Wrong username or password");
        }
    }
}
