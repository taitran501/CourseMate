package com.example.coursemate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnSavePassword;
    private static final String TAG = "ChangePasswordActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Ánh xạ các view
        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSavePassword = findViewById(R.id.btn_save_password);

        // Lấy username từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (username.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin tài khoản!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Xử lý sự kiện nút "Lưu thay đổi"
        btnSavePassword.setOnClickListener(v -> changePassword(username));
    }

    private void changePassword(String username) {
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi yêu cầu cập nhật mật khẩu
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("password", newPassword);

            String query = "username=eq." + username;
            SupabaseClientHelper.getNetworkUtils().update("User", query, requestBody.toString())
                    .thenRun(() -> runOnUiThread(() -> {
                        Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }))
                    .exceptionally(throwable -> {
                        Log.e(TAG, "Lỗi khi đổi mật khẩu", throwable);
                        runOnUiThread(() -> Toast.makeText(this, "Đã xảy ra lỗi. Vui lòng thử lại!", Toast.LENGTH_SHORT).show());
                        return null;
                    });

        } catch (Exception e) {
            Log.e(TAG, "Lỗi tạo request body", e);
        }
    }
}
