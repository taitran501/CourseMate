package com.example.coursemate.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import com.example.coursemate.course_mainpage.CourseListMainPage;
import com.example.coursemate.model.Teacher;
import com.example.coursemate.adapter.TeacherAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TeacherList extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TeacherAdapter adapter;
    private List<Teacher> teacherList;
    private static final String TAG = "TeacherList";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Thiết lập tiêu đề và nút quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Quản lý giáo viên");
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


        Button btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createTeacherFromAPI();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void fetchTeacherFromAPI() throws ExecutionException, InterruptedException {
        String query = "select=id,Partner(name)&role=eq.teacher&active=eq.true";

        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("User", query);
        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "User?" + query;
        Log.d(TAG, "Request URL: " + url);
        Log.d(TAG, "Supabase URL: " + SupabaseClientHelper.getNetworkUtils().getBaseUrl());
        future.get();

        future.thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "API Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    Log.d(TAG, "Fetched " + jsonArray.length() + " UserTeacher");
                    Log.d(TAG, "Fetched " + jsonArray );


                    teacherList = new ArrayList<>();
                    for (int i =0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String name = jsonObject.getJSONObject("Partner").getString("name");
                        teacherList.add(new Teacher(name, R.drawable.ic_avatar_placeholder, jsonObject.getString("id")  ));
                    }
                    // Add more teachers as needed

                    recyclerView = findViewById(R.id.teacher_recycler_view);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));

                    adapter = new TeacherAdapter(teacherList, new TeacherAdapter.OnTeacherClickListener() {
                        @Override
                        public void onTeacherClick(Teacher teacher) {
                            Intent intent = new Intent(TeacherList.this, TeacherDetailActivity.class);
                            intent.putExtra("teacher_name", teacher.getName());
                            intent.putExtra("user_id", teacher.getId());
                            intent.putExtra("teacher_avatar", teacher.getAvatarResId());
                            startActivity(intent);
                        }
                    });

                    recyclerView.setAdapter(adapter);

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
    private void createTeacherFromAPI() throws ExecutionException, InterruptedException {
        EditText edtEmail = findViewById(R.id.etEmail);
        EditText edtName = findViewById(R.id.etFullName);
        EditText edtPhone = findViewById(R.id.etPhoneNumber);
        String jsonBody = "{\"name\": \"" + edtName.getText().toString() + "\",\"email\": \"" + edtEmail.getText().toString() + "\",\"phone\": \"" + edtPhone.getText().toString()+ "\"}";

        CompletableFuture<Void> future = SupabaseClientHelper.getNetworkUtils().insert("Partner", jsonBody);
        future.get();

        String query = "select=id&email=eq." + edtEmail.getText().toString();
        CompletableFuture<String> future2 = SupabaseClientHelper.getNetworkUtils().select("Partner", query);
        future2.get();

        future2.thenAccept(response -> {
            if (response!= null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String jsonBody2 = "{\"role\": \"teacher\",\"username\": \"" + edtEmail.getText().toString() + "\",\"password\": \"teacherpass1\" , \"partner_id\": \""+jsonObject.getString("id")+"\"}";

                    CompletableFuture<Void> future3 = SupabaseClientHelper.getNetworkUtils().insert("User", jsonBody2);
                    Log.d(TAG, "Inserted UserTeacher: "+ jsonBody2);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });

    }


}

