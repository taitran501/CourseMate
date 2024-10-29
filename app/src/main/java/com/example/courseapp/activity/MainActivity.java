package com.example.courseapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.courseapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout developingLayout;
    private LinearLayout designingLayout;
    private LinearLayout aiAndMLLayout;
    private LinearLayout exploreLayout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Thiết lập padding để tránh đè lên hệ thống thanh
        View mainView = findViewById(R.id.main);
        if (mainView != null) {  // Kiểm tra null để tránh lỗi
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Tham chiếu các LinearLayout từ layout
        developingLayout = findViewById(R.id.developingLayout);
        designingLayout = findViewById(R.id.designingLayout);
        aiAndMLLayout = findViewById(R.id.aiAndMLLayout);
        exploreLayout = findViewById(R.id.exploreLayout);

        // Thiết lập sự kiện click để chuyển sang courselist Activity
        if (developingLayout != null) {
            developingLayout.setOnClickListener(v -> openCourseList());
        }

        if (designingLayout != null) {
            designingLayout.setOnClickListener(v -> openCourseList());
        }

        if (aiAndMLLayout != null) {
            aiAndMLLayout.setOnClickListener(v -> openCourseList());
        }

        if (exploreLayout != null) {
            exploreLayout.setOnClickListener(v -> openCourseList());
        }
    }

    // Phương thức để mở courselist Activity
    private void openCourseList() {
        Intent intent = new Intent(MainActivity.this, courselist.class);
        startActivity(intent);
    }
}
