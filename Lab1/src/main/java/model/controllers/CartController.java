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
import java.util.List;
import java.util.ResourceBundle;

public class CartController implements Initializable {
    public Label status;
    public Label totalPrice;
    public TableView<ProductTableParameters> table;
    public TableColumn<ProductTableParameters, String> name;
    public TableColumn<ProductTableParameters, String> type;
    public TableColumn<ProductTableParameters, String> quantity;
    public TableColumn<ProductTableParameters, String> warranty;
    public TableColumn<ProductTableParameters, String> price;
    private ObservableList<ProductTableParameters> data = FXCollections.observableArrayList();

    public Label customerId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateCustomer hibernateCustomer = new HibernateCustomer(entityManagerFactory);
    HibernateProduct hibernateProduct = new HibernateProduct(entityManagerFactory);
    HibernateCart hibernateCart = new HibernateCart(entityManagerFactory);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }
    public void initData(String id) {
        customerId.setText(id);
        loadData(null);
    }
    public void initData(String id, Cart cart) {
        customerId.setText(id);
        loadData(cart);
    }

    public void loadData(Cart cart) {
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
        if (cart == null) return;
        if (cart.getProducts() == null) return;

        status.setText(cart.getStatus());
        double total = 0;

        List<Product> products = cart.getProducts();
        int n = products.size();
        while (n > 0) {
            int quant = 1;
            Product product = products.get(0);
            products.remove(0);
            n--;
            for (int j = 0; j < n; j++) {
                if (product.getName().equals(products.get(j).getName())
                        && product.getType().equals(products.get(j).getType())) {
                    quant++;
                    products.remove(j);
                    n--;
                    j--;
                }
            }
            total += product.getPrice() * quant;

            ProductTableParameters productTableParameters = new ProductTableParameters(
                    Integer.toString(product.getId()),
                    product.getName(),
                    product.getType(),
                    Integer.toString(quant),
                    Integer.toString(product.getWarrantyYears()),
                    Double.toString(Math.round(product.getPrice() * quant * 100.0) / 100.0));

            data.add(productTableParameters);
        }
        table.setItems(data);

        total = Math.round(total * 100.0) / 100.0;
        cart.setPrice(total);
        cart.updateCart(hibernateCart);
        totalPrice.setText(Double.toString(total));
    }

    public Cart getLastCart(Customer customer) {
        if (customer.getCartList() == null) return null;
        if (customer.getCartList().isEmpty()) return null;
        return customer.getCartList().getLast();
    }

    public void removeItem(ActionEvent actionEvent) {
        if (!status.getText().equals("open")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- items can be removed only when order status is: open");
            alert.show();
            return;
        }
        ProductTableParameters p = table.getSelectionModel().getSelectedItem();
        if (p == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- no item selected");
            alert.show();
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

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Order canceled successfully");
            alert.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- order can be canceled only when order status is: open");
            alert.show();
        }
    }

    public void checkout(ActionEvent actionEvent) {
        if (!status.getText().equals("open")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- order can be purchased only when order status is: open");
            alert.show();
            return;
        }
        if (totalPrice.getText().equals("0.0")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- no items in this order");
            alert.show();
            return;
        }
        Customer customer = hibernateCustomer.getCustomer(customerId.getText());
        if (customer.getCardNo().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- no card number found\nPlease add card number in Account -> Settings");
            alert.show();
            return;
        }
        Cart cart = getLastCart(customer);
        cart.setStatus("Paid");
        cart.updateCart(hibernateCart);
        loadData(cart);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Order purchased successfully");
        alert.show();
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
}
