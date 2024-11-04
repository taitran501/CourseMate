package com.example.coursemate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.coursemate.model.Course;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "course_management.db";
    private static final int DATABASE_VERSION = 9;

    // Tên bảng và cột
    private static final String TABLE_COURSE = "Course";
    private static final String COLUMN_COURSE_ID = "id";
    private static final String COLUMN_COURSE_NAME = "name";
    private static final String COLUMN_COURSE_DESCRIPTION = "description";
    private static final String COLUMN_COURSE_SCHEDULE = "schedule";
    private static final String COLUMN_COURSE_MAX_STUDENTS = "max_students";
    private static final String COLUMN_COURSE_TEACHER_ID = "teacher_id";
    private static final String COLUMN_COURSE_STATUS = "status";
    private static final String COLUMN_COURSE_START_DATE = "start_date";
    private static final String COLUMN_COURSE_END_DATE = "end_date";
    private static final String COLUMN_COURSE_PRICE = "price";

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
        String createCourseTable = "CREATE TABLE " + TABLE_COURSE + " (" +
                COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COURSE_NAME + " TEXT NOT NULL, " +
                COLUMN_COURSE_DESCRIPTION + " TEXT, " +
                COLUMN_COURSE_SCHEDULE + " TEXT, " +
                COLUMN_COURSE_MAX_STUDENTS + " INTEGER, " +
                COLUMN_COURSE_TEACHER_ID + " INTEGER, " +
                COLUMN_COURSE_STATUS + " TEXT, " +          // Thêm cột status
                COLUMN_COURSE_START_DATE + " TEXT, " +      // Thêm cột start_date
                COLUMN_COURSE_END_DATE + " TEXT, " +        // Thêm cột end_date
                COLUMN_COURSE_PRICE + " TEXT, " +           // Thêm cột price
                "FOREIGN KEY (" + COLUMN_COURSE_TEACHER_ID + ") REFERENCES Teacher(id)" +
                ");";
        db.execSQL(createCourseTable);

        // Tạo bảng CourseRegistration nếu chưa có
        String createCourseRegistrationTable = "CREATE TABLE " + TABLE_COURSE_REGISTRATION + " (" +
                COLUMN_REGISTRATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REGISTRATION_STUDENT_ID + " INTEGER, " +
                COLUMN_REGISTRATION_COURSE_ID + " INTEGER, " +
                COLUMN_REGISTRATION_PAYMENT_STATUS + " TEXT DEFAULT 'unpaid', " +
                COLUMN_REGISTRATION_DATE + " DATETIME, " +
                "FOREIGN KEY (" + COLUMN_REGISTRATION_STUDENT_ID + ") REFERENCES Student(id), " +
                "FOREIGN KEY (" + COLUMN_REGISTRATION_COURSE_ID + ") REFERENCES Course(id)" +
                ");";
        db.execSQL(createCourseRegistrationTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nếu bạn muốn giữ lại dữ liệu cũ và thêm cột mới, hãy sử dụng ALTER TABLE
        // Tuy nhiên, hiện tại bạn đang DROP và CREATE lại các bảng, điều này sẽ xóa dữ liệu cũ
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE_REGISTRATION);

        // Tạo lại các bảng với cấu trúc mới nhất
        onCreate(db);
    }

    // Phương thức để thêm khóa học mới
    public long addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_NAME, course.getName());
        values.put(COLUMN_COURSE_DESCRIPTION, course.getDescription());
        values.put(COLUMN_COURSE_SCHEDULE, course.getSlot());
        values.put(COLUMN_COURSE_MAX_STUDENTS, course.getMaxStudents());
        values.put(COLUMN_COURSE_TEACHER_ID, course.getTeacherId());
        values.put(COLUMN_COURSE_STATUS, course.getStatus());
        values.put(COLUMN_COURSE_START_DATE, course.getStartDate()); // Đã là String
        values.put(COLUMN_COURSE_END_DATE, course.getEndDate());     // Đã là String
        values.put(COLUMN_COURSE_PRICE, course.getPrice());

        long id = db.insert(TABLE_COURSE, null, values);
        db.close();
        return id;
    }

    public Course getCourseById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Course course = null;

        try {
            cursor = db.query(TABLE_COURSE,
                    new String[]{
                            COLUMN_COURSE_ID,
                            COLUMN_COURSE_TEACHER_ID,
                            COLUMN_COURSE_NAME,
                            COLUMN_COURSE_DESCRIPTION,
                            COLUMN_COURSE_MAX_STUDENTS,
                            COLUMN_COURSE_STATUS,
                            COLUMN_COURSE_START_DATE,
                            COLUMN_COURSE_END_DATE,
                            COLUMN_COURSE_PRICE,
                            COLUMN_COURSE_SCHEDULE
                    },
                    COLUMN_COURSE_ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null);

            if (cursor != null && cursor.moveToFirst()) {
                course = new Course(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TEACHER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_NAME)),
                        R.drawable.ic_course_placeholder, // Placeholder cho icon
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_MAX_STUDENTS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_START_DATE)), // start date
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_END_DATE)),    // end date
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_SCHEDULE))
                );
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error in getCourseById", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return course;
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
        values.put(COLUMN_COURSE_STATUS, course.getStatus());
        values.put(COLUMN_COURSE_START_DATE, course.getStartDate()); // Đã là String
        values.put(COLUMN_COURSE_END_DATE, course.getEndDate());     // Đã là String
        values.put(COLUMN_COURSE_PRICE, course.getPrice());

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
                        R.drawable.ic_course_placeholder, // Placeholder cho icon
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_MAX_STUDENTS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_START_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_END_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_SCHEDULE))
                );
                courseList.add(course);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return courseList;
    }
}
