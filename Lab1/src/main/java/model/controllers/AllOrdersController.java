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
import model.HibernateCart;
import model.HibernateManager;
import model.OrderListTableParameters;
import model.entities.Cart;
import model.entities.Manager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AllOrdersController implements Initializable {
    public TableView<OrderListTableParameters> claimedOrder;
    public TableColumn<OrderListTableParameters, String> cO_id;
    public TableColumn<OrderListTableParameters, String> cO_price;
    public TableColumn<OrderListTableParameters, String> cO_status;
    public TableColumn<OrderListTableParameters, String> cO_createTime;
    public TableColumn<OrderListTableParameters, String> cO_updateTime;
    private ObservableList<OrderListTableParameters> cO_data = FXCollections.observableArrayList();

    public TableView<OrderListTableParameters> urgentOrders;
    public TableColumn<OrderListTableParameters, String> uO_id;
    public TableColumn<OrderListTableParameters, String> uO_price;
    public TableColumn<OrderListTableParameters, String> uO_status;
    public TableColumn<OrderListTableParameters, String> uO_createTime;
    public TableColumn<OrderListTableParameters, String> uO_updateTime;
    private ObservableList<OrderListTableParameters> uO_data = FXCollections.observableArrayList();

    public TableView<OrderListTableParameters> allOrders;
    public TableColumn<OrderListTableParameters, String> aO_id;
    public TableColumn<OrderListTableParameters, String> aO_price;
    public TableColumn<OrderListTableParameters, String> aO_status;
    public TableColumn<OrderListTableParameters, String> aO_createTime;
    public TableColumn<OrderListTableParameters, String> aO_updateTime;
    private ObservableList<OrderListTableParameters> aO_data = FXCollections.observableArrayList();

    public ComboBox<String> statuses;

    public CheckBox showOpen;
    public ComboBox<String> filterStatus;
    public TextField createdFrom;
    public TextField createdTo;
    public TextField updatedFrom;
    public TextField updatedTo;

    public Label managerId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kl_kursinis");
    HibernateCart hibernateCart = new HibernateCart(entityManagerFactory);
    HibernateManager hibernateManager = new HibernateManager(entityManagerFactory);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statuses.getItems().addAll("Paid", "Processing", "Packaged", "In Transit", "Completed", "Canceled");
        filterStatus.getItems().addAll("All", "Paid", "Processing", "Packaged", "In Transit", "Completed", "Canceled");
    }

    public void initData(String id) {
        managerId.setText(id);
        loadTables();
    }

    public void loadTables() {
        claimedOrder.getItems().clear();
        cO_id.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("order_id"));
        cO_price.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("price"));
        cO_status.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("status"));
        cO_createTime.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("createTime"));
        cO_updateTime.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("updateTime"));

        urgentOrders.getItems().clear();
        uO_id.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("order_id"));
        uO_price.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("price"));
        uO_status.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("status"));
        uO_createTime.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("createTime"));
        uO_updateTime.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("updateTime"));

        allOrders.getItems().clear();
        aO_id.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("order_id"));
        aO_price.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("price"));
        aO_status.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("status"));
        aO_createTime.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("createTime"));
        aO_updateTime.setCellValueFactory(new PropertyValueFactory<OrderListTableParameters, String>("updateTime"));

        for (Cart cart : hibernateCart.getAllCarts()) {
            if (cart.getManager() != null && cart.getManager().getId() == Integer.parseInt(managerId.getText())) {
                OrderListTableParameters orderListTableParameters = new OrderListTableParameters(
                        Integer.toString(cart.getId()),
                        Double.toString(cart.getPrice()),
                        cart.getStatus(),
                        cart.getCreationDate().toString(),
                        cart.getUpdateDate().toString());

                cO_data.add(orderListTableParameters);
            }
            if (cart.getCreationDate() == null || cart.getUpdateDate() == null) continue;
            if (cart.getManager() == null && cart.getUpdateDate().isBefore(LocalDateTime.now().minusDays(1))) {
                OrderListTableParameters orderListTableParameters = new OrderListTableParameters(
                        Integer.toString(cart.getId()),
                        Double.toString(cart.getPrice()),
                        cart.getStatus(),
                        cart.getCreationDate().toString(),
                        cart.getUpdateDate().toString());

                uO_data.add(orderListTableParameters);
            }
            else if (cart.getManager() == null && isFiltered(cart)) {
                OrderListTableParameters orderListTableParameters = new OrderListTableParameters(
                        Integer.toString(cart.getId()),
                        Double.toString(cart.getPrice()),
                        cart.getStatus(),
                        cart.getCreationDate().toString(),
                        cart.getCreationDate().toString());

                aO_data.add(orderListTableParameters);
            }
        }
        claimedOrder.setItems(cO_data);
        urgentOrders.setItems(uO_data);
        allOrders.setItems(aO_data);
    }

    private boolean isFiltered(Cart cart) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (!showOpen.isSelected()) {
            if (cart.getStatus().equals("open")) return false;
        }
        if (!filterStatus.getValue().equals("All")) {
            if (!cart.getStatus().equals(filterStatus.getValue())) return false;
        }
        if (!createdFrom.getText().isEmpty() && cart.getCreationDate().isBefore(LocalDateTime.parse(createdFrom.getText() + " 00:00:00", formatter))) return false;
        if (!createdTo.getText().isEmpty() && cart.getCreationDate().isAfter(LocalDateTime.parse(createdTo.getText() + " 23:59:59", formatter))) return false;
        if (!updatedFrom.getText().isEmpty() && cart.getUpdateDate().isBefore(LocalDateTime.parse(updatedFrom.getText() + " 00:00:00", formatter))) return false;
        if (!updatedTo.getText().isEmpty() && cart.getUpdateDate().isAfter(LocalDateTime.parse(updatedTo.getText() + " 23:59:59", formatter))) return false;

        return true;
    }

    public void selectOrder(ActionEvent actionEvent) {
        if (!checkInput()) return;

        OrderListTableParameters urgent = urgentOrders.getSelectionModel().getSelectedItem();
        OrderListTableParameters all = allOrders.getSelectionModel().getSelectedItem();
        System.out.println(urgent + " CIA " + all);
        if (urgent == null && all == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No order selected");
            alert.show();
            return;
        }
        else if (urgent != null && all != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select just 1 row");
            alert.show();
            loadTables();
            return;
        }
        else if (urgent != null) {
            Cart cart = hibernateCart.getCart(urgent.getOrder_id());
            Manager manager = hibernateManager.getManager(Integer.parseInt(managerId.getText()));
            cart.setManager(manager);
            hibernateCart.update(cart);
            loadTables();
        }
        else {
            Cart cart = hibernateCart.getCart(all.getOrder_id());
            Manager manager = hibernateManager.getManager(Integer.parseInt(managerId.getText()));
            cart.setManager(manager);
            hibernateCart.update(cart);
            loadTables();
        }
    }

    public void releaseOrder(ActionEvent actionEvent) {
        if (!checkInput()) return;

        OrderListTableParameters order = claimedOrder.getSelectionModel().getSelectedItem();
        if (order == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No order selected");
            alert.show();
            return;
        }

        Cart cart = hibernateCart.getCart(order.getOrder_id());
        cart.setManager(null);
        hibernateCart.update(cart);
        loadTables();
    }

    public void updateStatus(ActionEvent actionEvent) {
        if (!checkInput()) return;

        OrderListTableParameters order = claimedOrder.getSelectionModel().getSelectedItem();
        if (order == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No order selected");
            alert.show();
            return;
        }

        Cart cart = hibernateCart.getCart(order.getOrder_id());
        if (cart.getStatus().equals(statuses.getValue())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No changes");
            alert.show();
            return;
        }

        cart.setStatus(statuses.getValue());
        cart.setUpdateDate(LocalDateTime.now());
        hibernateCart.update(cart);
        loadTables();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Order updated");
        alert.show();
    }

    public void filter(ActionEvent actionEvent) {
        if (!checkInput()) return;
        loadTables();
    }

    public boolean checkInput() {
        boolean check = true;
        String alertText = "";
        if (!createdFrom.getText().isEmpty() && !Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", createdFrom.getText())) {
            check = false;
            alertText += "- wrong created from date\n";
        }
        if (!createdTo.getText().isEmpty() && !Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", createdTo.getText())) {
            check = false;
            alertText += "- wrong created to date\n";
        }
        if (!updatedFrom.getText().isEmpty() && !Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", updatedFrom.getText())) {
            check = false;
            alertText += "- wrong updated from date\n";
        }
        if (!updatedTo.getText().isEmpty() && !Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", updatedTo.getText())) {
            check = false;
            alertText += "- wrong updated to date\n";
        }

        if (!check) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(alertText);
            alert.show();
        }
        return check;
    }

    public void openProductsWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/manager.fxml"));
        Stage ordersWindow = getStage(loader, "Products page");

        ManagerController managerController = loader.getController();
        managerController.initData(managerId.getText());

        ordersWindow.show();
        closeStage();
    }

    public void openCartCommentsWindow(ActionEvent actionEvent) throws IOException {
        OrderListTableParameters order = claimedOrder.getSelectionModel().getSelectedItem();
        if (order == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No order selected");
            alert.show();
            return;
        }
        Cart cart = hibernateCart.getCart(order.getOrder_id());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/cartComments.fxml"));
        Stage promptWindow = getStage(loader, "Cart comments page");

        CartCommentsController cartCommentsController = loader.getController();
        cartCommentsController.initData(managerId.getId(), Integer.toString(cart.getId()));

        promptWindow.show();
    }

    public void openReportWindow(ActionEvent actionEvent) throws IOException {
        if (!checkInput()) return;

        List<Cart> cartList = new ArrayList<>();
        for (Cart cart : hibernateCart.getAllCarts()) {
            if (cart.getCreationDate() == null || cart.getUpdateDate() == null) continue;
            if (cart.getManager() == null && cart.getUpdateDate().isBefore(LocalDateTime.now().minusDays(1))) cartList.add(cart);
            else if (isFiltered(cart)) cartList.add(cart);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/report.fxml"));
        Stage promptWindow = getStage(loader, "More information page");

        ReportController reportController = loader.getController();
        reportController.initData(cartList);

        promptWindow.show();
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
        Stage stage = (Stage) createdFrom.getScene().getWindow();
        stage.close();
    }
}
