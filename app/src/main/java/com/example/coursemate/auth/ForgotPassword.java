package com.example.coursemate.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursemate.NetworkUtils;
import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;

import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";

    private Toolbar toolbar;
    private EditText etEmail;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Khởi tạo Toolbar và thiết lập làm ActionBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập tiêu đề và nút quay lại trên Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Quên mật khẩu");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Sự kiện click vào nút quay lại
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Khởi tạo các view
        etEmail = findViewById(R.id.etEmail);
        btnContinue = findViewById(R.id.btnContinue);

        // Xử lý sự kiện khi nhấn nút "Tiếp tục"
        btnContinue.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Log.w(TAG, "Email field is empty");
                Toast.makeText(ForgotPassword.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Preparing to send password recovery request for email: " + email);

            NetworkUtils authNetworkUtils = SupabaseClientHelper.getAuthNetworkUtils();
            String resetPasswordUrl = "/recover" ; // "/auth/v1/recover"
    
            try {
                JSONObject requestBody = new JSONObject();
                requestBody.put("email", email);
                requestBody.put("redirect_to", "https://taitran501.github.io/ResetPassword/#username=" + email);

                Log.d(TAG, "Request body: " + requestBody.toString());

                authNetworkUtils.insert(resetPasswordUrl, requestBody.toString()).thenRun(() -> {
                    Log.i(TAG, "Password recovery email sent successfully for: " + email);
                    runOnUiThread(() -> Toast.makeText(ForgotPassword.this, "Đã gửi email khôi phục mật khẩu", Toast.LENGTH_SHORT).show());
                }).exceptionally(throwable -> {
                    Log.e(TAG, "Failed to send password recovery email for: " + email, throwable);
                    runOnUiThread(() -> Toast.makeText(ForgotPassword.this, "Lỗi khi gửi email khôi phục mật khẩu", Toast.LENGTH_SHORT).show());
                    return null;
                });
            } catch (Exception e) {
                Log.e(TAG, "Error preparing password recovery request for email: " + email, e);
                Toast.makeText(ForgotPassword.this, "Lỗi khi chuẩn bị yêu cầu gửi email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
