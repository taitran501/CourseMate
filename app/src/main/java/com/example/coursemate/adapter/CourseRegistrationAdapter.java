//package com.example.coursemate.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.coursemate.R;
//import com.example.coursemate.model.CourseRegistration;
//import com.example.coursemate.model.Teacher;
//
//import java.util.List;
//
//public class CourseRegistrationAdapter extends RecyclerView.Adapter<CourseRegistrationAdapter.CourseRegistrationViewHolder> {
//
//    private List<CourseRegistration> courseRegistrationList;
//    private OnCourseRegistrationClickListener onCourseRegistrationClickListener;
//
//    public CourseRegistrationAdapter(List<CourseRegistration> courseRegistration, OnCourseRegistrationClickListener listener) {
//        this.courseRegistrationList = courseRegistration;
//        this.onCourseRegistrationClickListener = listener;
//    }
//
//    @NonNull
//    @Override
//    public CourseRegistrationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_registration, parent, false);
//        return new CourseRegistrationViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
//        Teacher teacher = teacherList.get(position);
//        holder.bind(teacher, onTeacherClickListener);
//    }
//
//    @Override
//    public int getItemCount() {
//        return teacherList.size();
//    }
//
//    public static class TeacherViewHolder extends RecyclerView.ViewHolder {
//        private TextView nameTextView;
//        private ImageView avatarImageView;
//
//        public TeacherViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nameTextView = itemView.findViewById(R.id.tv_teacher_name);
//            avatarImageView = itemView.findViewById(R.id.iv_teacher_avatar);
//        }
//
//        public void bind(final Teacher teacher, final OnTeacherClickListener listener) {
//            nameTextView.setText(teacher.getName());
//            avatarImageView.setImageResource(teacher.getAvatarResId());
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onTeacherClick(teacher);
//                }
//            });
//        }
//    }
//
//    public interface OnCourseRegistrationClickListener {
//        void onCourseRegistationrClick(CourseRegistration courseRegistration);
//    }
//}
