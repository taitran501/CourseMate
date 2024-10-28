package com.example.courseapp.domain;

public class coursedomain {

    private String title;
    private double price;
    private int picpath;

    public coursedomain(double price, String title, int picpath) {
        this.price = price;
        this.title = title;
        this.picpath = picpath;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPicpath() {
        return picpath;
    }

    public void setPicpath(int picpath) {
        this.picpath = picpath;
    }

}
