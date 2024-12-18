package com.example.coursemate.course_mainpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursemate.NetworkUtils;
import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;

import org.json.JSONArray;
import org.json.JSONObject;

public class CourseDetailMainPage extends AppCompatActivity {

    private TextView courseNameTextView, courseDescriptionTextView, courseSlotTextView, teacherNameTextView;
    private Button registerButton;
    private String courseId; // UUID thay vì int
    private static final String TAG = "CourseDetailMainPage";

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail_mainpage);

        // Nhận giá trị từ Intent
        Intent intent = getIntent();
        courseId = intent.getStringExtra("courseId");
        Log.d(TAG, "Received courseId: " + courseId);

        if (courseId == null || courseId.isEmpty()) {
            Log.e(TAG, "Invalid courseId: " + courseId);
            return;
        }

        // Khởi tạo các TextView và Button
        courseNameTextView = findViewById(R.id.courseNameTextView);
        courseDescriptionTextView = findViewById(R.id.courseDescriptionTextView);
        courseSlotTextView = findViewById(R.id.courseSlotTextView);
        teacherNameTextView = findViewById(R.id.teacherNameTextView);
        registerButton = findViewById(R.id.registerButton);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Thiết lập Toolbar làm ActionBar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thông tin khoá học");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Sự kiện click vào nút quay lại
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Gọi API để lấy thông tin chi tiết khóa học
        fetchCourseDetailsFromAPI();
    }

    /**
     * Gọi API Supabase để lấy thông tin chi tiết khóa học.
     */
    private void fetchCourseDetailsFromAPI() {
        Log.d(TAG, "Fetching course details for courseId: " + courseId);
        String queryCourse = "select=id,name,description,max_students,teacher_id&status=eq.open&id=eq." + courseId;

        SupabaseClientHelper.getNetworkUtils().select("Course", queryCourse).thenAccept(response -> {
            if (response != null && !response.isEmpty()) {
                Log.d(TAG, "Course API Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        JSONObject course = jsonArray.getJSONObject(0);
                        String courseName = course.optString("name", "No Name");
                        String courseDescription = course.optString("description", "No Description");
                        int courseSlot = course.optInt("max_students", 0);
                        String teacherId = course.optString("teacher_id", "");

                        Log.d(TAG, "Course Name: " + courseName);
                        Log.d(TAG, "Course Description: " + courseDescription);
                        Log.d(TAG, "Course Slots: " + courseSlot);
                        Log.d(TAG, "Teacher ID: " + teacherId);

                        fetchTeacherDetails(teacherId, courseName, courseDescription, courseSlot);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing course details", e);
                }
            } else {
                Log.e(TAG, "Empty response for course details.");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch course details", throwable);
            return null;
        });
    }

    private void fetchTeacherDetails(String teacherId, String courseName, String courseDescription, int courseSlot) {
        if (teacherId == null || teacherId.isEmpty()) {
            updateUI(courseName, courseDescription, courseSlot, "Unknown Teacher");
            return;
        }

        String queryUser = "select=partner_id&id=eq." + teacherId;
        Log.d(TAG, "Fetching teacher details with teacherId: " + teacherId);

        SupabaseClientHelper.getNetworkUtils().select("User", queryUser).thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "Teacher API Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        String partnerId = jsonArray.getJSONObject(0).optString("partner_id", "");
                        Log.d(TAG, "Partner ID: " + partnerId);
                        fetchPartnerDetails(partnerId, courseName, courseDescription, courseSlot);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error fetching teacher details", e);
                }
            }
        });
    }

    private void fetchPartnerDetails(String partnerId, String courseName, String courseDescription, int courseSlot) {
        if (partnerId == null || partnerId.isEmpty()) {
            updateUI(courseName, courseDescription, courseSlot, "Unknown Teacher");
            return;
        }

        String queryPartner = "select=name&id=eq." + partnerId;
        Log.d(TAG, "Fetching partner details with partnerId: " + partnerId);

        SupabaseClientHelper.getNetworkUtils().select("Partner", queryPartner).thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "Partner API Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String teacherName = jsonArray.length() > 0 ? jsonArray.getJSONObject(0).optString("name", "Unknown Teacher") : "Unknown Teacher";
                    Log.d(TAG, "Teacher Name: " + teacherName);
                    updateUI(courseName, courseDescription, courseSlot, teacherName);
                } catch (Exception e) {
                    Log.e(TAG, "Error fetching partner details", e);
                }
            }
        });
    }

    private void updateUI(String courseName, String courseDescription, int courseSlot, String teacherName) {
        runOnUiThread(() -> {
            Log.d(TAG, "Updating UI with course details");
            courseNameTextView.setText(courseName);
            courseDescriptionTextView.setText(courseDescription);
            courseSlotTextView.setText(courseSlot + " slots");
            teacherNameTextView.setText("Giáo viên: " + teacherName);
        });
    }
}
