package com.example.coursemate.model;

public class Classroom {
    private int id;
    private String name;
    private int chairNum;
    private int deskNum;

    // Constructor đầy đủ
    public Classroom(int id, String name, int chairNum, int deskNum) {
        this.id = id;
        this.name = name;
        this.chairNum = chairNum;
        this.deskNum = deskNum;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getChairNum() {
        return chairNum;
    }

    public int getDeskNum() {
        return deskNum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChairNum(int chairNum) {
        this.chairNum = chairNum;
    }

    public void setDeskNum(int deskNum) {
        this.deskNum = deskNum;
    }
}
