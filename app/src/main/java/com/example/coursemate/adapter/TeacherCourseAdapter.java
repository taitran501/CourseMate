package com.example.coursemate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.model.Course;

import java.util.List;

public class TeacherCourseAdapter extends RecyclerView.Adapter<TeacherCourseAdapter.TeacherCourseViewHolder> {

    private final Context context;
    private final List<Course> courseList;

    public TeacherCourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public TeacherCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_course, parent, false);
        return new TeacherCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherCourseViewHolder holder, int position) {
        Course course = courseList.get(position);

        // Hiển thị thông tin khóa học
        holder.tvCourseName.setText(course.getName()); // Tên khóa học
        holder.tvCourseStatus.setText("Trạng thái: " + course.getStatus()); // Trạng thái khóa học
        holder.tvCourseDates.setText(course.getStartDate() + " - " + course.getEndDate()); // Ngày bắt đầu và kết thúc
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class TeacherCourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvCourseStatus, tvCourseDates;

        public TeacherCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvTeacherCourseName); // Tên khóa học
            tvCourseStatus = itemView.findViewById(R.id.tvTeacherCourseStatus); // Trạng thái khóa học
            tvCourseDates = itemView.findViewById(R.id.tvTeacherCourseDates); // Ngày bắt đầu và kết thúc
        }
    }
}
