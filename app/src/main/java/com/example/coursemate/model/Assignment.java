package com.example.coursemate.model;

import java.util.Date;

public class Assignment {
    private int id;
    private String title;
    private String description;
    private Date deadline;
    private int courseId;

    // Constructor đầy đủ
    public Assignment(int id, String title, String description, Date deadline, int courseId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.courseId = courseId;
    }

    // Constructor tối giản
    public Assignment(String title, String description, Date deadline, int courseId) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.courseId = courseId;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
