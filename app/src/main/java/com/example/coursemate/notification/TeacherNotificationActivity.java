// NotificationActivity.java
package com.example.coursemate.notification;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import com.example.coursemate.adapter.NotificationCourseAdapter;
import com.example.coursemate.model.Course;
import com.example.coursemate.model.Notification;
import com.example.coursemate.model.User;
import com.example.coursemate.model.UserNotification;
import com.example.coursemate.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeacherNotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCourses;
    private NotificationCourseAdapter courseAdapter;
    private List<Course> courseList;
    private DatabaseHelper dbHelper; // Lớp quản lý cơ sở dữ liệu
    private EditText etNotificationMessage;

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
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String teacherId = sharedPreferences.getString("user_id", null);

        if (teacherId == null) {
            Toast.makeText(this, "Không tìm thấy thông tin giáo viên. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        String query = "select=id,name,description&teacher_id=eq." + teacherId;
        SupabaseClientHelper.getNetworkUtils().select("Course", query).thenAccept(response -> {
            if (response != null && !response.isEmpty()) {
                try {
                    JSONArray coursesArray = new JSONArray(response);
                    List<Course> fetchedCourses = new ArrayList<>();

                    for (int i = 0; i < coursesArray.length(); i++) {
                        JSONObject courseObject = coursesArray.getJSONObject(i);
                        Course course = new Course(
                                courseObject.getString("id"),
                                courseObject.getString("name"),
                                courseObject.optString("description", "Không có mô tả")
                        );
                        fetchedCourses.add(course);
                    }

                    runOnUiThread(() -> {
                        courseList.clear();
                        courseList.addAll(fetchedCourses);
                        courseAdapter.notifyDataSetChanged();
                    });
                } catch (Exception e) {
                    Log.e("TeacherNotificationActivity", "Lỗi phân tích dữ liệu khóa học", e);
                    runOnUiThread(() -> Toast.makeText(this, "Lỗi khi xử lý dữ liệu từ máy chủ.", Toast.LENGTH_SHORT).show());
                }
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Không tìm thấy khóa học nào.", Toast.LENGTH_SHORT).show());
            }
        }).exceptionally(throwable -> {
            Log.e("TeacherNotificationActivity", "Lỗi khi fetch dữ liệu từ Supabase", throwable);
            runOnUiThread(() -> Toast.makeText(this, "Lỗi kết nối đến máy chủ.", Toast.LENGTH_SHORT).show());
            return null;
        });
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
        try {
            // Lấy ngày hiện tại
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(new Date());

            JSONObject notificationJson = new JSONObject();
            notificationJson.put("message", message);
            notificationJson.put("course_id", course.getId());
            notificationJson.put("status", "new");
            notificationJson.put("created_at", currentDate); // Lưu ngày hiện tại

            // Log thông tin thông báo
            Log.d("sendNotification", "Notification JSON: " + notificationJson.toString());
            Toast.makeText(this, "Đang tạo thông báo...", Toast.LENGTH_SHORT).show();

            // Thêm thông báo vào bảng Notification
            SupabaseClientHelper.getNetworkUtils().insert("Notification", notificationJson.toString()).thenRun(() -> {
                runOnUiThread(() -> Toast.makeText(this, "Đã thêm thông báo vào bảng Notification.", Toast.LENGTH_SHORT).show());
                SupabaseClientHelper.getNetworkUtils().select("Notification", "select=id&message=eq." + message).thenAccept(response -> {
                    if (response != null && !response.isEmpty()) {
                        try {
                            JSONArray notificationArray = new JSONArray(response);
                            String notiId = notificationArray.getJSONObject(0).getString("id");
                            Log.d("sendNotification", "Notification ID: " + notiId);

                            String query = "select=user_id&course_id=eq." + course.getId();
                            SupabaseClientHelper.getNetworkUtils().select("StudentCourse", query).thenAccept(studentResponse -> {
                                if (studentResponse != null && !studentResponse.isEmpty()) {
                                    try {
                                        JSONArray studentsArray = new JSONArray(studentResponse);
                                        runOnUiThread(() -> Toast.makeText(this, "Đã tìm thấy " + studentsArray.length() + " sinh viên.", Toast.LENGTH_SHORT).show());

                                        for (int i = 0; i < studentsArray.length(); i++) {
                                            JSONObject studentObject = studentsArray.getJSONObject(i);
                                            String userId = studentObject.optString("user_id", null);

                                            if (userId != null) {
                                                JSONObject userNotificationJson = new JSONObject();
                                                userNotificationJson.put("user_id", userId);
                                                userNotificationJson.put("noti_id", notiId);

                                                SupabaseClientHelper.getNetworkUtils().insert("UserNotification", userNotificationJson.toString());
                                                Log.d("sendNotification", "Đã gửi thông báo đến user_id: " + userId);
                                            } else {
                                                Log.e("sendNotification", "user_id không tồn tại trong JSON Object.");
                                            }
                                        }

                                        runOnUiThread(() -> Toast.makeText(this, "Thông báo đã được gửi thành công.", Toast.LENGTH_SHORT).show());
                                    } catch (Exception e) {
                                        Log.e("sendNotification", "Lỗi xử lý danh sách sinh viên", e);
                                        runOnUiThread(() -> Toast.makeText(this, "Lỗi xử lý danh sách sinh viên.", Toast.LENGTH_SHORT).show());
                                    }
                                } else {
//                                    runOnUiThread(() -> Toast.makeText(this, "Không có sinh viên nào nhận được thông báo.", Toast.LENGTH_SHORT).show());
                                }
                            });
                        } catch (Exception e) {
                            Log.e("sendNotification", "Lỗi lấy ID thông báo", e);
                            runOnUiThread(() -> Toast.makeText(this, "Lỗi lấy ID thông báo.", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Không thể lấy ID thông báo.", Toast.LENGTH_SHORT).show());
                    }
                });
            }).exceptionally(throwable -> {
                Log.e("sendNotification", "Lỗi khi thêm thông báo", throwable);
                runOnUiThread(() -> Toast.makeText(this, "Gửi thông báo thất bại.", Toast.LENGTH_SHORT).show());
                return null;
            });
        } catch (Exception e) {
            Log.e("sendNotification", "Lỗi tạo thông báo", e);
            Toast.makeText(this, "Lỗi tạo thông báo.", Toast.LENGTH_SHORT).show();
        }
    }
}
