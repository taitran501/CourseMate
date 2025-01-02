package com.example.coursemate.model;

public class Course {
    // Thuộc tính của lớp Course
    private String id;                    // ID khóa học
    private int teacherId;             // ID giáo viên
    private String name;               // Tên khóa học
    private int iconResource;          // Icon khóa học
    private String description;        // Mô tả khóa học
    private int maxStudents;           // Số lượng sinh viên tối đa
    private String status;             // Trạng thái khóa học
    private String startDate;          // Định dạng "yyyy-MM-dd"
    private String endDate;            // Định dạng "yyyy-MM-dd"
    private String price;              // Giá khóa học
    private String startTime;
    private String endTime;


    public Course(String id, String name, String status, String startDate, String endDate) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Constructor đầy đủ (khớp với tham số truyền từ DatabaseHelper)
    public Course(String id, int teacherId, String name, int iconResource, String description, int maxStudents, String status, String startDate, String endDate, String price) {
        this.id = id;
        this.teacherId = teacherId;
        this.name = name;
        this.iconResource = iconResource;
        this.description = description;
        this.maxStudents = maxStudents;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Course(String id, String teacherId, String name, int iconResource, String description, int maxStudents, String status, String startDate, String endDate) {
        this.id = id;
        this.name = name;
        this.iconResource = iconResource;
        this.description = description;
        this.maxStudents = maxStudents;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Constructor tối giản (phục vụ trường hợp chỉ có tên và iconResource)
    public Course(String name, int iconResource, String description, String price, int maxStudents) {
        this.name = name;
        this.iconResource = iconResource;
        this.description = description;
        this.price = price;
        this.maxStudents = maxStudents;
    }

    public Course(String id, String name, String description, String startDate, String endDate, String startTime, String endTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Constructor cho TeacherSchedule
    public Course(String id, String name, String description, int maxStudents, String status, String startDate, String endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxStudents = maxStudents;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    // Constructor cho search course trong student dashboard
    public Course(String courseName, String courseDescription) {
        this.name = courseName;
        this.description = courseDescription;
    }

    // Constructor cho schedule của student
    public Course(String courseName, String courseDescription, String startTime, String endTime) {
        this.name = courseName;
        this.description = courseDescription;
        this.startDate = startTime;
        this.endDate = endTime;
    }

    public Course(String courseName, String courseDescription, String startTime, String endTime, String startDate, String endDate) {
        this.name = courseName;
        this.description = courseDescription;
        this.startDate = startTime;
        this.endDate = endTime;
    }

    // Constructor cho mainpage course adapter
    public Course(String id, int teacherId, String name, int icCourseIcon, String description, int i, String startDate, String endDate, String price) {
        this.id=id;
        this.teacherId = teacherId;
        this.name = name;
        this.iconResource = icCourseIcon;
        this.description = description;
    }

    public Course(int courseId, int teacherId, String name, int icCoursePlaceholder, String description, int maxStudents, String status, String formattedStartDate, String formattedEndDate, String price) {
        this.teacherId = teacherId;
        this.name = name;
        this.iconResource = icCoursePlaceholder;
        this.description = description;
        this.maxStudents = maxStudents;
    }

    public Course(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters và Setters cho tất cả các thuộc tính
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getStartTime() {
        return startDate;
    }
    public String getEndTime() {
        return endDate;
    }
}
