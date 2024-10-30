package com.example.coursemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.RecyclerView.TeacherAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeacherList extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TeacherAdapter adapter;
    private List<Teacher> teacherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

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


        teacherList = new ArrayList<>();
        teacherList.add(new Teacher("Alice Johnson", R.drawable.ic_avatar_placeholder));
        teacherList.add(new Teacher("Bob Smith", R.drawable.ic_avatar_placeholder));
        // Add more teachers as needed

        recyclerView = findViewById(R.id.teacher_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TeacherAdapter(teacherList, new TeacherAdapter.OnTeacherClickListener() {
            @Override
            public void onTeacherClick(Teacher teacher) {
                Intent intent = new Intent(TeacherList.this, TeacherDetailActivity.class);
                intent.putExtra("teacher_name", teacher.getName());
                intent.putExtra("teacher_avatar", teacher.getAvatarResId());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
    }
}

