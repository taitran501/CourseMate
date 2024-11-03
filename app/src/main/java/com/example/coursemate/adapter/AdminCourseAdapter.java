package com.example.coursemate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursemate.R;
import com.example.coursemate.course.CourseDetailActivity;
import com.example.coursemate.model.Course;
import java.util.List;

public class AdminCourseAdapter extends RecyclerView.Adapter<AdminCourseAdapter.ViewHolder> {
    private Context context;
    private List<Course> courseList;

    public AdminCourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseName.setText(course.getName());
        holder.courseDescription.setText(course.getDescription());
        holder.courseStatus.setText(course.getStatus());
        holder.courseIcon.setImageResource(course.getIconResource());

        // Thiết lập sự kiện click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("course_id", course.getId()); // Truyền ID khóa học qua Intent
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseDescription, courseStatus;
        ImageView courseIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseName);
            courseDescription = itemView.findViewById(R.id.courseDescription);
            courseStatus = itemView.findViewById(R.id.courseStatus);
            courseIcon = itemView.findViewById(R.id.courseIcon);
        }
    }
}
