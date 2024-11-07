package com.example.coursemate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.coursemate.model.Course;
import com.example.coursemate.model.Notification;
import com.example.coursemate.model.User;
import com.example.coursemate.model.UserNotification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "course_management.db";
    private static final int DATABASE_VERSION = 13;

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

        // Tạo bảng Notification
        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE Notification (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "message TEXT, " +
                "course_id INTEGER, " +
                "status TEXT, " +
                "created_at DATETIME, " +
                "FOREIGN KEY(course_id) REFERENCES Course(id)" +
                ");";
        db.execSQL(CREATE_NOTIFICATION_TABLE);

        // Tạo bảng UserNotification
        String CREATE_USERNOTIFICATION_TABLE = "CREATE TABLE UserNotification (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "noti_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES User(id), " +
                "FOREIGN KEY(noti_id) REFERENCES Notification(id)" +
                ");";
        db.execSQL(CREATE_USERNOTIFICATION_TABLE);

        // Thêm dữ liệu mẫu cho bảng User (Giáo viên)
        ContentValues teacherValues = new ContentValues();
        teacherValues.put(COLUMN_USER_USERNAME, "teacher1");
        teacherValues.put(COLUMN_USER_PASSWORD, "password123"); // Mật khẩu nên được mã hóa trong thực tế
        teacherValues.put(COLUMN_USER_ROLE, "teacher");
        teacherValues.put(COLUMN_USER_PARTNER_ID, (Integer) null); // Nếu không có đối tác
        long teacherId = db.insert(TABLE_USER, null, teacherValues);

        // Thêm dữ liệu mẫu cho bảng Course (Giáo viên ID = teacherId)
        ContentValues courseValues = new ContentValues();
        // Khóa học 1: Lập trình Android
        courseValues.put(COLUMN_COURSE_NAME, "Lập trình Android");
        courseValues.put(COLUMN_COURSE_DESCRIPTION, "Học lập trình ứng dụng Android từ cơ bản đến nâng cao.");
        courseValues.put(COLUMN_COURSE_MAX_STUDENTS, 30);
        courseValues.put(COLUMN_COURSE_TEACHER_ID, teacherId); // teacherId = 101
        courseValues.put(COLUMN_COURSE_STATUS, "ongoing");
        courseValues.put(COLUMN_COURSE_START_DATE, "2024-01-10");
        courseValues.put(COLUMN_COURSE_END_DATE, "2024-06-10");
        courseValues.put(COLUMN_COURSE_PRICE, "1000000 VND");
        long courseId1 = db.insert(TABLE_COURSE, null, courseValues);

        // Khóa học 2: Cơ sở dữ liệu
        courseValues.put(COLUMN_COURSE_NAME, "Cơ sở dữ liệu");
        courseValues.put(COLUMN_COURSE_DESCRIPTION, "Khóa học về thiết kế và quản lý cơ sở dữ liệu.");
        courseValues.put(COLUMN_COURSE_MAX_STUDENTS, 25);
        courseValues.put(COLUMN_COURSE_TEACHER_ID, teacherId); // teacherId = 101
        courseValues.put(COLUMN_COURSE_STATUS, "open");
        courseValues.put(COLUMN_COURSE_START_DATE, "2024-02-15");
        courseValues.put(COLUMN_COURSE_END_DATE, "2024-07-15");
        courseValues.put(COLUMN_COURSE_PRICE, "1200000 VND");
        long courseId2 = db.insert(TABLE_COURSE, null, courseValues);

        // Thêm dữ liệu mẫu cho bảng User (Sinh viên)
        ContentValues studentValues = new ContentValues();
        studentValues.put(COLUMN_USER_USERNAME, "student1");
        studentValues.put(COLUMN_USER_PASSWORD, "password1");
        studentValues.put(COLUMN_USER_ROLE, "student");
        studentValues.put(COLUMN_USER_PARTNER_ID, (Integer) null);
        long studentId1 = db.insert(TABLE_USER, null, studentValues);

        studentValues.put(COLUMN_USER_USERNAME, "student2");
        studentValues.put(COLUMN_USER_PASSWORD, "password2");
        studentValues.put(COLUMN_USER_ROLE, "student");
        studentValues.put(COLUMN_USER_PARTNER_ID, (Integer) null);
        long studentId2 = db.insert(TABLE_USER, null, studentValues);

        // Thêm dữ liệu mẫu vào bảng CourseRegistration
        ContentValues courseRegistrationValues = new ContentValues();
        courseRegistrationValues.put(COLUMN_REGISTRATION_STUDENT_ID, studentId1);
        courseRegistrationValues.put(COLUMN_REGISTRATION_COURSE_ID, courseId1);
        courseRegistrationValues.put(COLUMN_REGISTRATION_PAYMENT_STATUS, "paid");
        courseRegistrationValues.put(COLUMN_REGISTRATION_DATE, "2024-08-20");
        db.insert(TABLE_COURSE_REGISTRATION, null, courseRegistrationValues);

        courseRegistrationValues.put(COLUMN_REGISTRATION_STUDENT_ID, studentId2);
        courseRegistrationValues.put(COLUMN_REGISTRATION_COURSE_ID, courseId1);
        courseRegistrationValues.put(COLUMN_REGISTRATION_PAYMENT_STATUS, "paid");
        courseRegistrationValues.put(COLUMN_REGISTRATION_DATE, "2024-08-22");
        db.insert(TABLE_COURSE_REGISTRATION, null, courseRegistrationValues);

        courseRegistrationValues.put(COLUMN_REGISTRATION_STUDENT_ID, studentId1);
        courseRegistrationValues.put(COLUMN_REGISTRATION_COURSE_ID, courseId2);
        courseRegistrationValues.put(COLUMN_REGISTRATION_PAYMENT_STATUS, "unpaid");
        courseRegistrationValues.put(COLUMN_REGISTRATION_DATE, "2024-08-25");
        db.insert(TABLE_COURSE_REGISTRATION, null, courseRegistrationValues);

// Thêm dữ liệu mẫu cho bảng Notification
        ContentValues notificationValues = new ContentValues();

// Thông báo cho khóa học 1: Lập trình Android
        notificationValues.put("message", "Khóa học Lập trình Android bắt đầu vào tuần sau.");
        notificationValues.put("course_id", courseId1); // courseId1 = ID của "Lập trình Android"
        notificationValues.put("status", "new");
        notificationValues.put("created_at", "2024-01-10 10:00:00");
        long notiId1 = db.insert("Notification", null, notificationValues);
        Log.d("DatabaseHelper", "Inserted Notification1 with ID: " + notiId1);

// Thông báo cho khóa học 2: Cơ sở dữ liệu
        notificationValues.put("message", "Khóa học Cơ sở dữ liệu có thay đổi lịch học.");
        notificationValues.put("course_id", courseId2); // courseId2 = ID của "Cơ sở dữ liệu"
        notificationValues.put("status", "new");
        notificationValues.put("created_at", "2024-01-11 11:30:00");
        long notiId2 = db.insert("Notification", null, notificationValues);
        Log.d("DatabaseHelper", "Inserted Notification2 with ID: " + notiId2);

        // Thêm dữ liệu mẫu cho bảng UserNotification
        ContentValues userNotiValues = new ContentValues();

// Liên kết notiId1 với studentId1
        userNotiValues.put("user_id", studentId1);
        userNotiValues.put("noti_id", notiId1);
        db.insert("UserNotification", null, userNotiValues);

// Reset ContentValues để tránh carry-over dữ liệu
        userNotiValues.clear();

// Liên kết notiId1 với studentId2
        userNotiValues.put("user_id", studentId2);
        userNotiValues.put("noti_id", notiId1);
        db.insert("UserNotification", null, userNotiValues);

// Reset ContentValues lại
        userNotiValues.clear();

// Liên kết notiId2 với studentId1
        userNotiValues.put("user_id", studentId1);
        userNotiValues.put("noti_id", notiId2);
        db.insert("UserNotification", null, userNotiValues);

// Reset ContentValues lại
        userNotiValues.clear();

// Liên kết notiId2 với studentId2
        userNotiValues.put("user_id", studentId2);
        userNotiValues.put("noti_id", notiId2);
        db.insert("UserNotification", null, userNotiValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // DROP tất cả các bảng đã tạo trong onCreate, bao gồm Notification và UserNotification
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE_REGISTRATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNER);
        db.execSQL("DROP TABLE IF EXISTS Notification");
        db.execSQL("DROP TABLE IF EXISTS UserNotification");
        // Tạo lại các bảng với cấu trúc mới nhất
        onCreate(db);
    }


    // Phương thức để thêm khóa học mới
    public long addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_NAME, course.getName());
        values.put(COLUMN_COURSE_DESCRIPTION, course.getDescription());
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
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_PRICE))
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
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_PRICE))
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
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_PRICE))
                );
                courseList.add(course);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return courseList;
    }

    // Phương thức thêm Notification
    public long addNotification(Notification notification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("message", notification.getMessage());
        values.put("course_id", notification.getCourseId());
        values.put("status", notification.getStatus());

        // Chuyển đổi Date sang định dạng String
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAt = sdf.format(notification.getCreatedAt());
        values.put("created_at", createdAt);

        long id = db.insert("Notification", null, values);
        db.close();
        return id;
    }

    // Phương thức thêm UserNotification
    public long addUserNotification(UserNotification userNotification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userNotification.getUserId());
        values.put("noti_id", userNotification.getNotiId());

        long id = db.insert("UserNotification", null, values);
        db.close();
        return id;
    }

    // Phương thức lấy danh sách sinh viên đăng ký khóa học
    public List<User> getStudentsByCourseId(int courseId) {
        List<User> students = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT User.id, User.username, User.password, User.role, User.partner_id " +
                "FROM User " +
                "JOIN CourseRegistration ON User.id = CourseRegistration.student_id " +
                "WHERE CourseRegistration.course_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courseId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                int partnerId = cursor.getInt(cursor.getColumnIndexOrThrow("partner_id"));

                User user = new User(id, username, password, role, partnerId);
                students.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return students;
    }
}
