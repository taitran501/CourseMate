package com.example.coursemate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.adapter.TeacherAdapter;
import com.example.coursemate.model.CourseRegistration;
import com.example.coursemate.model.Teacher;
import com.example.coursemate.teacher.TeacherDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RegistrationList extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TeacherAdapter adapter;
    private List<CourseRegistration> courseRegistrationList;
    private static final String TAG = "TeacherList";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập tiêu đề và nút quay lại trên Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Duyệt học phí");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        try {
            fetchRegistrationFromAPI();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


//        Button btnSignUp = findViewById(R.id.btnSignUp);
//
//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    createTeacherFromAPI();
//                } catch (ExecutionException e) {
//                    throw new RuntimeException(e);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
    }

    private void fetchRegistrationFromAPI() throws ExecutionException, InterruptedException {
        String query = "select=id,Course(name),amount,User(Partner(name))&active=eq.true&payment_status=eq.unpaid";

        CompletableFuture<String> future = SupabaseClientHelper.getNetworkUtils().select("CourseRegistration", query);
        future.get();
        LinearLayout parentLayout = findViewById(R.id.list); // Your parent LinearLayout in the XML

        future.thenAccept(response -> {
            if (response != null) {
                Log.d(TAG, "API Response: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        // Extract values from the JSON object
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getJSONObject("User").getJSONObject("Partner").getString("name");
                        String courseName = jsonObject.getJSONObject("Course").getString("name");
                        int price = jsonObject.getInt("amount");


                        // Create a horizontal LinearLayout for each row
                        LinearLayout rowLayout = new LinearLayout(this);
                        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));

                        // Add TextViews for Name, Course Name, and Price
                        TextView nameTextView = new TextView(this);
                        nameTextView.setText(name);
                        nameTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        rowLayout.addView(nameTextView);

                        TextView courseTextView = new TextView(this);
                        courseTextView.setText(courseName);
                        courseTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        rowLayout.addView(courseTextView);

                        TextView priceTextView = new TextView(this);
                        priceTextView.setText(String.valueOf(price));
                        priceTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        rowLayout.addView(priceTextView);

                        // Add Approve Button
                        Button approveButton = new Button(this);
                        approveButton.setText("Approve");
                        approveButton.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        approveButton.setOnClickListener(v -> {
                            // Handle Approve button click
                            handleApprove(id);
                        });
                        rowLayout.addView(approveButton);

                        // Add Reject Button
                        Button rejectButton = new Button(this);
                        rejectButton.setText("Reject");
                        rejectButton.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        rejectButton.setOnClickListener(v -> {
                            // Handle Reject button click
                            handleReject(id);
                        });
                        rowLayout.addView(rejectButton);

                        parentLayout.addView(rowLayout);

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

    private void handleApprove(String courseRegistrationId) {
        String query = "id=eq."+courseRegistrationId;
        String jsonBody = "{\"payment_status\": \"paid\"}";
        CompletableFuture<Void> future = SupabaseClientHelper.getNetworkUtils().update("CourseRegistration", query, jsonBody);
        try {
            future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        Toast.makeText(this, "Approved ID: " + courseRegistrationId, Toast.LENGTH_SHORT).show();
        // Additional logic here
    }

    private void handleReject(String courseRegistrationId) {
        String query = "id=eq."+courseRegistrationId;
        String jsonBody = "{\"active\": \"false\"}";
        CompletableFuture<Void> future = SupabaseClientHelper.getNetworkUtils().update("CourseRegistration", query, jsonBody);
        try {
            future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        Toast.makeText(this, "Rejected ID: " + courseRegistrationId, Toast.LENGTH_SHORT).show();
        // Additional logic here
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

