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
import model.HibernateProduct;
import model.entities.Product;
import model.ProductTableParameters;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ManagerController implements Initializable {
    public TextField newName;
    public TextField newWarranty;
    public TextField newPrice;
    public ComboBox<String> types;
    public TableView<ProductTableParameters> table;
    public TableColumn<ProductTableParameters, String> name;
    public TableColumn<ProductTableParameters, String> type;
    public TableColumn<ProductTableParameters, String> warranty;
    public TableColumn<ProductTableParameters, String> price;
    private ObservableList<ProductTableParameters> data = FXCollections.observableArrayList();

    public Label managerId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateProduct hibernateProduct = new HibernateProduct(entityManagerFactory);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("manager window");
        types.getItems().addAll("Bike", "Brakes", "Fork", "Frame", "Handlebars", "Pedals", "Shock", "Wheels");
        loadTable();
    }

    public void initData(String id) {
        managerId.setText(id);
    }

    public void loadTable() {
        table.getItems().clear();
        name.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("name"));
        type.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("type"));
        warranty.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("warranty"));
        price.setCellValueFactory(new PropertyValueFactory<ProductTableParameters, String>("price"));

        for (Product product : hibernateProduct.getAllProducts()) {
            if (product.getCart() == null) {

                ProductTableParameters productTableParameters = new ProductTableParameters(
                        Integer.toString(product.getId()),
                        product.getName(),
                        product.getType(),
                        null,
                        Integer.toString(product.getWarrantyYears()),
                        Double.toString(product.getPrice()));

                data.add(productTableParameters);
            }
        }
        table.setItems(data);
    }

    public void createNewProduct() {
        if(!checkInput()) return;
        Product product = new Product(newName.getText(), types.getValue(), Double.parseDouble(newPrice.getText()), Integer.parseInt(newWarranty.getText()));
        hibernateProduct.create(product);
        loadTable();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Added new product");
        alert.show();
    }

    public boolean checkInput() {
        boolean check = true;
        String alertText = "";
        if (newName.getText().isEmpty()) { //name check
            check = false;
            alertText += "- no name\n";
        }

        if (newWarranty.getText().isEmpty()) { //warranty check
            check = false;
            alertText += "- no warranty\n";
        }
        if (!Pattern.matches("[0-9]*", newWarranty.getText())) {
            check = false;
            alertText += "- wrong warranty input\n";
        }

        if (newPrice.getText().isEmpty()) { //price check
            check = false;
            alertText += "- no price\n";
        }
        if (!Pattern.matches("[0-9]*\\.[0-9]*", newPrice.getText()) && !Pattern.matches("[0-9]*", newPrice.getText())) {
            check = false;
            alertText += "- wrong price input\n";
        }

        if (!check) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(alertText);
            alert.show();
        }

        return check;
    }

    public void updateItem(ActionEvent actionEvent) {
        ProductTableParameters p = table.getSelectionModel().getSelectedItem();
        if (p == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- no item selected");
            alert.show();
            return;
        }

        boolean check = true;
        String alertText = "";
        if (!Pattern.matches("[0-9]*", newWarranty.getText())) {
            check = false;
            alertText += "- wrong warranty input\n";
        }
        if (!Pattern.matches("[0-9]*\\.[0-9]*", newPrice.getText()) && !Pattern.matches("[0-9]*", newPrice.getText())) {
            check = false;
            alertText += "- wrong price input\n";
        }
        if (!check) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(alertText);
            alert.show();
        }

        check = true;
        Product product = hibernateProduct.getProduct(Integer.parseInt(p.getId()));
        if (!newName.getText().isEmpty() && !newName.getText().equals(product.getName())) {
            check = false;
            product.setName(newName.getText());
        }
        if (!types.getValue().equals(product.getType())) {
            check = false;
            product.setType(types.getValue());
        }
        if (!newWarranty.getText().isEmpty() && !newWarranty.getText().equals(Integer.toString(product.getWarrantyYears()))) {
            check = false;
            product.setWarrantyYears(Integer.parseInt(newWarranty.getText()));
        }
        if (!newPrice.getText().isEmpty() && !newPrice.getText().equals(Double.toString(product.getPrice()))) {
            check = false;
            product.setPrice(Double.parseDouble(newPrice.getText()));
        }
        if (check) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- no changes");
            alert.show();
            return;
        }

        product.updateProduct(hibernateProduct);
        loadTable();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Item updated");
        alert.show();
    }

    public void deleteItem(ActionEvent actionEvent) {
        ProductTableParameters p = table.getSelectionModel().getSelectedItem();
        if (p == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("- no item selected");
            alert.show();
            return;
        }
        Product product = hibernateProduct.getProduct(Integer.parseInt(p.getId()));
        product.removeProduct(hibernateProduct);
        loadTable();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Item deleted");
        alert.show();
    }

    public void openOrdersWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/allOrdersManager.fxml"));
        Stage ordersWindow = getStage(loader, "Orders page");

        AllOrdersController allOrdersController = loader.getController();
        allOrdersController.initData(managerId.getText());

        ordersWindow.show();
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
        Stage stage = (Stage) newName.getScene().getWindow();
        stage.close();
    }
}
