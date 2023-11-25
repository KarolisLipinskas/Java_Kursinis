package model.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.HibernateCustomer;
import model.HibernateProduct;
import model.Product;
import model.ProductTableParameters;
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

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateProduct hibernateProduct = new HibernateProduct(entityManagerFactory);

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

    public void loadTable(String productType, double min, double max) {
        table.getItems().clear();
        name.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("name"));
        type.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("type"));
        quantity.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("quantity"));
        warranty.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("warranty"));
        price.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("price"));

        for (Product product : hibernateProduct.getAllProducts()) {
            if ((productType.equals("All") || productType.equals(product.getType()))
                    && product.getPrice() > min && product.getPrice() < max) {

                ProductTableParameters productTableParameters = new ProductTableParameters(
                        Integer.toString(product.getId()),
                        product.getName(),
                        product.getType(),
                        "1",
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
        System.out.println(product);
        System.out.println(quantityField.getText());
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
