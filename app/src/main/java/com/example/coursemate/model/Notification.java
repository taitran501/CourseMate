package com.example.coursemate.model;

import java.util.Date;

public class Notification {
    private int id;
    private String message;
    private int courseId;
    private String status; // 'new' or 'checked'
    private Date createdAt;

    // Constructor đầy đủ
    public Notification(int id, String message, int courseId, String status, Date createdAt) {
        this.id = id;
        this.message = message;
        this.courseId = courseId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Constructor không có id (sử dụng khi thêm mới)
    public Notification(String message, int courseId, String status, Date createdAt) {
        this.message = message;
        this.courseId = courseId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
