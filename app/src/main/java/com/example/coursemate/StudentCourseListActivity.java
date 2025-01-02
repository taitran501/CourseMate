package com.example.coursemate;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.adapter.StudentCourseDashboardAdapter;
import com.example.coursemate.model.Course;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentCourseListActivity extends AppCompatActivity {

    private static final String TAG = "StudentCourseActivity";
    private RecyclerView recyclerView;
    private StudentCourseDashboardAdapter adapter;
    private ArrayList<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course);

        // Ánh xạ RecyclerView
        recyclerView = findViewById(R.id.rv_course_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách và Adapter
        courseList = new ArrayList<>();
        adapter = new StudentCourseDashboardAdapter(courseList);
        recyclerView.setAdapter(adapter);

        // Gọi hàm fetchCourses
        fetchDashboardCourses();
    }

    private void fetchDashboardCourses() {
        Log.d(TAG, "Fetching courses for dashboard");
        String rpcUrl = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "rpc/get_course_schedule";

        // Sử dụng JSONObject rỗng thay vì null
        JSONObject requestBody = new JSONObject();
        SupabaseClientHelper.getNetworkUtils().post(rpcUrl, requestBody).thenAccept(response -> {
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<Course> fetchedCourses = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("course_id");
                        String name = jsonObject.getString("course_name");
                        String description = jsonObject.optString("course_description", "Không có mô tả");
                        String startDate = jsonObject.optString("start_date", "Unknown");
                        String endDate = jsonObject.optString("end_date", "Unknown");
                        String startTime = jsonObject.optString("start_time", "Unknown");
                        String endTime = jsonObject.optString("end_time", "Unknown");

                        fetchedCourses.add(new Course(id, name, description, startDate, endDate, startTime, endTime));
                    }

                    // Cập nhật giao diện trên UI thread
                    runOnUiThread(() -> {
                        courseList.clear();
                        courseList.addAll(fetchedCourses);
                        adapter.notifyDataSetChanged();
                    });

                } catch (Exception e) {
                    Log.e(TAG, "Error parsing courses for dashboard", e);
                }
            } else {
                Log.e(TAG, "No response from Supabase RPC");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch courses", throwable);
            return null;
        });
    }
}
