package com.example.coursemate.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursemate.NetworkUtils;
import com.example.coursemate.OverviewActivity;
import com.example.coursemate.R;
import com.example.coursemate.fragment.StudentDashboardActivity;
// import com.example.coursemate.teacher.TeacherDashboardActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class Login extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvSignUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo Toolbar và thiết lập làm ActionBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Đăng nhập");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Khởi tạo các view
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);

        tvForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, ForgotPassword.class);
            startActivity(intent);
        });

        tvSignUpLink.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> loginUser());



        // Sự kiện ẩn/hiện mật khẩu cho cả etPassword và etConfirmPassword
        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableEnd = etPassword.getCompoundDrawables()[2] != null ? 2 : -1;
                    if (drawableEnd != -1 && event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[drawableEnd].getBounds().width())) {
                        togglePasswordVisibility(etPassword);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    // Hàm để ẩn/hiện mật khẩu
    private void togglePasswordVisibility(EditText passwordField) {
        if (passwordField.getTransformationMethod() instanceof PasswordTransformationMethod) {
            passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_closed, 0);
        } else {
            passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_open, 0);
        }
        passwordField.setSelection(passwordField.getText().length()); // Để con trỏ ở cuối văn bản
    }


    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo headers và URL Supabase
        Map<String, String> headers = Map.of(
                "Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndkcWJ6dGtnc2VsaGVvZG9tbnNlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzA4OTM5MzUsImV4cCI6MjA0NjQ2OTkzNX0.L6lu74ufICA5kvwl3UhHSRejwOzRumunQa2oakltS7c",
                "apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndkcWJ6dGtnc2VsaGVvZG9tbnNlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzA4OTM5MzUsImV4cCI6MjA0NjQ2OTkzNX0.L6lu74ufICA5kvwl3UhHSRejwOzRumunQa2oakltS7c"
        );
        String baseUrl = "https://wdqbztkgselheodomnse.supabase.co/rest/v1/";

        NetworkUtils networkUtils = new NetworkUtils(baseUrl, headers);

        // Truy vấn để kiểm tra tài khoản
        String query = "select=id,username,password,role&username=eq." + email + "&password=eq." + password;

        networkUtils.select("User", query).thenAccept(response -> {
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        JSONObject user = jsonArray.getJSONObject(0);
                        String role = user.optString("role", "");

                        // Điều hướng theo vai trò của người dùng
                        Intent intent;
                        switch (role) {
                            case "admin":
                                intent = new Intent(Login.this, OverviewActivity.class);
                                break;
                            case "student":
                                intent = new Intent(Login.this, StudentDashboardActivity.class);
                                break;
//                            case "teacher":
//                                intent = new Intent(Login.this, TeacherDashboardActivity.class);
//                                break;
                            default:
                                runOnUiThread(() -> Toast.makeText(this, "Vai trò không hợp lệ", Toast.LENGTH_SHORT).show());
                                return;
                        }
                        startActivity(intent);
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show());
                    }
                } catch (Exception e) {
                    Log.e("Login", "Error parsing user details", e);
                    runOnUiThread(() -> Toast.makeText(this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show());
                }
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Không thể kết nối tới máy chủ", Toast.LENGTH_SHORT).show());
            }
        }).exceptionally(throwable -> {
            Log.e("Login", "Failed to fetch user details", throwable);
            runOnUiThread(() -> Toast.makeText(this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show());
            return null;
        });
    }
}
