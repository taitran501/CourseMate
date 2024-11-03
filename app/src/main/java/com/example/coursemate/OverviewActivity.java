package com.example.coursemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursemate.course.AdminCourseListActivity;
import com.example.coursemate.student.StudentList;
import com.example.coursemate.teacher.TeacherList;

public class OverviewActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Khởi tạo Toolbar và thiết lập làm ActionBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập tiêu đề và nút quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Quản lý");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Sự kiện click vào nút quay lại
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Tìm view theo ID
        View buttonTeacherList = findViewById(R.id.button_teacher_list);
        View buttonCourseList = findViewById(R.id.button_course_list);
        View buttonStudentList = findViewById(R.id.button_student_list);

        // Sự kiện click cho nút Teacher List
        buttonTeacherList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến TeacherListActivity
                Intent intent = new Intent(OverviewActivity.this, TeacherList.class);
                startActivity(intent);
            }
        });

        // Sự kiện click cho nút Course List
        buttonCourseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến AdminCourseListActivity
                Intent intent = new Intent(OverviewActivity.this, AdminCourseListActivity.class);
                startActivity(intent);
            }
        });

        // Sự kiện click cho nút Student List
        buttonStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến StudentListActivity
                Intent intent = new Intent(OverviewActivity.this, StudentList.class);
                startActivity(intent);
            }
        });
    }
}
