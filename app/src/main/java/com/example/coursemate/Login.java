package com.example.coursemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

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

        // Sự kiện click vào nút quay lại (nếu cần)
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Khởi tạo các view
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);

        // Xử lý sự kiện khi nhấn vào "Quên mật khẩu"
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang ForgotPassword activity
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn vào "Đăng ký ngay" (nếu có)
        tvSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn vào nút "Đăng nhập" (nếu có)
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    // Phương thức xử lý đăng nhập (nếu cần)
    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Kiểm tra đầu vào và thực hiện đăng nhập
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Thực hiện logic đăng nhập với backend hoặc cơ sở dữ liệu

        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        // Chuyển sang Activity tiếp theo nếu cần
    }
}

