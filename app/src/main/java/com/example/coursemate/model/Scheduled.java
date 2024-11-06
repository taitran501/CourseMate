package com.example.coursemate.model;

public class Scheduled {
    private int id;
    private String day;
    private float startTime;
    private float endTime;
    private Course course;
    private Classroom classroom;

    // Constructor đầy đủ
    public Scheduled(int id, String day, float startTime, float endTime, Course course, Classroom classroom) {
        this.id = id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.course = course;
        this.classroom = classroom;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public float getStartTime() {
        return startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public Course getCourse() {
        return course;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(float endTime) {
        this.endTime = endTime;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }
}
