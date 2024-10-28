package com.example.courseapp.activity;

import java.util.Date;

public class Course {
    private int id;
    private int teacherId;
    private String name;
    private String description;
    private int maxStudents;
    private String status;
    private Date startDate;
    private Date endDate;

    // Constructors, Getters, and Setters
    public Course() { }

    public Course(int teacherId, String name, String description, int maxStudents, String status, Date startDate, Date endDate) {
        this.teacherId = teacherId;
        this.name = name;
        this.description = description;
        this.maxStudents = maxStudents;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
