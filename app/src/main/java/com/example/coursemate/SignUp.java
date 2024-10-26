package com.example.coursemate;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Quay lại activity trước đó
            }
        });

        // Khởi tạo các view
        etEmail = findViewById(R.id.etEmail);
        etFullName = findViewById(R.id.etFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignInLink = findViewById(R.id.tvSignInLink);

        // Sự kiện click vào "Đăng nhập ngay"
        tvSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến Login activity
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish(); // Đóng SignUp activity
            }
        });

        // Xử lý sự kiện nhấn nút Đăng ký
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });

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

        etConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableEnd = etConfirmPassword.getCompoundDrawables()[2] != null ? 2 : -1;
                    if (drawableEnd != -1 && event.getRawX() >= (etConfirmPassword.getRight() - etConfirmPassword.getCompoundDrawables()[drawableEnd].getBounds().width())) {
                        togglePasswordVisibility(etConfirmPassword);
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

    // Hàm xử lý đăng ký người dùng
    private void signUpUser() {
        String email = etEmail.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Kiểm tra thông tin đầu vào (có thể thêm các kiểm tra khác tại đây)
        if (email.isEmpty() || fullName.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Xử lý đăng ký với backend (ví dụ: Firebase, REST API)
        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
    }
}