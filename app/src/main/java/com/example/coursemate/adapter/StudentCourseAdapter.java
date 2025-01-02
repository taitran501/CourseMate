package com.example.coursemate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.model.Course;

import java.util.List;

public class StudentCourseAdapter extends RecyclerView.Adapter<StudentCourseAdapter.StudentCourseViewHolder> {

    private final List<Course> courseList;
    private final List<String> registrationDates;

    public StudentCourseAdapter(List<Course> courseList, List<String> registrationDates) {
        this.courseList = courseList;
        this.registrationDates = registrationDates;
    }

    @NonNull
    @Override
    public StudentCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_student, parent, false);
        return new StudentCourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentCourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        String registrationDate = registrationDates.get(position);

        holder.courseName.setText(course.getName());
        holder.courseDescription.setText(course.getDescription() != null ? course.getDescription() : "Không có mô tả");
        holder.registrationDate.setText("Ngày đăng ký: " + registrationDate);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class StudentCourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseDescription, registrationDate;

        public StudentCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name_student);
            courseDescription = itemView.findViewById(R.id.course_description_student);
            registrationDate = itemView.findViewById(R.id.course_registration_date_student);
        }
    }
}

