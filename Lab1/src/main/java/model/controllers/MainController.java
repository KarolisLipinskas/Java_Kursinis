package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public TextField priceMin;
    public TextField priceMax;
    public TextField quantity;
    public TableView table;
    public ComboBox<String> types;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        types.getItems().addAll("All", "Bike", "Brakes", "Fork", "Frame", "Handlebars", "Pedals", "Shock", "Wheels");
        System.out.println("Main page");
    }

    public void addToCart(ActionEvent actionEvent) {
        System.out.println(quantity.getText());
    }

    public void filter(ActionEvent actionEvent) {
        System.out.println(types.getValue());
        System.out.println(priceMin.getText() + " " + priceMax.getText());
    }

    public void openCartWindow(ActionEvent actionEvent) throws IOException {
        openWindow("../fxml/cart.fxml", "Cart page");
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

        Stage stage = (Stage) priceMax.getScene().getWindow();
        stage.close();
    }
}
