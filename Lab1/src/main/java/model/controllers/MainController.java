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
import model.entities.Cart;
import model.entities.Customer;
import model.entities.Product;

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
    public ObservableList<ProductTableParameters> data = FXCollections.observableArrayList();

    public Label customerId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    public HibernateProduct hibernateProduct = new HibernateProduct(entityManagerFactory);
    public HibernateCustomer hibernateCustomer = new HibernateCustomer(entityManagerFactory);
    public HibernateCart hibernateCart = new HibernateCart(entityManagerFactory);

    public void createNewProduct() { //perkelti i manager screen
        Product product = new Product("Bike v1", "Bike", 70.99, 3);
        hibernateProduct.create(product);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTypesComboBox(); // Užpildomas kategorijų sąrašas
        initializeTableColumns(); // Nustatomi lentelės stulpeliai
        loadTable("All", 0.0, 999999.99); // Užkraunami visi produktai
    }

    private void initializeTypesComboBox() {
        types.getItems().addAll("All", "Bike", "Brakes", "Fork", "Frame", "Handlebars", "Pedals", "Shock", "Wheels");
    }
    public void initData(String id) {
        customerId.setText(id);
    }

    private void initializeTableColumns() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        warranty.setCellValueFactory(new PropertyValueFactory<>("warranty"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void loadTable(String productType, double min, double max) {
        table.getItems().clear(); // Išvaloma lentelė
        data.setAll(fetchFilteredProducts(productType, min, max)); // Užpildoma naujais duomenimis
        table.setItems(data);
    }

    private ObservableList<ProductTableParameters> fetchFilteredProducts(String productType, double min, double max) {
        ObservableList<ProductTableParameters> filteredData = FXCollections.observableArrayList();

        for (Product product : hibernateProduct.getAllProducts()) {
            if (isValidProduct(product, productType, min, max)) {
                filteredData.add(mapProductToTableParameters(product));
            }
        }
        return filteredData;
    }

    private boolean isValidProduct(Product product, String productType, double min, double max) {
        return (productType.equals("All") || productType.equals(product.getType())) &&
                product.getPrice() >= min &&
                product.getPrice() <= max &&
                product.getCart() == null;
    }

    private ProductTableParameters mapProductToTableParameters(Product product) {
        return new ProductTableParameters(
                Integer.toString(product.getId()),
                product.getName(),
                product.getType(),
                "inf",
                Integer.toString(product.getWarrantyYears()),
                Double.toString(product.getPrice())
        );
    }

    public void addToCart(ActionEvent actionEvent) {
        if (!isValidQuantityInput()) {
            showAlert(Alert.AlertType.ERROR, "- wrong quantity input");
            return;
        }

        ProductTableParameters selectedProduct = table.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.ERROR, "- no item selected");
            return;
        }

        try {
            processAddToCart(selectedProduct);
            showAlert(Alert.AlertType.INFORMATION, "Item/s added to cart");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error adding items to cart: " + e.getMessage());
        }
    }

    private boolean isValidQuantityInput() {
        String quantityText = quantityField.getText();
        return !quantityText.isEmpty() && Pattern.matches("[0-9]+", quantityText) && !quantityText.equals("0");
    }

    private void processAddToCart(ProductTableParameters selectedProduct) {
        Product product = hibernateProduct.getProduct(Integer.parseInt(selectedProduct.getId()));
        int quantity = Integer.parseInt(quantityField.getText());

        Cart cart = ensureOpenCart();
        addProductsToCart(cart, product, quantity);
    }

    private Cart ensureOpenCart() {
        Customer customer = hibernateCustomer.getCustomer(customerId.getText());
        Cart cart = getOpenCart(customer);
        if (cart == null) {
            cart = new Cart("open", customer);
            hibernateCart.create(cart);
        }
        return getOpenCart(hibernateCustomer.getCustomer(customerId.getText()));
    }

    private void addProductsToCart(Cart cart, Product product, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cart.addProduct(product);
            hibernateProduct.create(new Product(product, cart)); // Sukuriamas produkto „klonas“
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
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
