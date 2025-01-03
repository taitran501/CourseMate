package com.example.coursemate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.model.Schedule1;

import java.util.List;

public class StudentCourseDashboardAdapter extends RecyclerView.Adapter<StudentCourseDashboardAdapter.CourseViewHolder> {

    private final List<Schedule1> courseList;

    public StudentCourseDashboardAdapter(List<Schedule1> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_student_dashboard, parent, false); // Đảm bảo layout tồn tại
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Schedule1 course = courseList.get(position);

        holder.courseName.setText(course.getCourseName());
        holder.courseDescription.setText(course.getDescription() != null ? course.getDescription() : "Không có mô tả");
        holder.courseDate.setText("Ngày: " + course.getStartDate() + " - " + course.getEndDate());
        holder.courseTime.setText("Thời gian: " + course.getStartTime() + " - " + course.getEndTime());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseDescription, courseDate, courseTime;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name_dashboard);
            courseDescription = itemView.findViewById(R.id.course_description_dashboard);
            courseDate = itemView.findViewById(R.id.course_date_dashboard);
            courseTime = itemView.findViewById(R.id.course_time_dashboard);
        }
    }
}
