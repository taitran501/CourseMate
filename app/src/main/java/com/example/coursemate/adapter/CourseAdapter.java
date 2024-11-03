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

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private final ArrayList<Course> courseList;
    private final OnCourseClickListener listener;

    public interface OnCourseClickListener {
        void onCourseClick(int position);
    }

    public CourseAdapter(ArrayList<Course> courseList, OnCourseClickListener listener) {
        this.courseList = courseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.tvCourseName.setText(course.getName());
        holder.imgCourseIcon.setImageResource(course.getIconResource());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName;
        ImageView imgCourseIcon;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            imgCourseIcon = itemView.findViewById(R.id.imgCourseIcon);

            // Đảm bảo chỉ gọi listener khi nó đã được khởi tạo
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition(); // Thay thế getBindingAdapterPosition() bằng getAdapterPosition()
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCourseClick(position);
                }
            });
        }
    }
}
