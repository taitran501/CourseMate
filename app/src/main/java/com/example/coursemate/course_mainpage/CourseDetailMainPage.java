package com.example.coursemate.course_mainpage;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import com.example.coursemate.auth.Login;

import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Intent;

import java.util.Locale;

public class CourseDetailMainPage extends AppCompatActivity {

    private TextView courseNameTextView, courseDescriptionTextView, courseSlotTextView, teacherNameTextView, coursePriceTextView;
    private String courseId;
    private static final String TAG = "CourseDetailMainPage";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail_mainpage);


        courseNameTextView = findViewById(R.id.courseNameTextView);
        courseDescriptionTextView = findViewById(R.id.courseDescriptionTextView);
        courseSlotTextView = findViewById(R.id.courseSlotTextView);
        teacherNameTextView = findViewById(R.id.teacherNameTextView);
        coursePriceTextView = findViewById(R.id.coursePriceTextView);
        Button registerButton = findViewById(R.id.registerButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thông tin khoá học");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Get courseId from Intent
        courseId = getIntent().getStringExtra("courseId");
        Log.d(TAG, "Received courseId: " + courseId);

        // Xử lý nút Đăng ký
        registerButton.setOnClickListener(view -> {
            Intent loginIntent = new Intent(CourseDetailMainPage.this, Login.class);
            loginIntent.putExtra("returnTo", "payment");
            loginIntent.putExtra("courseId", courseId);
            loginIntent.putExtra("courseName", courseNameTextView.getText().toString());
            loginIntent.putExtra("coursePrice", coursePriceTextView.getText().toString());
            startActivity(loginIntent);
        });


        if (courseId != null && !courseId.isEmpty()) {
            fetchCourseDetails();
        } else {
            Log.e(TAG, "Invalid courseId: " + courseId);
        }
    }

    private void fetchCourseDetails() {
        Log.d(TAG, "Fetching course details for courseId: " + courseId);

        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "/rpc/get_course_details";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("course_uuid", courseId);
        } catch (Exception e) {
            Log.e(TAG, "Error creating request body", e);
            return;
        }

        SupabaseClientHelper.getNetworkUtils().post(url, requestBody).thenAccept(response -> {
            if (response != null && !response.isEmpty()) {
                Log.d(TAG, "API Response: " + response);
                try {
                    // Parse JSON array response
                    JSONArray coursesArray = new JSONArray(response);
                    if (coursesArray.length() > 0) {
                        JSONObject course = coursesArray.getJSONObject(0);
                        String courseName = course.optString("course_name", "No Name");
                        String courseDescription = course.optString("course_description", "No Description");
                        int courseSlot = course.optInt("max_students", 0);
                        String teacherName = course.optString("teacher_name", "Unknown Teacher");
                        long coursePrice = course.optLong("course_price", 0L); // Sử dụng optLong để đảm bảo kiểu dữ liệu long

                        updateUI(courseName, courseDescription, courseSlot, teacherName, coursePrice);
                    } else {
                        Log.e(TAG, "No course data found in response.");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing course details", e);
                }
            } else {
                Log.e(TAG, "Empty or invalid response for course details.");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch course details", throwable);
            return null;
        });
    }

    private void updateUI(String courseName, String courseDescription, int courseSlot, String teacherName, long coursePrice) {
        runOnUiThread(() -> {
            Log.d(TAG, "Updating UI with course details");
            courseNameTextView.setText(courseName);
            courseDescriptionTextView.setText(courseDescription);
            courseSlotTextView.setText(courseSlot + " slots");
            teacherNameTextView.setText("Giáo viên: " + teacherName);
            coursePriceTextView.setText("Giá tiền: " + formatCurrency(coursePrice) + " đồng");
        });
    }

    private String formatCurrency(long amount) {
        return String.format("%,d", amount).replace(",", ".");
    }
}
