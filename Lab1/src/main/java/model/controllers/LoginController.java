package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Utils.CustomerValidator;
import model.entities.Customer;
import model.HibernateCustomer;
import model.HibernateManager;
import model.entities.Manager;

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
    HibernateManager hibernateManager = new HibernateManager(entityManagerFactory);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void createNewCustomer() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        Customer customer = new Customer(
                registerName.getText(),
                registerPass.getText(),
                name.getText(),
                surname.getText(),
                gmail.getText(),
                LocalDate.parse(birthdate.getText()),
                cardNo.getText());

        if (CustomerValidator.validate(customer, registerPassR.getText())) {
            hibernateCustomer.create(customer);
            registerName.clear();
            registerPass.clear();
            registerPassR.clear();
            name.clear();
            surname.clear();
            gmail.clear();
            birthdate.clear();
            cardNo.clear();
            alert.setContentText("Account created");
            alert.show();
        }
    }

    public void login(ActionEvent actionEvent) throws IOException {
        Customer customer = hibernateCustomer.getCustomer(loginName.getText(), loginPass.getText());
        Manager manager = hibernateManager.getManager(loginName.getText(), loginPass.getText());
        if (customer != null) {
            openMainWindow(customer);
        }
        else if (manager != null) {
            openManagerWindow();
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

        Stage stage = (Stage) loginName.getScene().getWindow();
        stage.close();
    }

    public void openManagerWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/manager.fxml"));
        Parent root = loader.load();

        Stage managerWindow = new Stage();
        managerWindow.setTitle("Manager page");
        managerWindow.setScene(new Scene(root));

        managerWindow.show();

        Stage stage = (Stage) loginName.getScene().getWindow();
        stage.close();
    }
}
