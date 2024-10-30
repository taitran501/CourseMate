package com.example.coursemate;

public class Course {
    private final String name;
    private final int iconResource;
    private final String description;  // Thêm thuộc tính mô tả khóa học
    private final String price;        // Thêm thuộc tính giá khóa học
    private final String slot;         // Thêm thuộc tính số lượng slot

    // Constructor cập nhật với các thuộc tính mới
    public Course(String name, int iconResource, String description, String price, String slot) {
        this.name = name;
        this.iconResource = iconResource;
        this.description = description;
        this.price = price;
        this.slot = slot;
    }

    // Getter cho tên khóa học
    public String getName() {
        return name;
    }

    // Getter cho icon khóa học
    public int getIconResource() {
        return iconResource;
    }

    // Getter cho mô tả khóa học
    public String getDescription() {
        return description;
    }

    // Getter cho giá khóa học
    public String getPrice() {
        return price;
    }

    // Getter cho số lượng slot
    public String getSlot() {
        return slot;
    }
}
