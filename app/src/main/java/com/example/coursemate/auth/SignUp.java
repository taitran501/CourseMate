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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursemate.NetworkUtils;
import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;

import org.json.JSONArray;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private EditText etEmail, etFullName, etPhoneNumber, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private TextView tvSignInLink;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Khởi tạo Toolbar và thiết lập làm ActionBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập tiêu đề và nút quay lại trên Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Đăng ký");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Khởi tạo các view
        etEmail = findViewById(R.id.etEmail);
        etFullName = findViewById(R.id.etFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignInLink = findViewById(R.id.tvSignInLink);

        tvSignInLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
            finish();
        });

        btnSignUp.setOnClickListener(v -> signUpUser());

        // Sự kiện ẩn/hiện mật khẩu
        etPassword.setOnTouchListener(this::handlePasswordVisibility);
        etConfirmPassword.setOnTouchListener(this::handlePasswordVisibility);
    }

    private boolean handlePasswordVisibility(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            EditText passwordField = (EditText) v;
            int drawableEnd = passwordField.getCompoundDrawables()[2] != null ? 2 : -1;
            if (drawableEnd != -1 && event.getRawX() >= (passwordField.getRight() - passwordField.getCompoundDrawables()[drawableEnd].getBounds().width())) {
                togglePasswordVisibility(passwordField);
                return true;
            }
        }
        return false;
    }

    private void togglePasswordVisibility(EditText passwordField) {
        if (passwordField.getTransformationMethod() instanceof PasswordTransformationMethod) {
            passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        passwordField.setSelection(passwordField.getText().length());
    }

    private void signUpUser() {
        String email = etEmail.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (email.isEmpty() || fullName.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            String message = "Vui lòng điền đầy đủ thông tin";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.d(TAG, message);
            return;
        }

        if (!password.equals(confirmPassword)) {
            String message = "Mật khẩu và xác nhận mật khẩu không khớp";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.d(TAG, message);
            return;
        }

//        if (!isPasswordStrong(password)) {
//            String message = "Mật khẩu phải chứa ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số.";
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//            Log.d(TAG, message);
//            return;
//        }

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("email", email);
            requestBody.put("password", password);

            // Authentication API URL
            String authUrl = "/signup";

            // Gọi API AUTH
            NetworkUtils authNetworkUtils = SupabaseClientHelper.getAuthNetworkUtils();
            authNetworkUtils.insert(authUrl, requestBody.toString()).thenRun(() -> {
                Log.d(TAG, "Người dùng đã được đăng ký vào Supabase Authentication");

                // Sau khi đăng ký thành công, đồng bộ vào bảng User
                syncWithUserTable(email, password, fullName, phoneNumber);
            }).exceptionally(throwable -> {
                Log.e(TAG, "Lỗi khi đăng ký người dùng với Supabase Authentication", throwable);
                runOnUiThread(() -> Toast.makeText(this, "Lỗi khi đăng ký tài khoản", Toast.LENGTH_SHORT).show());
                return null;
            });
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi chuẩn bị dữ liệu đăng ký", e);
        }
    }

    private void syncWithUserTable(String email, String password, String fullName, String phoneNumber) {
        try {
            JSONObject partnerRequestBody = new JSONObject();
            partnerRequestBody.put("name", fullName);
            partnerRequestBody.put("phone", phoneNumber);
            partnerRequestBody.put("email", email);

            // Gọi API REST cho bảng Partner
            NetworkUtils networkUtils = SupabaseClientHelper.getNetworkUtils();
            networkUtils.insert("Partner", partnerRequestBody.toString()).thenRun(() -> {
                String queryPartnerId = "select=id&name=eq." + fullName + "&phone=eq." + phoneNumber + "&email=eq." + email;

                // Truy vấn Partner để lấy Partner ID
                networkUtils.select("Partner", queryPartnerId).thenAccept(partnerResponse -> {
                    try {
                        JSONArray partnerArray = new JSONArray(partnerResponse);

                        if (partnerArray.length() == 0) {
                            String message = "Không tìm thấy Partner tương ứng";
                            Log.e(TAG, message);
                            runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
                            return;
                        }

                        JSONObject partner = partnerArray.getJSONObject(0);
                        String partnerId = partner.getString("id");

                        // Đồng bộ dữ liệu với bảng User
                        JSONObject userRequestBody = new JSONObject();
                        userRequestBody.put("username", email);
                        userRequestBody.put("password", password);
                        userRequestBody.put("role", "student");
                        userRequestBody.put("partner_id", partnerId);

                        networkUtils.insert("User", userRequestBody.toString()).thenRun(() -> {
                            Log.d(TAG, "Người dùng đã được đồng bộ vào bảng User");

                            runOnUiThread(() -> {
                                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this, Login.class));
                                finish();
                            });
                        }).exceptionally(throwable -> {
                            Log.e(TAG, "Lỗi khi đồng bộ người dùng vào bảng User", throwable);
                            return null;
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Lỗi xử lý kết quả Partner", e);
                        runOnUiThread(() -> Toast.makeText(this, "Lỗi khi xử lý Partner", Toast.LENGTH_SHORT).show());
                    }
                }).exceptionally(throwable -> {
                    Log.e(TAG, "Lỗi truy vấn Partner", throwable);
                    runOnUiThread(() -> Toast.makeText(this, "Lỗi khi truy vấn Partner", Toast.LENGTH_SHORT).show());
                    return null;
                });
            }).exceptionally(throwable -> {
                Log.e(TAG, "Lỗi khi thêm đối tác (Partner)", throwable);
                runOnUiThread(() -> Toast.makeText(this, "Lỗi khi thêm đối tác", Toast.LENGTH_SHORT).show());
                return null;
            });
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi chuẩn bị đồng bộ vào bảng User", e);
        }
    }


//    private boolean isPasswordStrong(String password) {
//        return password.length() >= 8 &&
//                password.matches(".*[A-Z].*") &&
//                password.matches(".*[a-z].*") &&
//                password.matches(".*\\d.*") &&
//                password.matches(".*[@#$%^&+=!].*");
//    }

}
