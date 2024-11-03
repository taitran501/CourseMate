package com.example.coursemate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.coursemate.model.Course;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "course_management.db";
    private static final int DATABASE_VERSION = 5;

    // Tên bảng và cột
    private static final String TABLE_COURSE = "Course";
    private static final String COLUMN_COURSE_ID = "id";
    private static final String COLUMN_COURSE_NAME = "name";
    private static final String COLUMN_COURSE_DESCRIPTION = "description";
    private static final String COLUMN_COURSE_SCHEDULE = "schedule";
    private static final String COLUMN_COURSE_MAX_STUDENTS = "max_students";
    private static final String COLUMN_COURSE_TEACHER_ID = "teacher_id";

    private static final String TABLE_COURSE_REGISTRATION = "CourseRegistration";
    private static final String COLUMN_REGISTRATION_ID = "id";
    private static final String COLUMN_REGISTRATION_STUDENT_ID = "student_id";
    private static final String COLUMN_REGISTRATION_COURSE_ID = "course_id";
    private static final String COLUMN_REGISTRATION_PAYMENT_STATUS = "payment_status";
    private static final String COLUMN_REGISTRATION_DATE = "registration_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Course với các cột cần thiết
        String createCourseTable = "CREATE TABLE Course (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "schedule TEXT, " +
                "max_students INTEGER, " +
                "teacher_id INTEGER, " +
                "status TEXT, " +              // Thêm cột status
                "dstart_date TEXT, " +         // Thêm cột start date
                "end_date TEXT, " +            // Thêm cột end date
                "price TEXT, " +               // Nếu chưa có cột price, thêm vào
                "FOREIGN KEY (teacher_id) REFERENCES Teacher(id));";
        db.execSQL(createCourseTable);

        // Tạo bảng CourseRegistration nếu chưa có
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
        // Xóa các bảng nếu chúng đã tồn tại
        db.execSQL("DROP TABLE IF EXISTS Course");
        db.execSQL("DROP TABLE IF EXISTS CourseRegistration");

        // Tạo lại các bảng với cấu trúc mới nhất
        onCreate(db);
    }




    // Phương thức để thêm khóa học mới
    public long addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_NAME, course.getName());
        values.put(COLUMN_COURSE_DESCRIPTION, course.getDescription());
        values.put(COLUMN_COURSE_SCHEDULE, course.getSlot()); // Placeholder nếu slot là schedule
        values.put(COLUMN_COURSE_MAX_STUDENTS, course.getMaxStudents());
        values.put(COLUMN_COURSE_TEACHER_ID, course.getTeacherId());

        long id = db.insert(TABLE_COURSE, null, values);
        db.close();
        return id;
    }

    public Course getCourseById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Course", new String[]{"id", "name", "description", "max_students", "status", "dstart_date", "end_date", "price"},
                "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Course course = new Course(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("teacher_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    R.drawable.ic_course_placeholder, // Placeholder cho icon
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("max_students")),
                    cursor.getString(cursor.getColumnIndexOrThrow("status")),
                    cursor.getString(cursor.getColumnIndexOrThrow("dstart_date")), // start date
                    cursor.getString(cursor.getColumnIndexOrThrow("end_date")),    // end date
                    cursor.getString(cursor.getColumnIndexOrThrow("price")),
                    "20" // Placeholder slot
            );
            cursor.close();
            return course;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }


    // Phương thức để cập nhật thông tin khóa học
    public int updateCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_NAME, course.getName());
        values.put(COLUMN_COURSE_DESCRIPTION, course.getDescription());
        values.put(COLUMN_COURSE_SCHEDULE, course.getSlot());
        values.put(COLUMN_COURSE_MAX_STUDENTS, course.getMaxStudents());
        values.put(COLUMN_COURSE_TEACHER_ID, course.getTeacherId());

        int rowsUpdated = db.update(TABLE_COURSE, values, COLUMN_COURSE_ID + " = ?", new String[]{String.valueOf(course.getId())});
        db.close();
        return rowsUpdated;
    }

    // Phương thức để lấy tất cả các khóa học
    public List<Course> getAllCourses() {
        List<Course> courseList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_COURSE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Course course = new Course(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TEACHER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_NAME)),
                        R.drawable.ic_course_placeholder,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_MAX_STUDENTS)),
                        "ongoing", // Placeholder status
                        null,      // Placeholder start date
                        null,      // Placeholder end date
                        "500",     // Placeholder price
                        "20"       // Placeholder slot
                );
                courseList.add(course);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return courseList;
    }
}
