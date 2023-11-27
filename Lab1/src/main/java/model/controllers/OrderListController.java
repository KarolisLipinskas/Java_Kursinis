package model.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderListController implements Initializable {
    public TableView<OrderListTableParameters> table;
    public TableColumn<OrderListTableParameters, String> order_id;
    public TableColumn<OrderListTableParameters, String> price;
    public TableColumn<OrderListTableParameters, String> status;
    private ObservableList<OrderListTableParameters> data = FXCollections.observableArrayList();

    public Label customerId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateCart hibernateCart = new HibernateCart(entityManagerFactory);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void initData(String id) {
        customerId.setText(id);
        loadTable();
    }

    public void loadTable() {
        table.getItems().clear();
        order_id.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("order_id"));
        price.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("price"));
        status.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("status"));

        for (Cart cart : hibernateCart.getAllCarts()) {
            if (cart.getCustomer().getId() == Integer.parseInt(customerId.getText())) {
                OrderListTableParameters orderListTableParameters = new OrderListTableParameters(
                        Integer.toString(cart.getId()),
                        Double.toString(cart.getPrice()),
                        cart.getStatus());

                data.add(orderListTableParameters);
            }
        }
        table.setItems(data);
    }

    public void viewOrder(ActionEvent actionEvent) throws IOException {
        OrderListTableParameters p = table.getSelectionModel().getSelectedItem();
        if (p == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No order selected");
            alert.show();
            return;
        }
        Cart cart = hibernateCart.getCart(p.getOrder_id());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/cart.fxml"));
        Stage cartWindow = getStage(loader, "Cart page");

        CartController cartController = loader.getController();
        cartController.initData(customerId.getText(), cart);

        cartWindow.show();
        closeStage();
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

    public void openSettingsWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/settings.fxml"));
        Stage settingsWindow = getStage(loader, "Settings page");

        SettingsController settingsController = loader.getController();
        settingsController.initData(customerId.getText());

        settingsWindow.show();
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
        Stage stage = (Stage) table.getScene().getWindow();
        stage.close();
    }
}
