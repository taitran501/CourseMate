package com.example.coursemate.course_mainpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursemate.R;

public class CoursePaymentActivity extends AppCompatActivity {

    private EditText etFullName, etPhoneNumber, etEmail;
    private TextView tvCourseName, tvTotalPrice, tvPaymentMethod;
    private Button btnPay;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Ánh xạ các thành phần giao diện
        etFullName = findViewById(R.id.etFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmail = findViewById(R.id.etEmail);
        tvCourseName = findViewById(R.id.tvCourseName);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        btnPay = findViewById(R.id.btn_pay);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Thiết lập Toolbar làm ActionBar
        setSupportActionBar(toolbar);

        // Thiết lập tiêu đề và nút quay lại trên Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thanh toán");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Sự kiện click vào nút quay lại
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Nhận dữ liệu từ Intent
        String courseName = getIntent().getStringExtra("courseName");
        String coursePrice = getIntent().getStringExtra("coursePrice");

        // Hiển thị dữ liệu trong TextView
        tvCourseName.setText("Khóa học: " + courseName);
        tvTotalPrice.setText("Tổng số tiền: " + coursePrice + " VNĐ");
        tvPaymentMethod.setText("Hình thức thanh toán: ");

        // Xử lý khi bấm nút Thanh Toán
        btnPay.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            String email = etEmail.getText().toString();

            if (fullName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
                Toast.makeText(CoursePaymentActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Xử lý thanh toán tại đây
                Toast.makeText(CoursePaymentActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

