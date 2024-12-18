package com.example.coursemate.model;

import java.util.Date;

public class CourseRegistration {
    private int id;                 // ID của việc đăng ký
    private int studentId;          // ID của sinh viên
    private int courseId;           // ID của khóa học
    private String paymentStatus;   // Trạng thái thanh toán: 'unpaid', 'paid', 'rejected'
    private double amount;          // Số tiền thanh toán
    private Date registrationDate;  // Ngày đăng ký

    // Constructor đầy đủ
    public CourseRegistration(int id, int studentId, int courseId, String paymentStatus, double amount, Date registrationDate) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.registrationDate = registrationDate;
    }

    // Constructor không có ID (dùng khi tạo mới bản ghi)
    public CourseRegistration(int studentId, int courseId, String paymentStatus, double amount, Date registrationDate) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.registrationDate = registrationDate;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    // Override toString (tùy chọn)
    @Override
    public String toString() {
        return "CourseRegistration{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", amount=" + amount +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
