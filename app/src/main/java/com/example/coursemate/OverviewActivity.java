package com.example.coursemate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursemate.course.AdminCourseListActivity;
import com.example.coursemate.model.Course;
import com.example.coursemate.model.Teacher;
import com.example.coursemate.student.StudentList;
import com.example.coursemate.teacher.TeacherList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OverviewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private static final String TAG = "OverviewActivity";


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

        // Sự kiện click vào nút quay lại
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Tìm view theo ID
        View buttonTeacherList = findViewById(R.id.button_teacher_list);
        View buttonCourseList = findViewById(R.id.button_course_list);
        View buttonStudentList = findViewById(R.id.button_student_list);
        View buttonReList = findViewById(R.id.button_registration_list);

        // Sự kiện click cho nút Teacher List
        buttonReList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến TeacherListActivity
                Intent intent = new Intent(OverviewActivity.this, RegistrationList.class);
                startActivity(intent);
            }
        });
        buttonTeacherList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến TeacherListActivity
                Intent intent = new Intent(OverviewActivity.this, TeacherList.class);
                startActivity(intent);
            }
        });

        // Sự kiện click cho nút Course List
        buttonCourseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến AdminCourseListActivity
                Intent intent = new Intent(OverviewActivity.this, AdminCourseListActivity.class);
                startActivity(intent);
            }
        });

        // Sự kiện click cho nút Student List
        buttonStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến StudentListActivity
                Intent intent = new Intent(OverviewActivity.this, StudentList.class);
                startActivity(intent);
            }
        });

        try {
            fetchTeacherFromAPI();
            fetchCourseFromAPI();
            fetchStudentFromAPI();
            fetchRegistrationFromAPI();
            fetchCourseRegistrationsFromAPI();
            fetchAmountCourseRegistrationsFromAPI();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchCourseRegistrationsFromAPI() throws ExecutionException, InterruptedException {
        String query = "select=count:course_id.count(),amount.sum(),Course(name)&payment_status=eq.paid&order=count.desc&limit=4";

        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("CourseRegistration", query);
        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "CourseRegistration?" + query;
        Log.d(TAG, "Request URL: " + url);
        Log.d(TAG, "Supabase URL: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
        future.get();

        future.thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "API Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    Log.d(TAG, "Fetched " + jsonArray.length() + " CourseRegistration");
                    Log.d(TAG, "Fetched " + jsonArray );
                    BarChart barChart = findViewById(R.id.barChart);
                    ArrayList<BarEntry> barEntries = new ArrayList<>();
                    List<String> labels = new ArrayList<>();
                    for (int i =0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            barEntries.add(new BarEntry(i+0.5f, jsonObject.getInt("count")));

                            String courseName = jsonObject.getJSONObject("Course").getString("name");

                            if (courseName.length() > 15) {
                                courseName = courseName.substring(0, 15) + "\n" + courseName.substring(15);
                            }
                            labels.add(courseName);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    BarDataSet barDataSet = new BarDataSet(barEntries, "Courses Count Sale");


                    List<Integer> colors = new ArrayList<>();
                    colors.add(getResources().getColor(R.color.barColor1));
                    colors.add(getResources().getColor(R.color.barColor2));
                    colors.add(getResources().getColor(R.color.barColor3));
                    colors.add(getResources().getColor(R.color.barColor4));
                    colors.add(getResources().getColor(R.color.barColor5));
                    barDataSet.setColors(colors); // Set custom colors


                    // Configure the X-axis for labels
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1f); // Ensure labels correspond to each bar
                    xAxis.setGranularityEnabled(true);
                    xAxis.setLabelRotationAngle(-45); // Rotate labels
                    barChart.setExtraBottomOffset(20f); // Add extra space at the bottom


                    // Style the chart
                    barChart.getDescription().setEnabled(false); // Disable description
                    barChart.getAxisRight().setEnabled(false); // Disable right Y-axis
                    barChart.getLegend().setEnabled(false); // Disable legend


                    BarData barData = new BarData(barDataSet);
                    barData.setBarWidth(0.8f);
                    barChart.setData(barData);
                    barChart.setFitBars(true);
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing CourseRegistration", e);
                }
            } else {
                Log.e(TAG, "No response from API");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch CourseRegistration", throwable);
            return null;
        });
    }
    private void fetchAmountCourseRegistrationsFromAPI() throws ExecutionException, InterruptedException {
        String query = "select=count:registration_date.count(),amount.sum(),registration_date&payment_status=eq.paid&order=registration_date.desc&limit=5";

        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("CourseRegistration", query);
        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "CourseRegistration?" + query;
        Log.d(TAG, "Request URL: " + url);
        Log.d(TAG, "Supabase URL: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
        future.get();

        future.thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "API Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    Log.d(TAG, "Fetched " + jsonArray.length() + " CourseRegistration");
                    Log.d(TAG, "Fetched " + jsonArray );
                    LineChart lineChart = findViewById(R.id.lineChart);

                    ArrayList<Entry> lineEntries = new ArrayList<>();
                    lineEntries.add(new Entry(0, 0));

                    List<String> labels = new ArrayList<>();
                    labels.add("");

                    for (int i =0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            lineEntries.add(new Entry(i+1f, jsonObject.getInt("sum")));


                            String courseName = jsonObject.getString("registration_date");

                            if (courseName.length() > 15) {
                                courseName = courseName.substring(0, 15) + "\n" + courseName.substring(15);
                            }
                            labels.add(courseName);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    LineDataSet lineDataSet = new LineDataSet(lineEntries, "Amount of Course Registrations Sales");
                    Log.d(TAG, "Labels " + labels );
                    // Configure X-Axis labels
                    XAxis xAxis = lineChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // Use your labels list
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Place labels below bars
                    xAxis.setGranularity(1f); // Ensure each label corresponds to a bar
                    xAxis.setGranularityEnabled(true); // Enable granularity
                    xAxis.setLabelRotationAngle(-45); // Rotate labels
                    xAxis.setDrawGridLines(false); // Optional: Remove grid lines for cleaner appearance
                    xAxis.setAvoidFirstLastClipping(true); // Avoid clipping of first and last labels

// Adjust chart for better alignment
                    lineChart.setExtraBottomOffset(30f); // Add extra space for rotated labels
                    lineChart.getAxisRight().setEnabled(false); // Disable right Y-axis (optional)
                    lineChart.getDescription().setEnabled(false); // Remove chart description
                    lineChart.setData(new LineData(lineDataSet));
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing CourseRegistration", e);
                }
            } else {
                Log.e(TAG, "No response from API");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch CourseRegistration", throwable);
            return null;
        });
    }



    private void fetchRegistrationFromAPI() throws ExecutionException, InterruptedException {
        String query = "active=eq.true&payment_status=eq.unpaid";

        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("CourseRegistration", query);
        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "CourseRegistration?" + query;
        Log.d(TAG, "Request URL: " + url);
        Log.d(TAG, "Supabase URL: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
        future.get();

        future.thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "API Response: " + response);
                try {

                    TextView teacherTextView = findViewById(R.id.tv_registration_count);
                    JSONArray jsonArray = new JSONArray(response);
                    teacherTextView.setText("Số đăng ký: " + jsonArray.length());
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing courses", e);
                }
            } else {
                Log.e(TAG, "No response from API");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch teachers", throwable);
            return null;
        });
    }
    private void fetchTeacherFromAPI() throws ExecutionException, InterruptedException {
        String query = "select=role&role=eq.teacher&active=eq.true";

        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("User", query);
        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "User?" + query;
        Log.d(TAG, "Request URL: " + url);
        Log.d(TAG, "Supabase URL: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
        future.get();

        future.thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "API Response: " + response);
                try {

                    TextView teacherTextView = findViewById(R.id.tv_teacher_count);
                    JSONArray jsonArray = new JSONArray(response);
                    teacherTextView.setText("Số giảng viên: " + jsonArray.length());
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing courses", e);
                }
            } else {
                Log.e(TAG, "No response from API");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch teachers", throwable);
            return null;
        });
    }
    private void fetchCourseFromAPI() throws ExecutionException, InterruptedException {
        String query = "select=name&status=neq.finished";

        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("Course", query);
        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "Course?" + query;
        Log.d(TAG, "Request URL: " + url);
        Log.d(TAG, "Supabase URL: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
        future.get();

        future.thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "API Response: " + response);
                try {

                    TextView courseTextView = findViewById(R.id.tv_course_count);
                    JSONArray jsonArray = new JSONArray(response);
                    courseTextView.setText("Số khóa học: " + jsonArray.length());
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
    private void fetchStudentFromAPI() throws ExecutionException, InterruptedException {
        String query = "select=role&role=eq.student";

        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("User", query);
        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "User?" + query;
        Log.d(TAG, "Request URL: " + url);
        Log.d(TAG, "Supabase URL: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
        future.get();

        future.thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "API Response: " + response);
                try {

                    TextView studentTextView = findViewById(R.id.tv_student_count);
                    JSONArray jsonArray = new JSONArray(response);
                    studentTextView.setText("Số học sinh: " + jsonArray.length());
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing courses", e);
                }
            } else {
                Log.e(TAG, "No response from API");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch students", throwable);
            return null;
        });
    }
}
