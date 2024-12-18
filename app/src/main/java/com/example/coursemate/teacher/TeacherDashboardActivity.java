//package com.example.coursemate.teacher;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.LinearLayout;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.coursemate.R;
//import com.example.coursemate.notification.TeacherNotificationActivity;
//
//public class TeacherDashboardActivity extends AppCompatActivity {
//
//    private LinearLayout layoutCourses, layoutSchedule, layoutNotifications;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_teacher_dashboard);
//
//        // Khởi tạo các layout cho từng chức năng
//        layoutCourses = findViewById(R.id.layoutCourses);
//        layoutSchedule = findViewById(R.id.layoutSchedule);
//        layoutNotifications = findViewById(R.id.layoutNotifications);
//
//        // Thiết lập các sự kiện click cho từng chức năng
//
//        layoutCourses.setOnClickListener(view -> {
//            Intent intent = new Intent(TeacherDashboardActivity.this, TeacherCourseListActivity.class);
//            startActivity(intent);
//        });
//        layoutSchedule.setOnClickListener(view -> {
//            Intent intent = new Intent(TeacherDashboardActivity.this, TeacherScheduleActivity.class);
//            startActivity(intent);
//        });
//        layoutNotifications.setOnClickListener(view -> {
//            Intent intent = new Intent(TeacherDashboardActivity.this, TeacherNotificationActivity.class);
//            startActivity(intent);
//        });
//    }
//}
