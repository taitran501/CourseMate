package com.example.coursemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        // Sự kiện click vào nút quay lại (nếu cần)
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Find views by ID
        View buttonTeacherList = findViewById(R.id.button_teacher_list);
        View buttonCourseList = findViewById(R.id.button_course_list);
        View buttonStudentList = findViewById(R.id.button_student_list);
        // Set click listener for Teacher List button
        buttonTeacherList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to TeacherListActivity
                Intent intent = new Intent(OverviewActivity.this, TeacherList.class);
                startActivity(intent);
            }
        });

        // Set click listener for Course List button
        buttonCourseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CourseListActivity

//                Intent intent = new Intent(OverviewActivity.this, CourseListActivity.class);
//                startActivity(intent);
            }
        });

        // Set click listener for Student List button
        buttonStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to TeacherListActivity
                Intent intent = new Intent(OverviewActivity.this, StudentList.class);
                startActivity(intent);
            }
        });
    }
}

