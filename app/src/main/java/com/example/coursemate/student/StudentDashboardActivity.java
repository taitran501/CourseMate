package com.example.coursemate.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coursemate.R;
import com.example.coursemate.SearchActivity;

public class StudentDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_dashboard);

        // Tham chiếu TextView tìm kiếm
        TextView searchTextView = findViewById(R.id.editTextText2);
        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDashboardActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
