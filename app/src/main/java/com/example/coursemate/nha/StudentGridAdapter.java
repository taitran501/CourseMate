package com.example.coursemate.nha;

// StudentGridAdapter.java

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;

import com.example.coursemate.R;

import java.util.ArrayList;
import java.util.List;

public class StudentGridAdapter extends BaseAdapter {
    private final Context context;
    private final List<Student> students;
    private final ActivityResultLauncher<Intent> editStudentLauncher;
    
    public StudentGridAdapter(Context context, List<Student> students, ActivityResultLauncher<Intent> editStudentLauncher) {
        this.context = context;
        this.students = students != null ? students : new ArrayList<>();
        this.editStudentLauncher = editStudentLauncher;
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Student getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false);
        }

        Student student = students.get(position);

        TextView nameTextView = convertView.findViewById(R.id.student_name);
        TextView phoneTextView = convertView.findViewById(R.id.student_phone);
        TextView emailTextView = convertView.findViewById(R.id.student_email);
        TextView addressTextView = convertView.findViewById(R.id.student_address);

        nameTextView.setText(student.getName());
        phoneTextView.setText(student.getPhone());
        emailTextView.setText(student.getEmail());
        addressTextView.setText(student.getAddress());

        // Thêm sự kiện onClick cho convertView
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StudentDetailActivity.class);
            intent.putExtra("student_id", student.getStudentId());
            intent.putExtra("student_name", student.getName());
            intent.putExtra("student_phone", student.getPhone());
            intent.putExtra("student_email", student.getEmail());
            intent.putExtra("student_address", student.getAddress());
            editStudentLauncher.launch(intent); // Sử dụng ActivityResultLauncher
        });

        return convertView;
    }

    public void add(Student student) {
        students.add(student);
        notifyDataSetChanged();
    }

    public void clear() {
        students.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Student> studentList) {
        students.addAll(studentList);
        notifyDataSetChanged();
    }
}
