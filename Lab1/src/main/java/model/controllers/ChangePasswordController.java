package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    public TextField newPassword;
    public TextField newPasswordR;

    SettingsController settingsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void initData(SettingsController settingsController) {
        this.settingsController = settingsController;
    }

    public void change(ActionEvent actionEvent) {
        if (!inputCheck()) return;
        String pass = "";
        for (int i = 0; i < newPassword.getText().length(); i++) pass += "*";
        settingsController.loginPass.setText(pass);
        settingsController.password.setText(newPassword.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Password updated locally");
        alert.show();
        closeStage();
    }

    public boolean inputCheck() {
        boolean check = true;
        String alertText = "";
        if (newPassword.getText().isEmpty()) {    //password check
            check = false;
            alertText += "- no password\n";
        }
        if (newPasswordR.getText().isEmpty()) {
            check = false;
            alertText += "- no repeat password\n";
        }
        if (!newPassword.getText().isEmpty() && !newPasswordR.getText().isEmpty() && !newPassword.getText().equals(newPasswordR.getText())) {
            check = false;
            alertText += "- passwords do not mach\n";
        }

        if (!check) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(alertText);
            alert.show();
        }

        return check;
    }

    public void closeStage() {
        Stage stage = (Stage) newPassword.getScene().getWindow();
        stage.close();
    }
}
