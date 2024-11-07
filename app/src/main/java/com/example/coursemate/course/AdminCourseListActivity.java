package com.example.coursemate.course;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.DatabaseHelper;
import com.example.coursemate.R;
import com.example.coursemate.adapter.AdminCourseAdapter;
import com.example.coursemate.model.Course;

import java.util.List;

public class AdminCourseListActivity extends AppCompatActivity implements AdminCourseAdapter.OnCourseClickListener {

    private RecyclerView courseRecyclerView;
    private AdminCourseAdapter adminCourseAdapter;
    private DatabaseHelper databaseHelper;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_list);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Thêm dữ liệu mẫu nếu bảng Course trống
        if (databaseHelper.getAllCourses().isEmpty()) {
            addSampleCourses();
        }

        // Lấy danh sách khóa học từ cơ sở dữ liệu
        courseList = databaseHelper.getAllCourses();

        // Thiết lập RecyclerView
        courseRecyclerView = findViewById(R.id.courseRecyclerView);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adminCourseAdapter = new AdminCourseAdapter(courseList, this);
        courseRecyclerView.setAdapter(adminCourseAdapter);

        // Khởi tạo Toolbar và thiết lập làm ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập tiêu đề và nút quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Khoá học");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Sự kiện click vào nút quay lại
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void addSampleCourses() {
        // Thêm dữ liệu mẫu vào cơ sở dữ liệu
        Course mathCourse = new Course(0, 101, "Math 101", R.drawable.ic_course_math, "Giới thiệu về Toán cao cấp", 30, "Mở", "2024-01-01", "2024-06-30", "500");
        Course historyCourse = new Course(0, 102, "History 201", R.drawable.ic_course_history, "Lịch sử thế giới cận đại", 25, "Đóng", "2024-02-01", "2024-07-31", "450");

        databaseHelper.addCourse(mathCourse);
        databaseHelper.addCourse(historyCourse);
    }

    @Override
    public void onCourseClick(int position) {
        Course selectedCourse = adminCourseAdapter.getCourseAtPosition(position);
        if (selectedCourse != null) {
            // Bắt đầu CourseDetailActivity và truyền course_id
            Intent intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra("course_id", selectedCourse.getId());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCourses();
    }

    private void loadCourses() {
        courseList = databaseHelper.getAllCourses();
        adminCourseAdapter.updateCourseList(courseList);
    }
}
