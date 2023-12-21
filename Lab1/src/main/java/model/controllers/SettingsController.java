package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.HibernateCartComments;
import model.entities.Customer;
import model.HibernateCart;
import model.HibernateCustomer;
import model.HibernateProduct;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SettingsController implements Initializable {
    public TextField name;
    public TextField surname;
    public TextField birthdate;
    public Label gmail;
    public Label cardNo;
    public Label loginName;
    public Label loginPass;

    public Label card;
    public Label password;
    public Label customerId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateCustomer hibernateCustomer = new HibernateCustomer(entityManagerFactory);
    HibernateCart hibernateCart = new HibernateCart(entityManagerFactory);
    HibernateProduct hibernateProduct = new HibernateProduct(entityManagerFactory);
    HibernateCartComments hibernateCartComments = new HibernateCartComments(entityManagerFactory);

    List<Stage> tempWindows = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void initData(String id) {
        customerId.setText(id);
        loadData();
    }

    public void loadData() {
        Customer customer = hibernateCustomer.getCustomer(customerId.getText());
        name.setText(customer.getName());
        surname.setText(customer.getSurname());
        birthdate.setText(customer.getBirthDate().toString());
        gmail.setText(customer.getGmail());
        card.setText(customer.getCardNo());
        String card = "-";
        if(!customer.getCardNo().isEmpty()) card = "****-****-****-" + customer.getCardNo().substring(15);
        cardNo.setText(card);
        loginName.setText(customer.getUsername());
        password.setText(customer.getPassword());
        String pass = "";
        for (int i = 0; i < customer.getPassword().length(); i++) pass += "*";
        loginPass.setText(pass);
    }

    public void saveChanges(ActionEvent actionEvent) {
        if (!checkInput()) return;

        Customer customer = hibernateCustomer.getCustomer(customerId.getText());
        customer.setName(name.getText());
        customer.setSurname(surname.getText());
        customer.setBirthDate(LocalDate.parse(birthdate.getText()));
        customer.setGmail(gmail.getText());
        customer.setCardNo(card.getText());
        customer.setUsername(loginName.getText());
        customer.setPassword(password.getText());

        customer.updateCustomer(hibernateCustomer);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Account changes saved to database");
        alert.show();
    }

    public boolean checkInput() {
        boolean check = true;
        String alertText = "";
        if (name.getText().isEmpty()) { //name check
            check = false;
            alertText += "- no name\n";
        }

        if (surname.getText().isEmpty()) { //surname check
            check = false;
            alertText += "- no surname\n";
        }

        if (birthdate.getText().isEmpty()) {   //birthdate check
            check = false;
            alertText += "- no birthdate\n";
        }
        else if (!Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", birthdate.getText())) {
            check = false;
            alertText += "- wrong date\n";
        }

        if (!check) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(alertText);
            alert.show();
        }

        return check;
    }

    public void deleteAccount() throws IOException {
        Customer customer = hibernateCustomer.getCustomer(customerId.getText());
        customer.removeCustomer(hibernateCustomer, hibernateCart, hibernateProduct, hibernateCartComments);
        logout(null);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Account deleted successfully");
        alert.show();
    }

    public void loginToDelete(ActionEvent actionEvent) throws IOException {
        openPromptWindow("");
    }

    public void openChangeGmail(ActionEvent actionEvent) throws IOException {
        openPromptWindow("../fxml/changeGmail.fxml");
    }

    public void openChangeCardNo(ActionEvent actionEvent) throws IOException {
        openPromptWindow("../fxml/changeCardNo.fxml");
    }

    public void openChangeNickname(ActionEvent actionEvent) throws IOException {
        openPromptWindow("../fxml/changeNickname.fxml");
    }

    public void openChangePassword(ActionEvent actionEvent) throws IOException {
        openPromptWindow("../fxml/changePassword.fxml");
    }

    public void openPromptWindow(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/loginPrompt.fxml"));
        Stage promptWindow = getStage(loader, "Login prompt");

        LoginPromptController loginPromptController = loader.getController();
        loginPromptController.initData(customerId.getText(), path, this);

        promptWindow.show();
        tempWindows.add(promptWindow);
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
        if (!tempWindows.isEmpty()) {
            for (Stage window : tempWindows) {
                if (window.isShowing()) window.close();
            }
        }
        stage.close();
    }

    public void setTempWindow(Stage tempWindow) {
        this.tempWindows.add(tempWindow);
    }
}
