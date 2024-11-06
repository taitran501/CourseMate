package com.example.coursemate.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursemate.R;
import com.example.coursemate.teacher.TeacherCourseListActivity;
/*
import com.example.coursemate.teacher.activities.AssignmentManagementActivity;
import com.example.coursemate.teacher.activities.ScheduleActivity;
import com.example.coursemate.teacher.activities.NotificationActivity;
import com.example.coursemate.teacher.activities.RegistrationManagementActivity;
 */
public class TeacherDashboardActivity extends AppCompatActivity {

    private LinearLayout layoutCourses, layoutSchedule, layoutAssignments, layoutNotifications, layoutRegistrations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        // Khởi tạo các layout cho từng chức năng
        layoutCourses = findViewById(R.id.layoutCourses);
        layoutSchedule = findViewById(R.id.layoutSchedule);
        layoutAssignments = findViewById(R.id.layoutAssignments);
        layoutNotifications = findViewById(R.id.layoutNotifications);
        layoutRegistrations = findViewById(R.id.layoutRegistrations);

        // Thiết lập các sự kiện click cho từng chức năng

        layoutCourses.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherDashboardActivity.this, TeacherCourseListActivity.class);
            startActivity(intent);
        });
        /*
        layoutSchedule.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherDashboardActivity.this, TeacherScheduleActivity.class);
            startActivity(intent);
        });
        layoutSchedule.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherDashboardActivity.this, TeacherAssignmentManagementActivity.class);
            startActivity(intent);
        });
        /*
        layoutNotifications.setOnClickListener(view -> startActivity(new Intent(TeacherDashboardActivity.this, NotificationActivity.class)));
        layoutRegistrations.setOnClickListener(view -> startActivity(new Intent(TeacherDashboardActivity.this, RegistrationManagementActivity.class)));
         */
    }
}
