package com.example.shop;

public class Item {
    private int id;
    private String title;
    private double price;
    private String photoPath;

    // Constructor
    public Item(int id, String title, double price, String photoPath) {
        this.id = id;
        this.title = title;
        this.price = price; // Initialize author to an empty string or provide a parameter
        this.photoPath = photoPath; // Initialize description to an empty string or provide a parameter
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Setter for title
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for author
    public double getPrice() {
        return price;
    }

    // Setter for author
    public void setPrice(double price) {
        this.price = price;
    }

    // Getter for description
    public String getPhotoPath() {
        return photoPath;
    }
    // Setter for description
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}