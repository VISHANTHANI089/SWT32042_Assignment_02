package com.example.inventory_management;

public class ProductModel  {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String vendor;

    public ProductModel(int id, String name, double price, int quantity, String vendor) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.vendor = vendor;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getVendor() {
        return vendor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}
