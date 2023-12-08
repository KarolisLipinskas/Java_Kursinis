package model.entities;

import java.util.List;

public class Warehouse {
    private String address;
    private List<Product> products;

    public Warehouse(String address, List<Product> products) {
        this.address = address;
        this.products = products;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
