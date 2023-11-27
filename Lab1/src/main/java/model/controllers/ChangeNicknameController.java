package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import model.HibernateCustomer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URL;
import java.util.ResourceBundle;

public class ChangeNicknameController implements Initializable {
    public TextField newNickname;

    SettingsController settingsController;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateCustomer hibernateCustomer = new HibernateCustomer(entityManagerFactory);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void initData(SettingsController settingsController) {
        this.settingsController = settingsController;
    }

    public void change(ActionEvent actionEvent) {
        if (!inputCheck()) return;
        settingsController.loginName.setText(newNickname.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Nickname updated locally");
        alert.show();
        closeStage();
    }

    public boolean inputCheck() {
        boolean check = true;
        if (newNickname.getText().isEmpty()) {   //nickname check
            check = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- no nickname");
            alert.show();
        }
        else {
            for (Customer customer : hibernateCustomer.getAllCustomers()) {
                if (newNickname.getText().equals(customer.getUsername())) {
                    check = false;
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("- nickname already exist");
                    alert.show();
                    break;
                }
            }
        }
        return check;
    }

    public void closeStage() {
        Stage stage = (Stage) newNickname.getScene().getWindow();
        stage.close();
    }
}
