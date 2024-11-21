package com.example.carrentalapp;

public class CarData {
    private String model;
    private String number;
    private String price;
    private String fuel;

    // Required default constructor for Firebase
    public CarData() {
    }

    public CarData(String model, String number, String price, String fuel) {
        this.model = model;
        this.number = number;
        this.price = price;
        this.fuel = fuel;
    }

    // Getter methods
    public String getModel() {
        return model;
    }

    public String getNumber() {
        return number;
    }

    public String getPrice() {
        return price;
    }

    public String getFuel() {
        return fuel;
    }

    // Setter methods
    public void setModel(String model) {
        this.model = model;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }
}
