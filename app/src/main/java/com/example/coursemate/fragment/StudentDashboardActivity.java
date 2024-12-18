package com.example.coursemate.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.coursemate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_fragment);

        // Set Fragment mặc định khi mở Dashboard
        if (savedInstanceState == null) {
            loadFragment(new FragmentDashboard());
        }

        // Xử lý chuyển đổi giữa các Fragment
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new FragmentDashboard(); // Fragment màn hình chính
                Log.d("StudentDashboardActivity", "Selected Home Fragment");
            } else if (itemId == R.id.nav_course) {
                selectedFragment = new FragmentCourse(); // Fragment khóa học
                Log.d("StudentDashboardActivity", "Selected Course Fragment");
            } else if (itemId == R.id.nav_schedule) {
                selectedFragment = new FragmentSchedule(); // Fragment thời khóa biểu
                Log.d("StudentDashboardActivity", "Selected Schedule Fragment");
            } else if (itemId == R.id.nav_setting) {
                selectedFragment = new FragmentSetting(); // Fragment cài đặt
                Log.d("StudentDashboardActivity", "Selected Setting Fragment");
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_dashboard, fragment); // ID của container
        transaction.commit();
    }
}
