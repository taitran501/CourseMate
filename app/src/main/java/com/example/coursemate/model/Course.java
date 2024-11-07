package com.example.coursemate.model;

public class Course {
    // Thuộc tính của lớp Course
    private int id;                    // ID khóa học
    private int teacherId;             // ID giáo viên
    private String name;               // Tên khóa học
    private int iconResource;          // Icon khóa học
    private String description;        // Mô tả khóa học
    private int maxStudents;           // Số lượng sinh viên tối đa
    private String status;             // Trạng thái khóa học
    private String startDate;          // Định dạng "yyyy-MM-dd"
    private String endDate;            // Định dạng "yyyy-MM-dd"
    private String price;              // Giá khóa học

    // Constructor đầy đủ (khớp với tham số truyền từ DatabaseHelper)
    public Course(int id, int teacherId, String name, int iconResource, String description, int maxStudents, String status, String startDate, String endDate, String price) {
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
    }

    // Constructor tối giản (phục vụ trường hợp chỉ có tên và iconResource)
    public Course(String name, int iconResource, String description, String price, int maxStudents) {
        this.name = name;
        this.iconResource = iconResource;
        this.description = description;
        this.price = price;
        this.maxStudents = maxStudents;
    }

    // Constructor phục vụ cho TeacherSchedule
    public Course(int id, String name, String description, int maxStudents, String status, String startDate, String endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxStudents = maxStudents;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Constructor đầy đủ (khớp với tham số truyền từ DatabaseHelper)
    public Course(int id, int teacherId, String name, String description, int maxStudents, String status, String startDate, String endDate, String price) {
        this.id = id;
        this.teacherId = teacherId;
        this.name = name;
        this.description = description;
        this.maxStudents = maxStudents;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
