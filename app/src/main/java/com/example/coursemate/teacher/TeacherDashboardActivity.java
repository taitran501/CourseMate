package com.example.coursemate.teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import org.json.JSONArray;
import org.json.JSONObject;

public class TeacherDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private LinearLayout layoutCourses, layoutSchedule, layoutNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        // Ánh xạ view
        tvWelcome = findViewById(R.id.tvWelcome);
        layoutCourses = findViewById(R.id.layoutCourses);
        layoutSchedule = findViewById(R.id.layoutSchedule);
        layoutNotifications = findViewById(R.id.layoutNotifications);

        // Lấy user_id từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);

        if (userId != null) {
            fetchTeacherName(userId);
        } else {
            tvWelcome.setText("Xin chào!");
            Toast.makeText(this, "Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
        }

        // Đặt các sự kiện click cho các layout
        layoutCourses.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherDashboardActivity.this, TeacherCourseListActivity.class);
            startActivity(intent);
        });

        layoutSchedule.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherDashboardActivity.this, TeacherScheduleActivity.class);
            startActivity(intent);
        });

//        layoutNotifications.setOnClickListener(view -> {
//            Intent intent = new Intent(TeacherDashboardActivity.this, TeacherNotificationActivity.class);
//            startActivity(intent);
//        });
    }

    private void fetchTeacherName(String userId) {
        String query = "select=partner_id(id,name)&id=eq." + userId;
        SupabaseClientHelper.getNetworkUtils().select("User", query).thenAccept(response -> {
            if (response != null && !response.isEmpty()) {
                try {
                    JSONArray userArray = new JSONArray(response);
                    if (userArray.length() > 0) {
                        JSONObject userObject = userArray.getJSONObject(0);
                        JSONObject partnerObject = userObject.getJSONObject("partner_id");
                        String teacherName = partnerObject.getString("name");
                        runOnUiThread(() -> tvWelcome.setText("Xin chào, " + teacherName + "!"));
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        tvWelcome.setText("Xin chào!");
                        Toast.makeText(this, "Lỗi khi xử lý dữ liệu từ máy chủ.", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                runOnUiThread(() -> {
                    tvWelcome.setText("Xin chào!");
                    Toast.makeText(this, "Không tìm thấy thông tin giáo viên.", Toast.LENGTH_SHORT).show();
                });
            }
        }).exceptionally(throwable -> {
            runOnUiThread(() -> {
                tvWelcome.setText("Xin chào!");
                Toast.makeText(this, "Lỗi kết nối đến máy chủ.", Toast.LENGTH_SHORT).show();
            });
            return null;
        });
    }
}
