package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CartController implements Initializable {
    public Label status;
    public Label totalPrice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Cart page");
    }

    public void openMainWindow(ActionEvent actionEvent) throws IOException {
        openWindow("../fxml/main.fxml", "Main page");
    }

    public void openSettingsWindow(ActionEvent actionEvent) throws IOException {
        openWindow("../fxml/settings.fxml", "Settings page");
    }

    public void openOrderListWindow(ActionEvent actionEvent) throws IOException {
        openWindow("../fxml/orderList.fxml", "Order history page");
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        openWindow("../fxml/login.fxml", "Login screen");
    }

    public void openWindow(String path, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();

        Stage cartWindow = new Stage();
        cartWindow.setTitle(title);
        cartWindow.setScene(new Scene(root));
        cartWindow.show();

        Stage stage = (Stage) status.getScene().getWindow();
        stage.close();
    }
}
