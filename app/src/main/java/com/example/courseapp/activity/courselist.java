package com.example.courseapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseapp.R;
import com.example.courseapp.adapter.courseadapter;
import com.example.courseapp.domain.coursedomain;

import java.util.ArrayList;

public class courselist extends AppCompatActivity {

    private RecyclerView recyclerView;
    private courseadapter adapter;
    private ArrayList<coursedomain> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_courselist);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);

        // Tạo dữ liệu mẫu cho courseList
        courseList = new ArrayList<>();
        courseList.add(new coursedomain(0.0, "AI and Machine Learning", R.drawable.ic_1));
        courseList.add(new coursedomain(0.0, "Web Development", R.drawable.ic_2));
        courseList.add(new coursedomain(0.0, "Data Science", R.drawable.ic_3));

        Log.d("CourseList", "Số lượng khóa học: " + courseList.size());

        if (courseList.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu để hiển thị", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thiết lập Adapter và LayoutManager cho RecyclerView
        adapter = new courseadapter(courseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Đặt sự kiện click cho các item trong RecyclerView
        adapter.setOnItemClickListener(course -> {
            Toast.makeText(this, "Clicked on: " + course.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }
}
