package model;

import javafx.beans.property.SimpleStringProperty;

public class ProductTableParameters {
    private SimpleStringProperty id = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty type = new SimpleStringProperty();
    private SimpleStringProperty quantity = new SimpleStringProperty();
    private SimpleStringProperty warranty = new SimpleStringProperty();
    private SimpleStringProperty price = new SimpleStringProperty();

    public ProductTableParameters() {
    }

    public ProductTableParameters(SimpleStringProperty id, SimpleStringProperty name, SimpleStringProperty type, SimpleStringProperty quantity, SimpleStringProperty warranty, SimpleStringProperty price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.warranty = warranty;
        this.price = price;
    }

    public ProductTableParameters(String id, String name, String type, String quantity, String warranty, String price) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.quantity = new SimpleStringProperty(quantity);
        this.warranty = new SimpleStringProperty(warranty);
        this.price = new SimpleStringProperty(price);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getQuantity() {
        return quantity.get();
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public String getWarranty() {
        return warranty.get();
    }

    public SimpleStringProperty warrantyProperty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty.set(warranty);
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
}
