package model.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import sample.TableParameters;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
        System.out.println("Main page");
    }
    public void initData(String id) {
        customerId.setText(id);
        System.out.println(customerId);
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
                    && product.getPrice() > min && product.getPrice() < max
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
        ProductTableParameters p = table.getSelectionModel().getSelectedItem();
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
        System.out.println(product);
        System.out.println(quantityField.getText());

        //-----
        cart = getOpenCart(customer);
        if (cart != null) {
            for (Product product1 : cart.getProducts()) {
                System.out.println(product1);
            }
        } else System.out.println("NULL");
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
        double min = 0.0;
        double max = 999999.99;
        if (!priceMin.getText().isEmpty()) min = Double.parseDouble(priceMin.getText());
        if (!priceMax.getText().isEmpty()) max = Double.parseDouble(priceMax.getText());

        loadTable(types.getValue(), min, max);
        System.out.println(types.getValue());
        System.out.println(priceMin.getText() + " " + priceMax.getText());
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
