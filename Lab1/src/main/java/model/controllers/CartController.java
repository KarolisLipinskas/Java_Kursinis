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
import model.entities.Cart;
import model.entities.Customer;
import model.entities.Product;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Console;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static model.Calculations.CartCalculation.*;
import static start.Main.isTest;

public class CartController implements Initializable {
    public Label status;
    public Label totalPrice;
    public TableView<ProductTableParameters> table;
    public TableColumn<ProductTableParameters, String> name;
    public TableColumn<ProductTableParameters, String> type;
    public TableColumn<ProductTableParameters, String> quantity;
    public TableColumn<ProductTableParameters, String> warranty;
    public TableColumn<ProductTableParameters, String> price;
    public ObservableList<ProductTableParameters> data = FXCollections.observableArrayList();

    public Label customerId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateCustomer hibernateCustomer = new HibernateCustomer(entityManagerFactory);
    HibernateProduct hibernateProduct = new HibernateProduct(entityManagerFactory);
    HibernateCart hibernateCart = new HibernateCart(entityManagerFactory);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }
    public void initData(String id) {
        if(!isTest) customerId.setText(id);
        loadData(null);
    }
    public void initData(String id, Cart cart) {
        if(!isTest) customerId.setText(id);
        loadData(cart);
    }

    public void loadData(Cart cart) {
        if(!isTest) {
            table.getItems().clear();
            name.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("name"));
            type.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("type"));
            quantity.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("quantity"));
            warranty.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("warranty"));
            price.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("price"));

            if (cart == null) {
                Customer customer = hibernateCustomer.getCustomer(customerId.getText());
                cart = getLastCart(customer);
            }
        }
        if (cart == null) return;
        if (cart.getProducts() == null) return;

        if(!isTest) status.setText(cart.getStatus());

        List<Product> products = cart.getProducts();
        List<Double> productPrices = calculateItemPrices(new ArrayList<>(cart.getProducts()));
        List<Integer> productQuantities = calculateQuantities(new ArrayList<>(cart.getProducts()));
        int n = productPrices.size();
        for (int i = 0; i < n; i++) {
            Product product = products.get(i);

            ProductTableParameters productTableParameters = new ProductTableParameters(
                    Integer.toString(product.getId()),
                    product.getName(),
                    product.getType(),
                    Integer.toString(productQuantities.get(i)),
                    Integer.toString(product.getWarrantyYears()),
                    Double.toString(productPrices.get(i)));

            data.add(productTableParameters);
        }
        if(!isTest) table.setItems(data);

        double total = calculateTotal(new ArrayList<>(cart.getProducts()));
        cart.setPrice(total);
        if(!isTest) cart.updateCart(hibernateCart);
        if(!isTest) totalPrice.setText(Double.toString(total));
    }

    public Cart getLastCart(Customer customer) {
        if (customer.getCartList() == null) return null;
        if (customer.getCartList().isEmpty()) return null;
        return customer.getCartList().getLast();
    }

    public void removeItem(ActionEvent actionEvent) {
        if (!status.getText().equals("open")) {
            showAlert("- items can be removed only when order status is: open", Alert.AlertType.ERROR);
            return;
        }
        ProductTableParameters p = table.getSelectionModel().getSelectedItem();
        if (p == null) {
            showAlert("- no item selected", Alert.AlertType.ERROR);
            return;
        }
        Product product = hibernateProduct.getProduct(Integer.parseInt(p.getId()));
        product.removeProduct(hibernateProduct);
        loadData(null);
    }

    public void cancelOrder(ActionEvent actionEvent) {
        if (status.getText().equals("open")) {
            Customer customer = hibernateCustomer.getCustomer(customerId.getText());
            Cart cart = getLastCart(customer);
            cart.removeCart(hibernateCart, hibernateProduct);

            status.setText("-");
            totalPrice.setText("-");
            loadData(null);

            showAlert("Order canceled successfully", Alert.AlertType.INFORMATION);
        }
        else {
            showAlert("- order can be canceled only when order status is: open", Alert.AlertType.ERROR);
        }
    }

    public void checkout(ActionEvent actionEvent) {
        if (!status.getText().equals("open")) {
            showAlert("- order can be purchased only when order status is: open", Alert.AlertType.ERROR);
            return;
        }
        if (totalPrice.getText().equals("0.0")) {
            showAlert("- no items in this order", Alert.AlertType.ERROR);
            return;
        }
        Customer customer = hibernateCustomer.getCustomer(customerId.getText());
        if (customer.getCardNo().isEmpty()) {
            showAlert("- no card number found\nPlease add card number in Account -> Settings", Alert.AlertType.ERROR);
            return;
        }
        Cart cart = getLastCart(customer);
        cart.setStatus("Paid");
        cart.updateCart(hibernateCart);
        loadData(cart);

        showAlert("Order purchased successfully", Alert.AlertType.INFORMATION);
    }

    public void openMainWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/main.fxml"));
        Stage mainWindow = getStage(loader, "Main page");

        MainController mainController = loader.getController();
        mainController.initData(customerId.getText());

        mainWindow.show();
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
        Stage stage = (Stage) status.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }
}
