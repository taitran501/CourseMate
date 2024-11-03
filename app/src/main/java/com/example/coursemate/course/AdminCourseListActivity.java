package com.example.coursemate.course;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursemate.R;
import com.example.coursemate.adapter.AdminCourseAdapter;
import com.example.coursemate.model.Course;
import java.util.ArrayList;
import java.util.List;

public class AdminCourseListActivity extends AppCompatActivity {

    private RecyclerView courseRecyclerView;
    private AdminCourseAdapter adminCourseAdapter;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_list);

        courseRecyclerView = findViewById(R.id.courseRecyclerView);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseList = new ArrayList<>();

        // Khởi tạo Toolbar và thiết lập làm ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập tiêu đề và nút quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Giáo viên");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Sự kiện click vào nút quay lại
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Thêm dữ liệu mẫu vào courseList (hoặc lấy từ cơ sở dữ liệu)
        courseList.add(new Course(1, 101, "Math 101", R.drawable.ic_course_math, "Introduction to Math", 30, "open", null, null, "500", "15"));
        courseList.add(new Course(2, 102, "History 201", R.drawable.ic_course_history, "World History", 25, "closed", null, null, "450", "10"));

        adminCourseAdapter = new AdminCourseAdapter(this, courseList);
        courseRecyclerView.setAdapter(adminCourseAdapter);
    }
}
