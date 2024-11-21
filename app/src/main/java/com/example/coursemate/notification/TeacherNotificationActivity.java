// NotificationActivity.java
package com.example.coursemate.notification;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.adapter.NotificationCourseAdapter;
import com.example.coursemate.model.Course;
import com.example.coursemate.model.Notification;
import com.example.coursemate.model.User;
import com.example.coursemate.model.UserNotification;
import com.example.coursemate.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeacherNotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCourses;
    private NotificationCourseAdapter courseAdapter;
    private List<Course> courseList;
    private DatabaseHelper dbHelper; // Lớp quản lý cơ sở dữ liệu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_notification);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Khởi tạo RecyclerView
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách khóa học
        courseList = new ArrayList<>();

        // Khởi tạo Adapter với listener để xử lý sự kiện click
        courseAdapter = new NotificationCourseAdapter(courseList, this, course -> {
            // Khi click vào một khóa học, mở dialog để nhập nội dung thông báo
            showNotificationDialog(course);
        });
        recyclerViewCourses.setAdapter(courseAdapter);

        // Tải dữ liệu khóa học từ cơ sở dữ liệu
        loadCourses();
    }

    private void loadCourses() {
        // Lấy danh sách khóa học của giáo viên hiện tại
        int teacherId = getCurrentTeacherId(); // Phương thức lấy ID giáo viên hiện tại
        courseList = dbHelper.getCoursesByTeacherId(teacherId);

        // Cập nhật Adapter
        courseAdapter.setCourses(courseList);
    }

    private int getCurrentTeacherId() {
        // Giả định bạn có phương thức lấy ID giáo viên từ session hoặc SharedPreferences
        // Ví dụ:
        // return SharedPreferencesManager.getTeacherId(this);
        return 1; // Giả định ID = 1
    }

    private void showNotificationDialog(Course course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Gửi thông Báo cho " + course.getName());

        // Set up input
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_send_notification, null);
        EditText inputMessage = dialogView.findViewById(R.id.etNotificationMessage);

        builder.setView(dialogView);

        // Set up buttons
        builder.setPositiveButton("Gửi", (dialog, which) -> {
            String message = inputMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendNotification(course, message);
            } else {
                Toast.makeText(this, "Vui lòng nhập nội dung thông báo", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void sendNotification(Course course, String message) {
        // Tạo đối tượng Notification
        Notification notification = new Notification(message, course.getId(), "new", new Date());

        // Thêm Notification vào cơ sở dữ liệu
        long notiId = dbHelper.addNotification(notification);

        if (notiId != -1) {
            // Lấy danh sách sinh viên đăng ký khóa học
            List<User> students = dbHelper.getStudentsByCourseId(course.getId());

            // Thêm vào bảng UserNotification
            for (User student : students) {
                UserNotification userNotification = new UserNotification(student.getId(), (int) notiId);
                dbHelper.addUserNotification(userNotification);
            }

            Toast.makeText(this, "Thông báo đã được gửi thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Gửi thông báo thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
