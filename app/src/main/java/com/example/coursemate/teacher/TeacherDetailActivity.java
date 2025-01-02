package com.example.coursemate.teacher;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coursemate.OverviewActivity;
import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import com.example.coursemate.adapter.TeacherAdapter;
import com.example.coursemate.auth.ForgotPassword;
import com.example.coursemate.course_mainpage.CourseListMainPage;
import com.example.coursemate.model.Teacher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TeacherDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private static final String TAG = "TeacherDetailActivity";
    private String partnerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thông tin giáo viên");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        try {
            fetchTeacherFromAPI();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Button editBtn = findViewById(R.id.btn_edit);
        Button eraseBtn = findViewById(R.id.btn_erase);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    patchTeacherFromAPI();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        eraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    deleteTeacherFromAPI();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private void fetchTeacherFromAPI() throws ExecutionException, InterruptedException {
        Intent intent = getIntent();
        String id =  intent.getStringExtra("user_id");
        String query = "select=id,Partner(name,phone,id)&id=eq." + id ;

        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("User", query);
        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "User?" + query;
        Log.d(TAG, "Request URL: " + url);
        Log.d(TAG, "Supabase URL: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
        String query2 = "select=name,status,max_students&teacher_id=eq." +id+ "&order=status.asc";

        CompletableFuture<String> future2 = SupabaseClientHelper.getNetworkUtils().select("Course", query2);
        future.get();


        future.thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "API Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Log.d(TAG, "Fetched " + jsonArray.length() + " UserTeacher");
                    Log.d(TAG, "Fetched " + jsonArray );

                    TextView teacherNameTextView = findViewById(R.id.et_profile_name);
                    TextView teacherPhoneTextView = findViewById(R.id.et_profile_phone);
                    ImageView teacherAvatarImageView = findViewById(R.id.iv_profile_image);
                    LinearLayout coursesList = findViewById(R.id.course_list);


                    int teacherAvatarResId = intent.getIntExtra("teacher_avatar", -1);
                    teacherNameTextView.setText(jsonObject.getJSONObject("Partner").getString("name"));
                    teacherPhoneTextView.setText(jsonObject.getJSONObject("Partner").getString("phone"));
                    teacherAvatarImageView.setImageResource(teacherAvatarResId);

                    this.partnerId = jsonObject.getJSONObject("Partner").getString("id");



                } catch (Exception e) {
                    Log.e(TAG, "Error parsing UserTeacher", e);
                }
            } else {
                Log.e(TAG, "No response from API");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch UserTeacher", throwable);
            return null;
        });
        LinearLayout parentLayout = findViewById(R.id.course_list); // Parent LinearLayout

        future2.get();
        future2.thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "API Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject course = jsonArray.getJSONObject(i);

                        String courseName = course.getString("name");
                        String progress = course.getString("max_students");
                        String status = course.getString("status");

                        // Create the inner LinearLayout
                        LinearLayout courseLayout = new LinearLayout(this);
                        courseLayout.setOrientation(LinearLayout.HORIZONTAL);
                        courseLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        courseLayout.setGravity(Gravity.CENTER_VERTICAL);

                        // Create TextView for course name
                        TextView courseNameView = new TextView(this);
                        courseNameView.setLayoutParams(new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1 // layout_weight
                        ));
                        courseNameView.setText(courseName);
                        courseNameView.setTextColor(Color.BLACK);
                        courseNameView.setTextSize(16);

                        // Create TextView for progress
                        TextView progressView = new TextView(this);
                        LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                0.1f
                        );
                        progressParams.setMarginEnd(16);
                        progressView.setLayoutParams(progressParams);
                        progressView.setText(progress);
                        progressView.setTextColor(Color.BLACK);
                        progressView.setTextSize(16);

                        // Create TextView for status
                        TextView statusView = new TextView(this);
                        statusView.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        statusView.setText(status);
                        if (status.equals("ongoing")) {
                            statusView.setTextColor(Color.GREEN);
                        }
                        else if (status.equals("open")) {
                            statusView.setTextColor(Color.BLACK);
                        }

                        else {
                            statusView.setTextColor(Color.RED);
                        }
                        statusView.setTextSize(16);

                        // Add TextViews to the LinearLayout
                        courseLayout.addView(courseNameView);
                        courseLayout.addView(progressView);
                        courseLayout.addView(statusView);

                        // Add the LinearLayout to the parent
                        parentLayout.addView(courseLayout);

                    }


                } catch (Exception e) {
                    Log.e(TAG, "Error parsing UserTeacher", e);
                }
            } else {
                Log.e(TAG, "No response from API");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch UserTeacher", throwable);
            return null;
        });
    }
    private void patchTeacherFromAPI() throws ExecutionException, InterruptedException {
        String query = "id=eq."+this.partnerId ;
        EditText et_profile_name = findViewById(R.id.et_profile_name);
        EditText et_profile_phone = findViewById(R.id.et_profile_phone);

        String jsonBody = "{\"name\":\""+et_profile_name.getText().toString()+"\",\"phone\":\""+et_profile_phone.getText().toString()+"\"}";
        CompletableFuture<Void> future = SupabaseClientHelper.getNetworkUtils().update("Partner", query, jsonBody);
        future.get();

        Toast.makeText(TeacherDetailActivity.this, "Teacher details updated successfully", Toast.LENGTH_SHORT).show();


    }
    private void deleteTeacherFromAPI() throws ExecutionException, InterruptedException {
        Intent intent = getIntent();
        String query = "id=eq."+intent.getStringExtra("user_id");

        String jsonBody = "{\"active\":\"false\"}";
        CompletableFuture<Void> future = SupabaseClientHelper.getNetworkUtils().update("User", query, jsonBody);
        future.get();
        Intent intent2 = new Intent(TeacherDetailActivity.this, OverviewActivity.class);
        startActivity(intent2);



    }

}
