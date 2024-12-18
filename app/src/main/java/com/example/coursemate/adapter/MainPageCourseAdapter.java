package com.example.coursemate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.model.Course;
import android.util.Log;

import java.util.ArrayList;

public class MainPageCourseAdapter extends RecyclerView.Adapter<MainPageCourseAdapter.CourseViewHolder> {

    private final ArrayList<Course> courseList;
    private final OnCourseClickListener listener;

    public MainPageCourseAdapter(ArrayList<Course> courseList, OnCourseClickListener listener) {
        this.courseList = courseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_mainpage, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        Log.d("CourseAdapter", "Course Name: " + course.getName() + ", Icon: " + course.getIconResource());

        // Gán dữ liệu từ đối tượng Course vào ViewHolder
        holder.tvCourseName.setText(course.getName());
        holder.imgCourseIcon.setImageResource(course.getIconResource()); // Hiển thị icon

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> listener.onCourseClick(position));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName;
        ImageView imgCourseIcon;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);

            // Liên kết View trong item_course.xml
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            imgCourseIcon = itemView.findViewById(R.id.imgCourseIcon);
        }
    }

    public interface OnCourseClickListener {
        void onCourseClick(int position);
    }
}
