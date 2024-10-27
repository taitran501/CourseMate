package com.example.coursemate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Khởi tạo các view
        etEmail = findViewById(R.id.etEmail);
        btnContinue = findViewById(R.id.btnContinue);

        // Xử lý sự kiện khi nhấn nút "Tiếp tục"
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotPassword.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // TODO: Xử lý logic gửi email khôi phục mật khẩu

                Toast.makeText(ForgotPassword.this, "Đã gửi email khôi phục mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

