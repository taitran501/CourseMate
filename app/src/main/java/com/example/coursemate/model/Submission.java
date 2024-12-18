package com.example.coursemate.model;

import java.util.Date;

public class Submission {
    private int id;                 // ID của Submission
    private String content;         // Nội dung bài nộp
    private Date submissionDate;    // Ngày nộp bài
    private double grade;           // Điểm của bài nộp
    private int assignmentId;       // ID của Assignment
    private int studentId;          // ID của Student

    // Constructor đầy đủ
    public Submission(int id, String content, Date submissionDate, double grade, int assignmentId, int studentId) {
        this.id = id;
        this.content = content;
        this.submissionDate = submissionDate;
        this.grade = grade;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
    }

    // Constructor không có ID (dùng khi tạo mới Submission)
    public Submission(String content, Date submissionDate, double grade, int assignmentId, int studentId) {
        this.content = content;
        this.submissionDate = submissionDate;
        this.grade = grade;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    // Override toString (tùy chọn)
    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", submissionDate=" + submissionDate +
                ", grade=" + grade +
                ", assignmentId=" + assignmentId +
                ", studentId=" + studentId +
                '}';
    }
}
