package com.example.coursemate;

public class Teacher {
    private String name;
    private int avatarResId;

    public Teacher(String name, int avatarResId) {
        this.name = name;
        this.avatarResId = avatarResId;
    }

    public String getName() {
        return name;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}
