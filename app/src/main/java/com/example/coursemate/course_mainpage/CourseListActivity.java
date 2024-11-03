package com.example.coursemate.course_mainpage;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.DatabaseHelper;
import com.example.coursemate.R;
import com.example.coursemate.adapter.CourseAdapter;
import com.example.coursemate.model.Course;

import java.util.ArrayList;

public class CourseListActivity extends AppCompatActivity implements CourseAdapter.OnCourseClickListener {

    private ArrayList<Course> courseList;
    private DatabaseHelper dbHelper; // Khai báo DatabaseHelper
    private SQLiteDatabase db; // Khai báo SQLiteDatabase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase(); // Mở cơ sở dữ liệu

        RecyclerView rvCourseList = findViewById(R.id.rvCourseList);
        rvCourseList.setLayoutManager(new LinearLayoutManager(this));

        courseList = new ArrayList<>();
        courseList.add(new Course("OOP - Lập trình hướng đối tượng", R.drawable.ic_course_icon, "Mô tả chi tiết khóa học", "500.000", "80"));
        courseList.add(new Course("DSA - Cấu trúc dữ liệu và giải thuật", R.drawable.ic_course_icon, "Mô tả chi tiết khóa học", "450.000", "100"));
        courseList.add(new Course("SQL - Cơ sở dữ liệu", R.drawable.ic_course_icon, "Mô tả chi tiết khóa học", "400.000", "90"));
        courseList.add(new Course("DOTNET - Công nghệ .NET", R.drawable.ic_course_icon, "Mô tả chi tiết khóa học", "550.000", "70"));


        // Tạo adapter và truyền danh sách khóa học cùng listener
        CourseAdapter courseAdapter = new CourseAdapter(courseList, this);
        rvCourseList.setAdapter(courseAdapter);
    }


    @Override
    public void onCourseClick(int position) {
        Intent intent = new Intent(CourseListActivity.this, CourseDetailActivity.class);

        // Truyền thông tin khóa học vào Intent
        Course selectedCourse = courseList.get(position);
        intent.putExtra("courseName", selectedCourse.getName());
        intent.putExtra("courseDescription", selectedCourse.getDescription());
        intent.putExtra("coursePrice", selectedCourse.getPrice());
        intent.putExtra("courseSlot", selectedCourse.getSlot());

        startActivity(intent);
    }
}
