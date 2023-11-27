package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ChangeGmailController implements Initializable {
    public TextField newGmail;

    SettingsController settingsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void initData(SettingsController settingsController) {
        this.settingsController = settingsController;
    }

    public void change(ActionEvent actionEvent) {
        if (!inputCheck()) return;
        settingsController.gmail.setText(newGmail.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Gmail updated locally");
        alert.show();
        closeStage();
    }

    public boolean inputCheck() {
        boolean check = true;
        if (newGmail.getText().isEmpty()) {  //gmail check
            check = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- no gmail");
            alert.show();
        }
        else if (!Pattern.matches(".+@.+\\..+", newGmail.getText())) {
            check = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- wrong gmail");
            alert.show();
        }
        return check;
    }

    public void closeStage() {
        Stage stage = (Stage) newGmail.getScene().getWindow();
        stage.close();
    }
}
