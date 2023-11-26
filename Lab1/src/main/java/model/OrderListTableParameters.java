package model;

import javafx.beans.property.SimpleStringProperty;

public class OrderListTableParameters {
    private SimpleStringProperty order_id = new SimpleStringProperty();
    private SimpleStringProperty price = new SimpleStringProperty();
    private SimpleStringProperty status = new SimpleStringProperty();

    public OrderListTableParameters() {
    }

    public OrderListTableParameters(SimpleStringProperty order_id, SimpleStringProperty price, SimpleStringProperty status) {
        this.order_id = order_id;
        this.price = price;
        this.status = status;
    }

    public OrderListTableParameters(String order_id, String price, String status) {
        this.order_id = new SimpleStringProperty(order_id);
        this.price = new SimpleStringProperty(price);
        this.status = new SimpleStringProperty(status);
    }

    public String getOrder_id() {
        return order_id.get();
    }

    public SimpleStringProperty order_idProperty() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id.set(order_id);
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
