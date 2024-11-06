package com.example.coursemate.teacher;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.adapter.TeacherCourseAdapter;
import com.example.coursemate.model.Course;

import java.util.ArrayList;
import java.util.List;

public class TeacherCourseListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TeacherCourseAdapter adapter;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_list);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTeacherCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách khóa học
        courseList = getTeacherCourses(); // Hàm lấy danh sách khóa học cho giáo viên

        // Khởi tạo Adapter
        adapter = new TeacherCourseAdapter(this, courseList);
        recyclerView.setAdapter(adapter);
    }

    // Ví dụ hàm lấy danh sách khóa học. Bạn cần thay thế bằng cách lấy dữ liệu thực từ cơ sở dữ liệu hoặc API.
    private List<Course> getTeacherCourses() {
        List<Course> courses = new ArrayList<>();
        // Thêm dữ liệu mẫu
        courses.add(new Course(1, 101, "Lập trình Android", R.drawable.ic_course_android, "Học lập trình ứng dụng Android từ cơ bản đến nâng cao.", 30, "ongoing", "2024-01-10", "2024-06-10", "1000000 VND", "5"));
        courses.add(new Course(2, 101, "Cơ sở dữ liệu", R.drawable.ic_course_db, "Khóa học về thiết kế và quản lý cơ sở dữ liệu.", 25, "open", "2024-02-15", "2024-07-15", "1200000 VND", "4"));
        // Thêm các khóa học khác...
        return courses;
    }


}
