package com.example.coursemate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.coursemate.model.Assignment;
import com.example.coursemate.model.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "course_management.db";
    private static final int DATABASE_VERSION = 10;

    // Tên bảng và cột
    private static final String TABLE_USER = "User";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_USERNAME = "username";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_ROLE = "role";
    private static final String COLUMN_USER_PARTNER_ID = "partner_id";

    private static final String TABLE_PARTNER = "Partner";
    private static final String COLUMN_PARTNER_ID = "id";
    private static final String COLUMN_PARTNER_NAME = "name";
    private static final String COLUMN_PARTNER_PHONE = "phone";
    private static final String COLUMN_PARTNER_EMAIL = "email";

    private static final String TABLE_COURSE = "Course";
    private static final String COLUMN_COURSE_ID = "id";
    private static final String COLUMN_COURSE_NAME = "name";
    private static final String COLUMN_COURSE_DESCRIPTION = "description";
    private static final String COLUMN_COURSE_MAX_STUDENTS = "max_students";
    private static final String COLUMN_COURSE_TEACHER_ID = "teacher_id";
    private static final String COLUMN_COURSE_STATUS = "status";
    private static final String COLUMN_COURSE_START_DATE = "start_date";
    private static final String COLUMN_COURSE_END_DATE = "end_date";
    private static final String COLUMN_COURSE_PRICE = "price";
    private static final String COLUMN_COURSE_SLOT = "slot";

    private static final String TABLE_ASSIGNMENT = "Assignment";
    private static final String COLUMN_ASSIGNMENT_ID = "id";
    private static final String COLUMN_ASSIGNMENT_TITLE = "title";
    private static final String COLUMN_ASSIGNMENT_DESCRIPTION = "description";
    private static final String COLUMN_ASSIGNMENT_DEADLINE = "deadline";
    private static final String COLUMN_ASSIGNMENT_COURSE_ID = "course_id";

    private static final String TABLE_COURSE_REGISTRATION = "CourseRegistration";
    private static final String COLUMN_REGISTRATION_ID = "id";
    private static final String COLUMN_REGISTRATION_STUDENT_ID = "student_id";
    private static final String COLUMN_REGISTRATION_COURSE_ID = "course_id";
    private static final String COLUMN_REGISTRATION_PAYMENT_STATUS = "payment_status";
    private static final String COLUMN_REGISTRATION_DATE = "registration_date";

    // Các bảng khác như Scheduled, Classroom, etc. nên được thêm tương tự

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Partner
        String createPartnerTable = "CREATE TABLE " + TABLE_PARTNER + " (" +
                COLUMN_PARTNER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PARTNER_NAME + " TEXT NOT NULL, " +
                COLUMN_PARTNER_PHONE + " TEXT NOT NULL, " +
                COLUMN_PARTNER_EMAIL + " TEXT" +
                ");";
        db.execSQL(createPartnerTable);

        // Tạo bảng User
        String createUserTable = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_USERNAME + " VARCHAR(255) NOT NULL UNIQUE, " +
                COLUMN_USER_PASSWORD + " VARCHAR(255) NOT NULL, " +
                COLUMN_USER_ROLE + " TEXT NOT NULL DEFAULT 'student', " +
                COLUMN_USER_PARTNER_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_USER_PARTNER_ID + ") REFERENCES " + TABLE_PARTNER + "(" + COLUMN_PARTNER_ID + ")" +
                ");";
        db.execSQL(createUserTable);

        // Tạo bảng Course với khóa ngoại chính xác
        String createCourseTable = "CREATE TABLE " + TABLE_COURSE + " (" +
                COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COURSE_TEACHER_ID + " INTEGER, " +
                COLUMN_COURSE_NAME + " TEXT NOT NULL, " +
                COLUMN_COURSE_DESCRIPTION + " TEXT, " +
                COLUMN_COURSE_MAX_STUDENTS + " INTEGER, " +
                COLUMN_COURSE_STATUS + " TEXT NOT NULL DEFAULT 'closed', " +
                COLUMN_COURSE_START_DATE + " TEXT, " +
                COLUMN_COURSE_END_DATE + " TEXT, " +
                COLUMN_COURSE_PRICE + " TEXT, " +
                COLUMN_COURSE_SLOT + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_COURSE_TEACHER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ")" +
                ");";
        db.execSQL(createCourseTable);

        // Tạo bảng Assignment
        String CREATE_ASSIGNMENT_TABLE = "CREATE TABLE " + TABLE_ASSIGNMENT + " (" +
                COLUMN_ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ASSIGNMENT_TITLE + " VARCHAR(255) NOT NULL, " +
                COLUMN_ASSIGNMENT_DESCRIPTION + " TEXT, " +
                COLUMN_ASSIGNMENT_DEADLINE + " DATETIME, " +
                COLUMN_ASSIGNMENT_COURSE_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ASSIGNMENT_COURSE_ID + ") REFERENCES " + TABLE_COURSE + "(" + COLUMN_COURSE_ID + ")" +
                ");";
        db.execSQL(CREATE_ASSIGNMENT_TABLE);

        // Tạo bảng CourseRegistration
        String createCourseRegistrationTable = "CREATE TABLE " + TABLE_COURSE_REGISTRATION + " (" +
                COLUMN_REGISTRATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REGISTRATION_STUDENT_ID + " INTEGER, " +
                COLUMN_REGISTRATION_COURSE_ID + " INTEGER, " +
                COLUMN_REGISTRATION_PAYMENT_STATUS + " TEXT DEFAULT 'unpaid', " +
                COLUMN_REGISTRATION_DATE + " DATETIME, " +
                "FOREIGN KEY (" + COLUMN_REGISTRATION_STUDENT_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "), " +
                "FOREIGN KEY (" + COLUMN_REGISTRATION_COURSE_ID + ") REFERENCES " + TABLE_COURSE + "(" + COLUMN_COURSE_ID + ")" +
                ");";
        db.execSQL(createCourseRegistrationTable);

        // Tạo các bảng khác như Scheduled, Classroom, StudentCourse, Notification, UserNotification tương tự...

        // Thêm dữ liệu mẫu cho bảng User (Giáo viên)
        ContentValues teacherValues = new ContentValues();
        teacherValues.put(COLUMN_USER_USERNAME, "teacher1");
        teacherValues.put(COLUMN_USER_PASSWORD, "password123"); // Mật khẩu nên được mã hóa trong thực tế
        teacherValues.put(COLUMN_USER_ROLE, "teacher");
        teacherValues.put(COLUMN_USER_PARTNER_ID, (Integer) null); // Nếu không có đối tác
        long teacherId = db.insert(TABLE_USER, null, teacherValues);

        // Bạn có thể thêm các dữ liệu mẫu khác nếu cần
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nếu bạn muốn giữ lại dữ liệu cũ và thêm cột mới, hãy sử dụng ALTER TABLE
        // Tuy nhiên, hiện tại bạn đang DROP và CREATE lại các bảng, điều này sẽ xóa dữ liệu cũ
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE_REGISTRATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNER);
        // Tạo lại các bảng với cấu trúc mới nhất
        onCreate(db);
    }

    // Phương thức để thêm khóa học mới
    public long addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_NAME, course.getName());
        values.put(COLUMN_COURSE_DESCRIPTION, course.getDescription());
        values.put(COLUMN_COURSE_SLOT, course.getSlot());
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
                            COLUMN_COURSE_SLOT
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
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_SLOT))
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
        values.put(COLUMN_COURSE_SLOT, course.getSlot());
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

    // Phương thức để lấy tất cả các khóa học của một giáo viên cụ thể
    public List<Course> getCoursesByTeacherId(int teacherId) {
        List<Course> courseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSE,
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
                        COLUMN_COURSE_SLOT
                },
                COLUMN_COURSE_TEACHER_ID + " = ?",
                new String[]{String.valueOf(teacherId)},
                null,
                null,
                COLUMN_COURSE_START_DATE + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Course course = new Course(
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
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_SLOT))
                );
                courseList.add(course);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return courseList;
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
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_SLOT))
                );
                courseList.add(course);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return courseList;
    }
}
