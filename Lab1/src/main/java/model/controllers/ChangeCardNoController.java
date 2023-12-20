package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ChangeCardNoController implements Initializable {
    public TextField newCardNo;

    SettingsController settingsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void initData(SettingsController settingsController) {
        this.settingsController = settingsController;
    }

    public void change(ActionEvent actionEvent) {
        if (!inputCheck()) return;
        String card = "-";
        if(!newCardNo.getText().isEmpty()) card = "****-****-****-" + newCardNo.getText().substring(15);
        settingsController.cardNo.setText(card);
        settingsController.card.setText(newCardNo.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Card number updated locally");
        alert.show();
        closeStage();
    }

    public boolean inputCheck() {
        boolean check = true;
        if (!newCardNo.getText().isEmpty() && !Pattern.matches("[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}", newCardNo.getText())) {  //cardNo check
            check = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- wrong card number");
            alert.show();
        }
        return check;
    }

    public void closeStage() {
        Stage stage = (Stage) newCardNo.getScene().getWindow();
        stage.close();
    }
}
