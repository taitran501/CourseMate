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

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList;

    public CourseAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_schedule_student_course.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_student_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);

        // Bind data to views
        holder.tvCourseName.setText(course.getName());
        holder.tvCourseDescription.setText(course.getDescription());
        holder.tvCourseTime.setText("Time: " + course.getStartTime() + " - " + course.getEndTime());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvCourseDescription, tvCourseTime;

        public CourseViewHolder(View itemView) {
            super(itemView);

            // Map XML views
            tvCourseName = itemView.findViewById(R.id.course_name);
            tvCourseTime = itemView.findViewById(R.id.course_time);
        }
    }
}
