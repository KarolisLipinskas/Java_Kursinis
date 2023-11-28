package model.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import model.HibernateCustomer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPromptController implements Initializable {
    public TextField loginName;
    public PasswordField loginPass;

    public Label customerId;
    public Label next;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateCustomer hibernateCustomer = new HibernateCustomer(entityManagerFactory);
    SettingsController settingsController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void initData(String id, String path, SettingsController settingsController) {
        customerId.setText(id);
        next.setText(path);
        this.settingsController = settingsController;
    }

    public void login(ActionEvent actionEvent) throws IOException {
        Customer customer = hibernateCustomer.getCustomer(customerId.getText());
        if (customer.getUsername().equals(loginName.getText()) && customer.getPassword().equals(loginPass.getText())) {
            openWindow();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Wrong username or password");
            alert.show();
        }
    }

    public void openWindow() throws IOException {
        if (next.getText().isEmpty()) {
            settingsController.deleteAccount();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(next.getText()));
        Stage window = getStage(loader, "Change");

        switch (next.getText()) {
            case "../fxml/changeGmail.fxml":
                ChangeGmailController changeGmailController = loader.getController();
                changeGmailController.initData(settingsController);
                break;
            case "../fxml/changeCardNo.fxml":
                ChangeCardNoController changeCardNoController = loader.getController();
                changeCardNoController.initData(settingsController);
                break;
            case "../fxml/changeNickname.fxml":
                ChangeNicknameController changeNicknameController = loader.getController();
                changeNicknameController.initData(settingsController);
                break;
            case "../fxml/changePassword.fxml":
                ChangePasswordController changePasswordController = loader.getController();
                changePasswordController.initData(settingsController);
                break;
            default: return;
        }

        window.show();
        settingsController.setTempWindow(window);
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
        Stage stage = (Stage) loginName.getScene().getWindow();
        stage.close();
    }
}
