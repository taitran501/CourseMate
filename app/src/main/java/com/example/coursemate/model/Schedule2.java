package com.example.coursemate.model;

public class Schedule2 {
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

    // SCHEDULE FOR FRAGMENT SCHEDULE
    public Schedule2(String courseName, String teacherName, String startTime, String endTime, String classroomName) {
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroomName = classroomName;
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

    public String getTeacherName() {
        return teacherName;
    }

    public String getClassroomName() {
        return classroomName;
    }
}
