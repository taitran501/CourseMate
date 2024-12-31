package com.example.coursemate.course_mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import com.example.coursemate.adapter.MainPageCourseAdapter;
import com.example.coursemate.auth.Login;
import com.example.coursemate.model.Course;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class CourseListMainPage extends AppCompatActivity implements MainPageCourseAdapter.OnCourseClickListener {

    private ArrayList<Course> courseList;
    private MainPageCourseAdapter mainPageCourseAdapter;
    private static final String TAG = "CourseListMainPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list_mainpage);

        // Khởi tạo RecyclerView
        RecyclerView rvCourseList = findViewById(R.id.rvCourseList);
        rvCourseList.setLayoutManager(new LinearLayoutManager(this));

        Log.d(TAG, "Setting up RecyclerView and Adapter");

        // Khởi tạo danh sách khóa học và adapter
        courseList = new ArrayList<>();
        mainPageCourseAdapter = new MainPageCourseAdapter(courseList, this);
        rvCourseList.setAdapter(mainPageCourseAdapter);

        // Thiết lập sự kiện cho nút Go to Login
        Button loginButton = findViewById(R.id.btnLoginScreen);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(CourseListMainPage.this, Login.class);
            startActivity(intent);
        });

        // Gọi API để lấy danh sách khóa học
        fetchCoursesFromAPI();
        Log.d(TAG, "Fetching courses from API: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
    }

    /**
     * Gọi API Supabase để lấy danh sách khóa học.
     */
    private void fetchCoursesFromAPI() {
        String query = "select=id,name,description,status";

        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("Course", query);
        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "Course?" + query;
        Log.d(TAG, "Request URL: " + url);
        Log.d(TAG, "Supabase URL: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
        future.thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "API Response: " + response);
                try {
                    ArrayList<Course> fetchedCourses = parseCoursesFromJson(response);
                    Log.d(TAG, "Fetched " + fetchedCourses.size() + " courses");
                    runOnUiThread(() -> {
                        courseList.clear();
                        courseList.addAll(fetchedCourses);
                        Log.d(TAG, "Updated courseList with " + courseList.size() + " courses");
                        mainPageCourseAdapter.notifyDataSetChanged();
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing courses", e);
                }
            } else {
                Log.e(TAG, "No response from API");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch courses", throwable);
            return null;
        });
    }

    /**
     * Parse JSON response thành danh sách đối tượng Course.
     */
    private ArrayList<Course> parseCoursesFromJson(String json) throws Exception {
        Log.d(TAG, "Starting parseCoursesFromJson method");
        ArrayList<Course> courses = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            // Lấy thông tin khóa học từ JSON
            String id = jsonObject.getString("id"); // UUID là String
            String name = jsonObject.getString("name");
            String description = jsonObject.optString("description", "No Description");

            Log.d(TAG, "Parsed Course: " + id + ", " + name + ", " + description);

            // Tạo đối tượng Course
            courses.add(new Course(id, 0, name, R.drawable.ic_course_icon, description, 0, "", "", ""));
        }
        return courses;
    }

    @Override
    public void onCourseClick(int position) {
        // Mở chi tiết khóa học khi nhấn vào item
        Intent intent = new Intent(CourseListMainPage.this, CourseDetailMainPage.class);

        // Truyền thông tin cơ bản của khóa học vào Intent
        Course selectedCourse = courseList.get(position);
        Log.d(TAG, "Selected Course ID: " + selectedCourse.getId());
        intent.putExtra("courseId", selectedCourse.getId());
        intent.putExtra("courseName", selectedCourse.getName());

        startActivity(intent);
    }
}
