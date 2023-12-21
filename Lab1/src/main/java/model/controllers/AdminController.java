package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.HibernateAdministrator;
import model.HibernateCustomer;
import model.HibernateManager;
import model.entities.Administrator;
import model.entities.Customer;
import model.entities.Manager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AdminController implements Initializable {
    public ComboBox<String> userType;
    public TextField registerName;
    public PasswordField registerPass;
    public PasswordField registerPassR;
    public TextField name;
    public TextField surname;
    public TextField gmail;
    public TextField birthdate;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateCustomer hibernateCustomer = new HibernateCustomer(entityManagerFactory);
    HibernateManager hibernateManager = new HibernateManager(entityManagerFactory);
    HibernateAdministrator hibernateAdministrator = new HibernateAdministrator(entityManagerFactory);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userType.getItems().addAll("Administrator", "Manager");
    }

    public void registerNewUser(ActionEvent actionEvent) {
        if (!checkInput()) return;
        if (userType.getValue().equals("Administrator")) createNewAdmin();
        else if (userType.getValue().equals("Manager")) createNewManager();
    }

    public void createNewManager() {
        Manager manager = new Manager(
                registerName.getText(),
                registerPass.getText(),
                name.getText(),
                surname.getText(),
                gmail.getText(),
                LocalDate.parse(birthdate.getText()));

        hibernateManager.create(manager);

        registerName.clear();
        registerPass.clear();
        registerPassR.clear();
        name.clear();
        surname.clear();
        gmail.clear();
        birthdate.clear();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("New manager account created");
        alert.show();
    }

    public void createNewAdmin() {
        Administrator administrator = new Administrator(
                registerName.getText(),
                registerPass.getText(),
                name.getText(),
                surname.getText(),
                gmail.getText(),
                LocalDate.parse(birthdate.getText()));

        hibernateAdministrator.create(administrator);

        registerName.clear();
        registerPass.clear();
        registerPassR.clear();
        name.clear();
        surname.clear();
        gmail.clear();
        birthdate.clear();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("New administrator account created");
        alert.show();
    }

    private boolean checkInput() {
        boolean check = true;
        String alertText = "";

        if (registerName.getText().isEmpty()) {   //nickname check
            check = false;
            alertText += "- no nickname\n";
        }
        else {
            for (Administrator administrator : hibernateAdministrator.getAllAdmins()) {
                if (registerName.getText().equals(administrator.getUsername())) {
                    check = false;
                    alertText += "- nickname already exist\n";
                    break;
                }
            }
            for (Manager manager : hibernateManager.getAllManagers()) {
                if (registerName.getText().equals(manager.getUsername())) {
                    check = false;
                    alertText += "- nickname already exist\n";
                    break;
                }
            }
            for (Customer customer : hibernateCustomer.getAllCustomers()) {
                if (registerName.getText().equals(customer.getUsername())) {
                    check = false;
                    alertText += "- nickname already exist\n";
                    break;
                }
            }
        }

        if (registerPass.getText().isEmpty()) {    //password check
            check = false;
            alertText += "- no password\n";
        }
        if (registerPassR.getText().isEmpty()) {
            check = false;
            alertText += "- no repeat password\n";
        }
        if (!registerPass.getText().isEmpty() && !registerPassR.getText().isEmpty() && !registerPass.getText().equals(registerPassR.getText())) {
            check = false;
            alertText += "- passwords do not mach\n";
        }

        if (name.getText().isEmpty()) { //name check
            check = false;
            alertText += "- no name\n";
        }

        if (surname.getText().isEmpty()) { //surname check
            check = false;
            alertText += "- no surname\n";
        }

        if (gmail.getText().isEmpty()) {  //gmail check
            check = false;
            alertText += "- no gmail\n";
        }
        else if (!Pattern.matches(".+@.+\\..+", gmail.getText())) {
            check = false;
            alertText += "- wrong gmail\n";
        }

        if (birthdate.getText().isEmpty()) {   //birthdate check
            check = false;
            alertText += "- no birthdate\n";
        }
        else if (!Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", birthdate.getText())) {
            check = false;
            alertText += "- wrong date\n";
        }

        //Error alert
        if(!check) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(alertText);
            alert.show();
        }

        return check;
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/login.fxml"));
        Stage cartWindow = getStage(loader, "Login screen");

        cartWindow.show();
        closeStage();
    }

    public Stage getStage(FXMLLoader loader ,String title) throws IOException {
        Parent root = loader.load();
        Stage window = new Stage();
        window.setTitle(title);
        window.setScene(new Scene(root));
        return window;
    }

    public void closeStage() {
        Stage stage = (Stage) registerName.getScene().getWindow();
        stage.close();
    }
}
