package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    public TextField name;
    public TextField surname;
    public TextField birthdate;
    public Label gmail;
    public Label cardNo;
    public Label loginName;
    public Label loginPass;

    public Label customerId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Settings page");
    }

    public void initData(String id) {
        customerId.setText(id);
        System.out.println(customerId);
    }

    public void openMainWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/main.fxml"));
        Stage mainWindow = getStage(loader, "Main page");

        MainController mainController = loader.getController();
        mainController.initData(customerId.getText());

        mainWindow.show();
        closeStage();
    }

    public void openCartWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/cart.fxml"));
        Stage cartWindow = getStage(loader, "Cart page");

        CartController cartController = loader.getController();
        cartController.initData(customerId.getText());

        cartWindow.show();
        closeStage();
    }

    public void openOrderListWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/orderList.fxml"));
        Stage orderListWindow = getStage(loader, "Order history page");

        OrderListController orderListController = loader.getController();
        orderListController.initData(customerId.getText());

        orderListWindow.show();
        closeStage();
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
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    public void openWindow(String path, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();

        Stage cartWindow = new Stage();
        cartWindow.setTitle(title);
        cartWindow.setScene(new Scene(root));
        cartWindow.show();

        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }
}
