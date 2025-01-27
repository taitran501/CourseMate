package com.example.coursemate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.model.Schedule2;

import java.util.List;

public class StudentScheduleAdapter extends RecyclerView.Adapter<StudentScheduleAdapter.ScheduleViewHolder> {
// ADAPTER FOR STUDENT SCHEDULE
    private final List<Schedule2> scheduleList;
    private final Context context;

    public StudentScheduleAdapter(List<Schedule2> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule_student_course, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule2 schedule = scheduleList.get(position);

        // Gán dữ liệu vào ViewHolder
        holder.tvCourseName.setText(schedule.getCourseName());
        holder.tvTeacherName.setText("Giáo viên: " + schedule.getTeacherName());
        holder.tvTime.setText("Thời gian: " + schedule.getStartTime() + " - " + schedule.getEndTime());
        holder.tvClassroom.setText("Phòng: " + schedule.getClassroomName());
    }

    @Override
    public int getItemCount() {
        return scheduleList != null ? scheduleList.size() : 0; // Tránh lỗi null
    }

    // ViewHolder class
    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvTeacherName, tvTime, tvClassroom;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCourseName = itemView.findViewById(R.id.course_name);
            tvTeacherName = itemView.findViewById(R.id.teacher_name);
            tvTime = itemView.findViewById(R.id.course_time);
            tvClassroom = itemView.findViewById(R.id.classroom_name);
        }
    }
}
