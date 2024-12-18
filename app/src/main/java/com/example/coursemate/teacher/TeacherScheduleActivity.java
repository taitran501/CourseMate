// TeacherScheduleActivity.java
package com.example.coursemate.teacher;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.model.Schedule;
import com.example.coursemate.model.Course;
import com.example.coursemate.model.Classroom;
import com.example.coursemate.adapter.ScheduleAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeacherScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTeacherSchedule;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_schedule);

        // Khởi tạo RecyclerView
        recyclerViewTeacherSchedule = findViewById(R.id.recyclerViewTeacherSchedule);
        recyclerViewTeacherSchedule.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách Schedule
        scheduleList = new ArrayList<>();

        // Khởi tạo Adapter
        scheduleAdapter = new ScheduleAdapter(scheduleList, this);
        recyclerViewTeacherSchedule.setAdapter(scheduleAdapter);

//        // Tải dữ liệu mẫu
//        loadSampleData();
    }

//    private void loadSampleData() {
//        // Tạo các đối tượng Course mẫu
//        Course course1 = new Course(1, "Lập trình Android", "Khóa học Android cơ bản", 30, "ongoing", "2024-09-01", "2024-12-31");
//        Course course2 = new Course(2, "Cơ sở dữ liệu", "Khóa học cơ sở dữ liệu nâng cao", 25, "ongoing", "2024-09-15", "2024-12-15");
//        Course course3 = new Course(3, "Lập trình Web", "Khóa học lập trình Web cơ bản", 20, "open", "2024-10-01", "2025-01-31");
//
//        // Tạo các đối tượng Classroom mẫu
//        Classroom classroom1 = new Classroom(1, "A101", 30, 15);
//        Classroom classroom2 = new Classroom(2, "B202", 25, 12);
//        Classroom classroom3 = new Classroom(3, "C303", 20, 10);
//
//        // Tạo các đối tượng Schedule mẫu
//        Schedule schedule1 = new Schedule(1, "1", 8.0f, 10.0f, course1, classroom1); // Thứ Hai 8:00 - 10:00
//        Schedule schedule2 = new Schedule(2, "3", 14.5f, 16.5f, course2, classroom2); // Thứ Tư 14:30 - 16:30
//        Schedule schedule3 = new Schedule(3, "5", 9.0f, 11.0f, course3, classroom3); // Thứ Sáu 9:00 - 11:00
//
//        // Thêm các Schedule vào danh sách
//        scheduleList.add(schedule1);
//        scheduleList.add(schedule2);
//        scheduleList.add(schedule3);
//
//        // Cập nhật Adapter
//        scheduleAdapter.notifyDataSetChanged();
//    }
}
