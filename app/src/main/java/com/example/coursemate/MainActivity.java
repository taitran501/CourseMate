package com.example.coursemate;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CourseAdapter.OnCourseClickListener {

    private ArrayList<Course> courseList;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Thiết lập RecyclerView cho danh sách khóa học
        RecyclerView rvCourseList = findViewById(R.id.rvCourseList);
        rvCourseList.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách khóa học
        courseList = new ArrayList<>();
        courseList.add(new Course("OOP - Lập trình hướng đối tượng", R.drawable.ic_course_icon, "Mô tả chi tiết khóa học", "500.000", "80"));
        courseList.add(new Course("DSA - Cấu trúc dữ liệu và giải thuật", R.drawable.ic_course_icon, "Mô tả chi tiết khóa học", "450.000", "100"));
        courseList.add(new Course("SQL - Cơ sở dữ liệu", R.drawable.ic_course_icon, "Mô tả chi tiết khóa học", "400.000", "90"));
        courseList.add(new Course("DOTNET - Công nghệ .NET", R.drawable.ic_course_icon, "Mô tả chi tiết khóa học", "550.000", "70"));

        // Tạo adapter và truyền danh sách khóa học cùng listener
        CourseAdapter courseAdapter = new CourseAdapter(courseList, this);
        rvCourseList.setAdapter(courseAdapter);

        // Thiết lập sự kiện cho nút Go to Login
        Button loginButton = findViewById(R.id.btnLoginScreen);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        });
    }

    @Override
    public void onCourseClick(int position) {
        Intent intent = new Intent(MainActivity.this, CourseDetailActivity.class);

        // Truyền thông tin khóa học vào Intent
        Course selectedCourse = courseList.get(position);
        intent.putExtra("courseName", selectedCourse.getName());
        intent.putExtra("courseDescription", selectedCourse.getDescription());
        intent.putExtra("coursePrice", selectedCourse.getPrice());
        intent.putExtra("courseSlot", selectedCourse.getSlot());

        startActivity(intent);
    }
}
