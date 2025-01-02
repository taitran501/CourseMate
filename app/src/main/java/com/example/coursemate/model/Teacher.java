package com.example.coursemate.model;

public class Teacher {
    private String name;
    private int avatarResId;
    private String id;


    public Teacher(String name, int avatarResId, String id) {
        this.name = name;
        this.avatarResId = avatarResId;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}
