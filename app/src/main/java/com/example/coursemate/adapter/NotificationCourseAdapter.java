package com.example.coursemate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.model.Course;

import android.widget.TextView;

import java.util.List;

public class NotificationCourseAdapter extends RecyclerView.Adapter<NotificationCourseAdapter.CourseViewHolder> {

    private List<Course> courseList;
    private Context context;
    private OnItemClickListener listener;

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Course course);
    }

    // Sửa tên constructor từ CourseAdapter thành NotificationCourseAdapter
    public NotificationCourseAdapter(List<Course> courseList, Context context, OnItemClickListener listener) {
        this.courseList = courseList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Đảm bảo layout đúng
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.tvCourseName.setText(course.getName());
        holder.tvCourseDescription.setText(course.getDescription());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvCourseDescription;

        public CourseViewHolder(View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCourseDescription = itemView.findViewById(R.id.tvCourseDescription);
        }
    }

    // Phương thức để cập nhật danh sách khóa học
    public void setCourses(List<Course> courses) {
        this.courseList = courses;
        notifyDataSetChanged();
    }
}
