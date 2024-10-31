package com.example.coursemate;

import java.util.Date;

public class Course {
    // Thuộc tính từ cả hai lớp
    private int id;                   // ID khóa học
    private int teacherId;             // ID giáo viên
    private String name;               // Tên khóa học
    private int iconResource;          // Icon khóa học
    private String description;        // Mô tả khóa học
    private int maxStudents;           // Số lượng sinh viên tối đa
    private String status;             // Trạng thái khóa học
    private Date startDate;            // Ngày bắt đầu
    private Date endDate;              // Ngày kết thúc
    private String price;              // Giá khóa học
    private String slot;               // Số lượng slot

    // Constructor đầy đủ
    public Course(int id, int teacherId, String name, int iconResource, String description, int maxStudents, String status, Date startDate, Date endDate, String price, String slot) {
        this.id = id;
        this.teacherId = teacherId;
        this.name = name;
        this.iconResource = iconResource;
        this.description = description;
        this.maxStudents = maxStudents;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.slot = slot;
    }

    // Constructor không có ID (tùy chọn, nếu cần)
    public Course(int teacherId, String name, String description, int maxStudents, String status, Date startDate, Date endDate) {
        this.teacherId = teacherId;
        this.name = name;
        this.description = description;
        this.maxStudents = maxStudents;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Constructor tối giản (phục vụ trường hợp chỉ có tên và iconResource)
    public Course(String name, int iconResource, String description, String price, String slot) {
        this.name = name;
        this.iconResource = iconResource;
        this.description = description;
        this.price = price;
        this.slot = slot;
    }

    // Getters và Setters cho tất cả các thuộc tính
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

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }
    public String getTitle() {
        return price;
    }
}
