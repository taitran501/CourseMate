package com.example.coursemate.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursemate.OverviewActivity;
import com.example.coursemate.R;
import com.example.coursemate.student.StudentLogin;

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

        // Thiết lập tiêu đề và nút quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Đăng nhập");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Sự kiện click vào nút quay lại
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Khởi tạo các view
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);

        // Sự kiện khi nhấn vào "Quên mật khẩu"
        tvForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, ForgotPassword.class);
            startActivity(intent);
        });

        // Sự kiện khi nhấn vào "Đăng ký ngay"
        tvSignUpLink.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });

        // Sự kiện khi nhấn vào nút "Đăng nhập"
        btnLogin.setOnClickListener(view -> loginUser());
    }

    // Phương thức xử lý đăng nhập
    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Kiểm tra đầu vào
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra tài khoản mẫu
        if (email.equals("admin") && password.equals("1")) {
            // Đăng nhập với tài khoản admin, chuyển sang OverviewActivity
            Intent intent = new Intent(Login.this, OverviewActivity.class);
            startActivity(intent);
        } else if (email.equals("student") && password.equals("1")) {
            // Đăng nhập với tài khoản teacher, chuyển sang MainDashboardActivity
            Intent intent = new Intent(Login.this, StudentLogin.class);
            startActivity(intent);
        } else {
            // Thông báo lỗi nếu thông tin đăng nhập không hợp lệ
            Toast.makeText(this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show();
        }
    }
}
