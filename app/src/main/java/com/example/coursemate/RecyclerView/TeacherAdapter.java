package com.example.coursemate.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.Teacher;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {

    private List<Teacher> teacherList;
    private OnTeacherClickListener onTeacherClickListener;

    public TeacherAdapter(List<Teacher> teachers, OnTeacherClickListener listener) {
        this.teacherList = teachers;
        this.onTeacherClickListener = listener;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher, parent, false);
        return new TeacherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);
        holder.bind(teacher, onTeacherClickListener);
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public static class TeacherViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private ImageView avatarImageView;

        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_teacher_name);
            avatarImageView = itemView.findViewById(R.id.iv_teacher_avatar);
        }

        public void bind(final Teacher teacher, final OnTeacherClickListener listener) {
            nameTextView.setText(teacher.getName());
            avatarImageView.setImageResource(teacher.getAvatarResId());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTeacherClick(teacher);
                }
            });
        }
    }

    public interface OnTeacherClickListener {
        void onTeacherClick(Teacher teacher);
    }
}
