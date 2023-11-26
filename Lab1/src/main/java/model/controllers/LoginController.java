package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;
import model.HibernateCustomer;
import model.HibernateEmployee;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
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

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateCustomer hibernateCustomer = new HibernateCustomer(entityManagerFactory);

    public void createNewCustomer() {
        if (!checkInput()) return;
        Customer customer = new Customer(
                registerName.getText(),
                registerPass.getText(),
                name.getText(),
                surname.getText(),
                gmail.getText(),
                LocalDate.parse(birthdate.getText()),
                cardNo.getText());

        hibernateCustomer.create(customer);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Nice");
    }

    private boolean checkInput() {
        boolean check = true;
        if (!registerPass.getText().equals(registerPassR.getText())) {
            check = false;
            System.out.println("Passwords do not mach");
        }
        if (!Pattern.matches(".+@.+\\..+", gmail.getText())) {
            check = false;
            System.out.println("Wrong gmail");
        }
        if (!Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", birthdate.getText())) {
            check = false;
            System.out.println("Wrong date");
        }
        return check;
    }

    public void login(ActionEvent actionEvent) throws IOException {
        Customer customer = hibernateCustomer.getCustomer(loginName.getText(), loginPass.getText());
        if (customer != null) {
            System.out.println("Successfully logged in");

            openMainWindow(customer);

            Stage stage = (Stage) loginName.getScene().getWindow();
            stage.close();
        }
        else {
            System.out.println("Wrong username or password");
        }
    }

    public void openMainWindow(Customer customer) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/main.fxml"));
        Parent root = loader.load();

        Stage mainWindow = new Stage();
        mainWindow.setTitle("Main page");
        mainWindow.setScene(new Scene(root));

        MainController mainController = loader.getController();
        mainController.initData(Integer.toString(customer.getId()));

        mainWindow.show();
    }
}
