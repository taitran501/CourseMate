package com.example.coursemate.model;

public class Schedule {
    private String id;
    private String day;
    private String startTime;
    private String endTime;
    private String courseName;
    private String teacherName;
    private String classroomName;

    public Schedule(String courseName, String teacherName, String startTime, String endTime, String classroomName) {
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

    public String getTeacherName() {
        return teacherName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public String getDay() {
        return day;
    }
}
