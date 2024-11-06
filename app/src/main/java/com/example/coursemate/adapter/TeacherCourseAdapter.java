package com.example.coursemate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.model.Course;

import java.util.List;

public class TeacherCourseAdapter extends RecyclerView.Adapter<TeacherCourseAdapter.TeacherCourseViewHolder> {

    private Context context;
    private List<Course> courseList;

    public TeacherCourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @Override
    public TeacherCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_course, parent, false);
        return new TeacherCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TeacherCourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.ivCourseIcon.setImageResource(course.getIconResource());
        holder.tvCourseName.setText(course.getName());
        holder.tvCourseStatus.setText(course.getStatus());
        holder.tvCourseDates.setText("Từ " + course.getStartDate() + " đến " + course.getEndDate());
        // Bạn có thể thêm các thông tin khác nếu cần
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class TeacherCourseViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCourseIcon;
        TextView tvCourseName, tvCourseStatus, tvCourseDates;

        public TeacherCourseViewHolder(View itemView) {
            super(itemView);
            ivCourseIcon = itemView.findViewById(R.id.ivTeacherCourseIcon);
            tvCourseName = itemView.findViewById(R.id.tvTeacherCourseName);
            tvCourseStatus = itemView.findViewById(R.id.tvTeacherCourseStatus);
            tvCourseDates = itemView.findViewById(R.id.tvTeacherCourseDates);

            // Thiết lập sự kiện click nếu cần (ví dụ: mở chi tiết khóa học)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Xử lý khi click vào item
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Course clickedCourse = courseList.get(position);
                        // Mở Activity chi tiết khóa học hoặc thực hiện hành động khác
                        // Ví dụ:
                        // Intent intent = new Intent(context, TeacherCourseDetailActivity.class);
                        // intent.putExtra("courseId", clickedCourse.getId());
                        // context.startActivity(intent);
                    }
                }
            });
        }
    }
}
