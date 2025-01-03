package com.example.coursemate.model;

public class Notification {
    private int id;
    private String message;
    private int courseId;
    private String status; // 'new' or 'checked'
    private String createdAt; // Lưu dưới dạng chuỗi yyyy-MM-dd

    // Constructor đầy đủ
    public Notification(int id, String message, int courseId, String status, String createdAt) {
        this.id = id;
        this.message = message;
        this.courseId = courseId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Constructor không có id
    public Notification(String message, int courseId, String status, String createdAt) {
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

    public String getCreatedAt() {
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

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
