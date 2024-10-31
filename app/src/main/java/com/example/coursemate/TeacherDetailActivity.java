package com.example.coursemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TeacherDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);

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



        TextView teacherNameTextView = findViewById(R.id.et_profile_name);
        ImageView teacherAvatarImageView = findViewById(R.id.iv_profile_image);

        Intent intent = getIntent();
        String teacherName = intent.getStringExtra("teacher_name");
        int teacherAvatarResId = intent.getIntExtra("teacher_avatar", -1);

        teacherNameTextView.setText(teacherName);
        teacherAvatarImageView.setImageResource(teacherAvatarResId);
    }
}
