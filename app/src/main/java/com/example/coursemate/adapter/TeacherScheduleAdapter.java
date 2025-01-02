package com.example.coursemate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.model.Schedule;

import java.util.List;

public class TeacherScheduleAdapter extends RecyclerView.Adapter<TeacherScheduleAdapter.TeacherScheduleViewHolder> {

    private final Context context;
    private final List<Schedule> scheduleList;

    public TeacherScheduleAdapter(Context context, List<Schedule> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public TeacherScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_schedule, parent, false);
        return new TeacherScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);

        holder.tvDay.setText("Ngày: " + schedule.getDay());
        holder.tvTime.setText("Thời gian: " + schedule.getStartTime() + " - " + schedule.getEndTime());
        holder.tvCourseName.setText(schedule.getCourseName());
        holder.tvClassroom.setText("Phòng: " + schedule.getClassroomName());
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    static class TeacherScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvTime, tvCourseName, tvClassroom;

        public TeacherScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvTeacherScheduleDay);
            tvTime = itemView.findViewById(R.id.tvTeacherScheduleTime);
            tvCourseName = itemView.findViewById(R.id.tvTeacherScheduleCourseName);
            tvClassroom = itemView.findViewById(R.id.tvTeacherScheduleClassroom);
        }
    }
}
