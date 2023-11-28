package model.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class MainController implements Initializable {
    public TextField priceMin;
    public TextField priceMax;
    public TextField quantityField;
    public ComboBox<String> types;
    public TableView<ProductTableParameters> table;
    public TableColumn<ProductTableParameters, String> name;
    public TableColumn<ProductTableParameters, String> type;
    public TableColumn<ProductTableParameters, String> quantity;
    public TableColumn<ProductTableParameters, String> warranty;
    public TableColumn<ProductTableParameters, String> price;
    private ObservableList<ProductTableParameters> data = FXCollections.observableArrayList();

    public Label customerId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateProduct hibernateProduct = new HibernateProduct(entityManagerFactory);
    HibernateCustomer hibernateCustomer = new HibernateCustomer(entityManagerFactory);
    HibernateCart hibernateCart = new HibernateCart(entityManagerFactory);

    public void createNewProduct() { //perkelti i manager screen
        Product product = new Product("Bike v1", "Bike", 70.99, 3);
        hibernateProduct.create(product);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        types.getItems().addAll("All", "Bike", "Brakes", "Fork", "Frame", "Handlebars", "Pedals", "Shock", "Wheels");
        loadTable("All", 0.0, 999999.99);
    }
    public void initData(String id) {
        customerId.setText(id);
    }

    public void loadTable(String productType, double min, double max) {
        table.getItems().clear();
        name.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("name"));
        type.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("type"));
        quantity.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("quantity"));
        warranty.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("warranty"));
        price.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("price"));

        for (Product product : hibernateProduct.getAllProducts()) {
            if ((productType.equals("All") || productType.equals(product.getType()))
                    && product.getPrice() >= min && product.getPrice() <= max
                    && product.getCart() == null) {

                ProductTableParameters productTableParameters = new ProductTableParameters(
                        Integer.toString(product.getId()),
                        product.getName(),
                        product.getType(),
                        "inf",
                        Integer.toString(product.getWarrantyYears()),
                        Double.toString(product.getPrice()));

                data.add(productTableParameters);
            }
        }
        table.setItems(data);
    }

    public void addToCart(ActionEvent actionEvent) {
        if (quantityField.getText().isEmpty() || !Pattern.matches("[0-9]*", quantityField.getText()) || quantityField.getText().equals("0")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- wrong quantity input");
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
        int quant = Integer.parseInt(quantityField.getText());

        Customer customer = hibernateCustomer.getCustomer(customerId.getText());
        Cart cart = getOpenCart(customer);
        if (cart == null) {
            Cart newCart = new Cart("open", customer);
            hibernateCart.create(newCart);
        }

        customer = hibernateCustomer.getCustomer(customerId.getText());
        cart = getOpenCart(customer);

        for (int i = 0; i < quant; i++) {
            cart.addProduct(product);
            //hibernateCart.update(cart);
            hibernateProduct.create(new Product(product, cart));
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Item/s added to cart");
        alert.show();
    }
    public Cart getOpenCart(Customer customer) {
        if (customer.getCartList() == null) return null;
        else {
            for (Cart cart : customer.getCartList()) {
                if(cart.getStatus().equals("open")) return cart;
            }
        }
        return null;
    }

    public void filter(ActionEvent actionEvent) {
        boolean check = true;
        String alertText = "";
        if (!Pattern.matches("[0-9]*\\.[0-9]*", priceMin.getText()) && !Pattern.matches("[0-9]*", priceMin.getText())) {
            check = false;
            alertText += "- wrong min input\n";
        }
        if (!Pattern.matches("[0-9]*\\.[0-9]*", priceMax.getText()) && !Pattern.matches("[0-9]*", priceMax.getText())) {
            check = false;
            alertText += "- wrong max input\n";
        }
        if (!check){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(alertText);
            alert.show();
            return;
        }

        double min = 0.0;
        double max = 999999.99;
        if (!priceMin.getText().isEmpty()) min = Double.parseDouble(priceMin.getText());
        if (!priceMax.getText().isEmpty()) max = Double.parseDouble(priceMax.getText());

        loadTable(types.getValue(), min, max);
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
        Stage stage = (Stage) priceMax.getScene().getWindow();
        stage.close();
    }
}
