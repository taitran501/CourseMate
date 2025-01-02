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
import com.example.coursemate.PaymentActivity;
import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import com.example.coursemate.fragment.StudentDashboardActivity;
import com.example.coursemate.teacher.TeacherDashboardActivity;

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

        btnLogin.setOnClickListener(view -> {
            String returnTo = getIntent().getStringExtra("returnTo");

            if ("payment".equals(returnTo)) {
                // Nếu quay lại từ giao diện thanh toán, sử dụng logic dành riêng
                handleLoginForPayment();
            } else {
                // Nếu không phải từ giao diện thanh toán, điều hướng dựa trên role
                handleLoginByRole();
            }
        });


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

    private void handleLoginForPayment() {
        loginUser(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
            String role = sharedPreferences.getString("role", "");

            if (!"student".equals(role)) {
                // Ngăn chặn nếu role không phải là student
                Toast.makeText(this, "Chỉ tài khoản Student mới được phép thanh toán.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Không cho phép tài khoản với vai trò '" + role + "' thực hiện thanh toán.");
                return;
            }

            // Nếu role là student, chuyển đến giao diện thanh toán
            Intent intent = getIntent();
            String courseId = intent.getStringExtra("courseId");
            String courseName = intent.getStringExtra("courseName");
            String coursePrice = intent.getStringExtra("coursePrice");

            Intent paymentIntent = new Intent(Login.this, PaymentActivity.class);
            paymentIntent.putExtra("courseId", courseId);
            paymentIntent.putExtra("courseName", courseName);
            paymentIntent.putExtra("coursePrice", coursePrice);
            startActivity(paymentIntent);
            finish();
        });
    }


    private void handleLoginByRole() {
        loginUser(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
            String role = sharedPreferences.getString("role", "");

            Intent intentRole;
            if ("admin".equals(role)) {
                intentRole = new Intent(Login.this, OverviewActivity.class);
                Log.d(TAG, "Chuyển hướng đến màn hình admin");
            } else if ("student".equals(role)) {
                intentRole = new Intent(Login.this, StudentDashboardActivity.class);
                Log.d(TAG, "Chuyển hướng đến màn hình student");
            } else if ("teacher".equals(role)) {
                intentRole = new Intent(Login.this, TeacherDashboardActivity.class);
                Log.d(TAG, "Chuyển hướng đến màn hình teacher");
            }
            else {
                String message = "Vai trò không hợp lệ";
                runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
                Log.d(TAG, message);
                return;
            }

            startActivity(intentRole);
            finish();
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

    private void loginUser(Runnable onSuccessCallback) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
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
                        saveUserSession(user.optString("username"), user.optString("role"), user.optString("partner_id"));
                        runOnUiThread(onSuccessCallback);
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show());
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(this, "Lỗi xử lý phản hồi từ máy chủ", Toast.LENGTH_SHORT).show());
                }
            }
        }).exceptionally(throwable -> {
            runOnUiThread(() -> Toast.makeText(this, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show());
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

        // Lấy user_id từ bảng User sau khi lưu username
        fetchAndSaveUserId(username);
    }

    private void fetchAndSaveUserId(String username) {
        String query = "select=id&username=eq." + username;

        SupabaseClientHelper.getNetworkUtils().select("User", query).thenAccept(response -> {
            if (response != null && !response.isEmpty()) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        String userId = jsonArray.getJSONObject(0).getString("id");

                        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_id", userId); // Lưu user_id vào SharedPreferences
                        editor.apply();

                        Log.d(TAG, "Đã lưu user_id: " + userId);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing user_id", e);
                }
            } else {
                Log.e(TAG, "Không tìm thấy user_id cho username: " + username);
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Lỗi khi lấy user_id", throwable);
            return null;
        });
    }


}
