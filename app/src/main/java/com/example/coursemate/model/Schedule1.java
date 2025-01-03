package com.example.coursemate.model;

public class Schedule1 {
    private String id;
    private String day;
    private String startTime;
    private String endTime;
    private String courseName;
    private String teacherName;
    private String classroomName;
    private String description;
    private String startDate;
    private String endDate;

    // SCHEDULE FOR SEE ALL COURSES IN STUDENT DASHBOARD
    public Schedule1(String id, String name, String description, String startDate, String endDate, String startTime, String endTime) {
        this.id = id;
        this.courseName = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters và Setters
    public String getCourseName() {
        return courseName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
