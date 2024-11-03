package com.example.coursemate.course_mainpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursemate.R;

public class CourseDetailActivity extends AppCompatActivity {

    private TextView courseNameTextView, courseDescriptionTextView, coursePriceTextView, courseSlotTextView;
    private Button registerButton;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course1_detail);

        // Khởi tạo các TextView và Button
        courseNameTextView = findViewById(R.id.courseNameTextView);
        courseDescriptionTextView = findViewById(R.id.courseDescriptionTextView);
        coursePriceTextView = findViewById(R.id.coursePriceTextView);
        courseSlotTextView = findViewById(R.id.courseSlotTextView);
        registerButton = findViewById(R.id.registerButton);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String courseName = intent.getStringExtra("courseName");
        String courseDescription = intent.getStringExtra("courseDescription");
        String coursePrice = intent.getStringExtra("coursePrice");
        String courseSlot = intent.getStringExtra("courseSlot");

        // Đặt thông tin chi tiết cho các TextView
        if (courseName != null) {
            courseNameTextView.setText(courseName);
        }
        if (courseDescription != null) {
            courseDescriptionTextView.setText(courseDescription);
        }
        if (coursePrice != null) {
            coursePriceTextView.setText(coursePrice + " VNĐ");
        }
        if (courseSlot != null) {
            courseSlotTextView.setText(courseSlot + " slots");
        }

        // Xử lý sự kiện khi bấm nút Đăng Ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(CourseDetailActivity.this, CoursePaymentActivity.class);
                paymentIntent.putExtra("courseName", courseName);
                paymentIntent.putExtra("coursePrice", coursePrice);
                startActivity(paymentIntent);
            }
        });

        // Thiết lập Toolbar làm ActionBar
        setSupportActionBar(toolbar);

        // Thiết lập tiêu đề và nút quay lại trên Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thông tin khoá học");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Sự kiện click vào nút quay lại
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
