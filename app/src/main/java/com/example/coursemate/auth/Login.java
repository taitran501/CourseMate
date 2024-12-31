package com.example.coursemate.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursemate.NetworkUtils;
import com.example.coursemate.OverviewActivity;
import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import com.example.coursemate.fragment.StudentDashboardActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

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

        // Sự kiện ẩn/hiện mật khẩu
        etPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableEnd = etPassword.getCompoundDrawables()[2] != null ? 2 : -1;
                if (drawableEnd != -1 && event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[drawableEnd].getBounds().width())) {
                    togglePasswordVisibility(etPassword);
                    return true;
                }
            }
            return false;
        });
    }

    // Hàm để ẩn/hiện mật khẩu
    private void togglePasswordVisibility(EditText passwordField) {
        if (passwordField.getTransformationMethod() instanceof PasswordTransformationMethod) {
            passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        passwordField.setSelection(passwordField.getText().length());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            String message = "Vui lòng nhập đầy đủ thông tin";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Trường thông tin bị bỏ trống: email=" + email + ", password=" + password);
            return;
        }

        NetworkUtils networkUtils = SupabaseClientHelper.getNetworkUtils();
        String query = "select=id,username,password,role,partner_id&username=eq." + email + "&password=eq." + password;

        networkUtils.select("User", query).thenAccept(response -> {
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        JSONObject user = jsonArray.getJSONObject(0);
                        String username = user.optString("username", "");
                        String role = user.optString("role", "");
                        String partnerId = user.optString("partner_id", null);

                        Log.d(TAG, "Đăng nhập thành công: username=" + username + ", role=" + role + ", partner_id=" + partnerId);

                        if (partnerId == null) {
                            Log.e(TAG, "partner_id is null for username: " + username);
                            runOnUiThread(() -> Toast.makeText(this, "Lỗi: partner_id chưa được gán", Toast.LENGTH_SHORT).show());
                            return;
                        }

                        // Lưu thông tin đăng nhập vào SharedPreferences
                        saveUserSession(username, role, partnerId);

                        Intent intent;
                        if (role.equals("admin")) {
                            intent = new Intent(Login.this, OverviewActivity.class);
                            Log.d(TAG, "Chuyển hướng đến màn hình admin");
                        } else if (role.equals("student")) {
                            intent = new Intent(Login.this, StudentDashboardActivity.class);
                            Log.d(TAG, "Chuyển hướng đến màn hình student");
                        } else {
                            String message = "Vai trò không hợp lệ";
                            runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
                            Log.d(TAG, message);
                            return;
                        }

                        startActivity(intent);
                        finish();
                    } else {
                        String message = "Thông tin đăng nhập không chính xác";
                        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
                        Log.d(TAG, "Đăng nhập thất bại: Tài khoản hoặc mật khẩu không đúng, email=" + email);
                    }
                } catch (Exception e) {
                    String message = "Lỗi xử lý thông tin người dùng";
                    Log.e(TAG, message, e);
                    runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
                }
            } else {
                String message = "Không thể kết nối tới máy chủ";
                runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
                Log.d(TAG, "Lỗi kết nối tới máy chủ Supabase");
            }
        }).exceptionally(throwable -> {
            String message = "Lỗi kết nối khi truy vấn Supabase";
            Log.e(TAG, message, throwable);
            runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
            return null;
        });
    }

    // Hàm lưu thông tin đăng nhập vào SharedPreferences
    private void saveUserSession(String username, String role, String partnerId) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("role", role);
        editor.putString("partner_id", partnerId);
        editor.apply();

        Log.d(TAG, "Đã lưu thông tin user session: username=" + username + ", role=" + role + ", partner_id=" + partnerId);
    }
}
