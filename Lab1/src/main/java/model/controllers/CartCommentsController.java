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
import model.entities.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class CartCommentsController implements Initializable {
    public TableView<CartCommentsParameters> table;
    public TableColumn<CartCommentsParameters, String> orderNo;
    public TableColumn<CartCommentsParameters, String> replying;
    public TableColumn<CartCommentsParameters, String> text;
    private ObservableList<CartCommentsParameters> data = FXCollections.observableArrayList();

    public TextArea comment;

    public Label userId;
    public Label cartId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateCustomer hibernateCustomer = new HibernateCustomer(entityManagerFactory);
    HibernateCart hibernateCart = new HibernateCart(entityManagerFactory);
    HibernateManager hibernateManager = new HibernateManager(entityManagerFactory);
    HibernateCartComments hibernateCartComments = new HibernateCartComments(entityManagerFactory);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }
    public void initData(String userId, String cartId) {
        this.userId.setText(userId);
        this.cartId.setText(cartId);
        loadData();
    }

    public void loadData() {
        table.getItems().clear();
        orderNo.setCellValueFactory(new PropertyValueFactory<CartCommentsParameters, String>("comment_id"));
        replying.setCellValueFactory(new PropertyValueFactory<CartCommentsParameters, String>("replying"));
        text.setCellValueFactory(new PropertyValueFactory<CartCommentsParameters, String>("text"));

        for (CartComment cartComment : hibernateCartComments.getAllCartComments()) {
            String reply = "";
            if (cartComment.getCart().getId() == Integer.parseInt(cartId.getText())) {
                if (cartComment.getCartComment() == null) reply += "First comment";
                else reply += "Comment: " + cartComment.getCartComment().getId();

                CartCommentsParameters cartCommentsParameters = new CartCommentsParameters(
                        Integer.toString(cartComment.getId()),
                        reply,
                        cartComment.getText());
                data.add(cartCommentsParameters);
            }
        }
        table.setItems(data);
    }

    public void newComment() {
        if (comment.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No comment");
            alert.show();
            return;
        }
        Cart cart = hibernateCart.getCart(cartId.getText());
        CartComment lastComment = hibernateCartComments.getLastComment(Integer.parseInt(cartId.getText()));
        CartComment cartComment;
        cartComment = new CartComment(comment.getText(), cart, lastComment);

        hibernateCartComments.create(cartComment);
        loadData();
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
