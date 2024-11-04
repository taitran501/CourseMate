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

import java.util.List;

public class AdminCourseAdapter extends RecyclerView.Adapter<AdminCourseAdapter.ViewHolder> {

    private final List<Course> courseList;
    private final OnCourseClickListener listener;

    // Interface để xử lý sự kiện click
    public interface OnCourseClickListener {
        void onCourseClick(int position);
    }

    public AdminCourseAdapter(List<Course> courseList, OnCourseClickListener listener) {
        this.courseList = courseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_course, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseName.setText(course.getName());
        holder.courseDescription.setText(course.getDescription());
        holder.courseStatus.setText(course.getStatus());
        holder.courseIcon.setImageResource(course.getIconResource());
    }

    public void updateCourseList(List<Course> newCourseList) {
        courseList.clear();
        courseList.addAll(newCourseList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public Course getCourseAtPosition(int position) {
        return courseList.get(position);
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView courseName, courseDescription, courseStatus;
        ImageView courseIcon;
        OnCourseClickListener onCourseClickListener;

        public ViewHolder(@NonNull View itemView, OnCourseClickListener listener) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseName);
            courseDescription = itemView.findViewById(R.id.courseDescription);
            courseStatus = itemView.findViewById(R.id.courseStatus);
            courseIcon = itemView.findViewById(R.id.courseIcon);
            this.onCourseClickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onCourseClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onCourseClickListener.onCourseClick(position);
                }
            }
        }
    }
}
