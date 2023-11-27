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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

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

        registerName.clear();
        registerPass.clear();
        registerPassR.clear();
        name.clear();
        surname.clear();
        gmail.clear();
        birthdate.clear();
        cardNo.clear();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Account created");
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

        if (!cardNo.getText().isEmpty() && !Pattern.matches("[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}", cardNo.getText())) {  //cardNo check
            check = false;
            alertText += "- wrong cardNo\n";
        }

        //Error alert
        if(!check) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(alertText);
            alert.show();
        }

        return check;
    }

    public void login(ActionEvent actionEvent) throws IOException {
        Customer customer = hibernateCustomer.getCustomer(loginName.getText(), loginPass.getText());
        if (customer != null) {
            openMainWindow(customer);

            Stage stage = (Stage) loginName.getScene().getWindow();
            stage.close();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Wrong nickname or password");
            alert.show();
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
