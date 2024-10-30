package com.example.coursemate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "course_management.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Course
        String createCourseTable = "CREATE TABLE Course (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "schedule TEXT, " +
                "max_students INTEGER, " +
                "teacher_id INTEGER, " +
                "FOREIGN KEY (teacher_id) REFERENCES Teacher(id));";
        db.execSQL(createCourseTable);

        // Tạo bảng CourseRegistration
        String createCourseRegistrationTable = "CREATE TABLE CourseRegistration (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "course_id INTEGER, " +
                "payment_status TEXT DEFAULT 'unpaid', " +
                "registration_date DATETIME, " +
                "FOREIGN KEY (student_id) REFERENCES Student(id), " +
                "FOREIGN KEY (course_id) REFERENCES Course(id));";
        db.execSQL(createCourseRegistrationTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa các bảng nếu đã tồn tại và tạo lại
        db.execSQL("DROP TABLE IF EXISTS CourseRegistration");
        db.execSQL("DROP TABLE IF EXISTS Course");
        onCreate(db);
    }
}
