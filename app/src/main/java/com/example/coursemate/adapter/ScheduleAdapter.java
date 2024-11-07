// ScheduleAdapter.java
package com.example.coursemate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.model.Schedule;

import android.widget.TextView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Schedule> scheduleList;
    private Context context;

    public ScheduleAdapter(List<Schedule> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);

        // Chuyển đổi giá trị ngày từ số sang tên ngày
        String dayName = getDayName(schedule.getDay());
        holder.tvDay.setText(dayName);

        // Định dạng thời gian
        String time = formatTime(schedule.getStartTime()) + " - " + formatTime(schedule.getEndTime());
        holder.tvTime.setText(time);

        // Hiển thị tên khóa học và phòng học
        holder.tvCourseName.setText(schedule.getCourse().getName());
        holder.tvClassroom.setText("Phòng: " + schedule.getClassroom().getName());
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvTime, tvCourseName, tvClassroom;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvClassroom = itemView.findViewById(R.id.tvClassroom);
        }
    }

    private String getDayName(String dayNumber) {
        switch (dayNumber) {
            case "0":
                return "Chủ Nhật";
            case "1":
                return "Thứ Hai";
            case "2":
                return "Thứ Ba";
            case "3":
                return "Thứ Tư";
            case "4":
                return "Thứ Năm";
            case "5":
                return "Thứ Sáu";
            case "6":
                return "Thứ Bảy";
            default:
                return "Không xác định";
        }
    }

    private String formatTime(float time) {
        int hour = (int) time;
        int minute = (int) ((time - hour) * 60);
        return String.format("%02d:%02d", hour, minute);
    }
}
