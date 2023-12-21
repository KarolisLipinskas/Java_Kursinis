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
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class ReportController implements Initializable {
    public Label orderAmount;
    public Label orderPrice;
    public Label oOrderAmount;
    public Label oOrderPrice;
    public Label pOrderAmount;
    public Label pOrderPrice;

    public Label prOrderAmount;
    public Label paOrderAmount;
    public Label iOrderAmount;

    public Label uOrderAmount;
    public Label uOrderPrice;
    public Label cOrderAmount;
    public Label cOrderPrice;

    public Label coOrderAmount;
    public Label caOrderAmount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }
    public void initData(List<Cart> cartList) {
        loadData(cartList);
    }

    public void loadData(List<Cart> cartList) {
        int oA = 0;
        double oP = 0.0;
        int oOA = 0;
        double oOP = 0.0;
        int pOA = 0;
        double pOP = 0.0;

        int prOA = 0;
        int paOA = 0;
        int iOA = 0;

        int uOA = 0;
        double uOP = 0.0;
        int cOA = 0;
        double cOP = 0.0;

        int coOA = 0;
        int caOA = 0;

        for (Cart cart : cartList) {
            oA++;
            oP += cart.getPrice();

            if (cart.getStatus().equals("open")) { oOA++; oOP += cart.getPrice(); }
            if (cart.getStatus().equals("Paid")) { pOA++; pOP += cart.getPrice(); }
            if (cart.getStatus().equals("Processing")) prOA++;
            if (cart.getStatus().equals("Packaged")) paOA++;
            if (cart.getStatus().equals("In Transit")) iOA++;
            if (cart.getManager() == null && cart.getCreationDate().isBefore(LocalDateTime.now().minusDays(1))) { uOA++; uOP += cart.getPrice(); }
            if (cart.getManager() != null) { cOA++; cOP += cart.getPrice(); }
            if (cart.getStatus().equals("Completed")) coOA++;
            if (cart.getStatus().equals("Canceled")) caOA++;
        }

        orderAmount.setText(Integer.toString(oA));
        orderPrice.setText(Double.toString(oP));
        oOrderAmount.setText(Integer.toString(oOA));
        oOrderPrice.setText(Double.toString(oOP));
        pOrderAmount.setText(Integer.toString(pOA));
        pOrderPrice.setText(Double.toString(pOP));

        prOrderAmount.setText(Integer.toString(prOA));
        paOrderAmount.setText(Integer.toString(paOA));
        iOrderAmount.setText(Integer.toString(iOA));

        uOrderAmount.setText(Integer.toString(uOA));
        uOrderPrice.setText(Double.toString(uOP));
        cOrderAmount.setText(Integer.toString(cOA));
        cOrderPrice.setText(Double.toString(cOP));

        coOrderAmount.setText(Integer.toString(coOA));
        caOrderAmount.setText(Integer.toString(caOA));
    }
}
