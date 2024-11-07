package com.example.coursemate.model;

public class UserNotification {
    private int id;
    private int userId;
    private int notiId;

    // Constructor đầy đủ
    public UserNotification(int id, int userId, int notiId) {
        this.id = id;
        this.userId = userId;
        this.notiId = notiId;
    }

    // Constructor không có id (sử dụng khi thêm mới)
    public UserNotification(int userId, int notiId) {
        this.userId = userId;
        this.notiId = notiId;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getNotiId() {
        return notiId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setNotiId(int notiId) {
        this.notiId = notiId;
    }
}
