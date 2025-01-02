package com.example.coursemate.course_mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.DatabaseHelper;
import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import com.example.coursemate.adapter.AdminCourseAdapter;
import com.example.coursemate.adapter.MainPageCourseAdapter;
import com.example.coursemate.adapter.TeacherAdapter;
import com.example.coursemate.auth.Login;
import com.example.coursemate.model.Course;
import com.example.coursemate.model.Teacher;
import com.example.coursemate.teacher.TeacherDetailActivity;
import com.example.coursemate.teacher.TeacherList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        try {
            fetchCoursesFromAPI();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Log.d(TAG, "Fetching courses from API: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
    }


//
//    private RecyclerView courseRecyclerView;
//    private AdminCourseAdapter adminCourseAdapter;
//    private DatabaseHelper databaseHelper;
//    private List<Course> courseList;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_course_list);
//
//
//
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("Khoá học");
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        try {
//            loadCourses();
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void loadCourses() throws ExecutionException, InterruptedException {
//        String query = "select=id,name,description,status";
//
//        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("Course", query);
//        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "Course?" + query;
//        Log.d(TAG, "Request URL: " + url);
//        Log.d(TAG, "Supabase URL: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
//       future.get();
//
//        future.thenAccept(response -> {
//            if (response != null) {
//                Log.d(TAG, "API Response: " + response);
//                try {
//                    ArrayList<Course> fetchedCourses = parseCoursesFromJson(response);
//                    Log.d(TAG, "Fetched " + fetchedCourses.size() + " courses");
//                    courseRecyclerView = findViewById(R.id.courseRecyclerView);
//                    courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//                    adminCourseAdapter = new AdminCourseAdapter(courseList, this);
//                    courseRecyclerView.setAdapter(adminCourseAdapter);
//
//
//                } catch (Exception e) {
//                    Log.e(TAG, "Error parsing courses", e);
//                }
//            } else {
//                Log.e(TAG, "No response from API");
//            }
//        }).exceptionally(throwable -> {
//            Log.e(TAG, "Failed to fetch courses", throwable);
//            return null;
//        });
//    }




//    private RecyclerView recyclerView;
//    private TeacherAdapter adapter;
//    private List<Teacher> teacherList;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_teacher_list);
//
//        try {
//            fetchTeacherFromAPI();
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    private void fetchTeacherFromAPI() throws ExecutionException, InterruptedException {
//        String query = "select=id,Partner(name)&role=eq.teacher&active=eq.true";
//
//        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("User", query);
//        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "User?" + query;
//        Log.d(TAG, "Request URL: " + url);
//        Log.d(TAG, "Supabase URL: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
//        future.get();
//
//        future.thenAccept(response -> {
//            if (response != null) {
//                Log.d(TAG, "API Response: " + response);
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//
//                    Log.d(TAG, "Fetched " + jsonArray.length() + " UserTeacher");
//                    Log.d(TAG, "Fetched " + jsonArray );
//
//
//                    teacherList = new ArrayList<>();
//                    for (int i =0; i<jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                        String name = jsonObject.getJSONObject("Partner").getString("name");
//                        teacherList.add(new Teacher(name, R.drawable.ic_avatar_placeholder, jsonObject.getString("id")  ));
//                    }
//                    // Add more teachers as needed
//
//                    recyclerView = findViewById(R.id.teacher_recycler_view);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//                    adapter = new TeacherAdapter(teacherList, new TeacherAdapter.OnTeacherClickListener() {
//                        @Override
//                        public void onTeacherClick(Teacher teacher) {
//                            Intent intent = new Intent(CourseListMainPage.this, TeacherDetailActivity.class);
//                            intent.putExtra("teacher_name", teacher.getName());
//                            intent.putExtra("user_id", teacher.getId());
//                            intent.putExtra("teacher_avatar", teacher.getAvatarResId());
//                            startActivity(intent);
//                        }
//                    });
//
//                    recyclerView.setAdapter(adapter);
//
//                } catch (Exception e) {
//                    Log.e(TAG, "Error parsing UserTeacher", e);
//                }
//            } else {
//                Log.e(TAG, "No response from API");
//            }
//        }).exceptionally(throwable -> {
//            Log.e(TAG, "Failed to fetch UserTeacher", throwable);
//            return null;
//        });
//    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.overview_dashboard);
//
//        // Initialize charts
//
//        // Setup Bar Chart
//        try {
//            fetchCourseRegistrationsFromAPI();
//            fetchAmountCourseRegistrationsFromAPI();
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

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

    /**
     * Gọi API Supabase để lấy danh sách khóa học.
     */
    private void fetchCoursesFromAPI() throws ExecutionException, InterruptedException {
        String query = "select=*";

        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("Course", query);
        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "Course?" + query;
        Log.d(TAG, "Request URL: " + url);
        Log.d(TAG, "Supabase URL: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
//       future.get();

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
