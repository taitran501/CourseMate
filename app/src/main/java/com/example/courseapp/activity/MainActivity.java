package com.example.courseapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseapp.R;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private ConstraintLayout constraintLayout2;
    private ConstraintLayout constraintLayout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Thiết lập padding để tránh đè lên hệ thống thanh
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tham chiếu các ConstraintLayout từ layout
        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout2 = findViewById(R.id.constraintLayout2);
        constraintLayout3 = findViewById(R.id.constraintLayout3);

        // Thiết lập sự kiện click để chuyển sang courselist Activity
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCourseList();
            }
        });

        constraintLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCourseList();
            }
        });

        constraintLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCourseList();
            }
        });
    }

    // Phương thức để mở courselist Activity
    private void openCourseList() {
        Intent intent = new Intent(MainActivity.this, courselist.class);
        startActivity(intent);
    }
}
