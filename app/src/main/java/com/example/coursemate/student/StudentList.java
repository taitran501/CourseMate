package com.example.coursemate.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursemate.R;
import com.example.coursemate.model.Student;
import com.example.coursemate.adapter.StudentGridAdapter;

import java.util.ArrayList;

public class StudentList extends AppCompatActivity {
    private StudentGridAdapter adapter;
    private StudentDBHelper dbHelper;
    private EditText searchEditText;
    private GridView studentGridView;

    private int currentPage = 1;
    private int totalPages = 1;

    // Khai báo ActivityResultLauncher để thêm sinh viên
    private final ActivityResultLauncher<Intent> addStudentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        // Xử lý kết quả trả về từ AddStudentActivity
                        String name = data.getStringExtra("student_name");
                        String phone = data.getStringExtra("student_phone");
                        String email = data.getStringExtra("student_email");
                        String address = data.getStringExtra("student_address");

                        // Thêm sinh viên mới vào danh sách
                        Student student = new Student(name, phone, email, address);
                        adapter.add(student);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
    );

    // Khai báo ActivityResultLauncher để chỉnh sửa sinh viên
    private final ActivityResultLauncher<Intent> editStudentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Nạp lại danh sách sinh viên sau khi chỉnh sửa
                    loadStudents();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        studentGridView = findViewById(R.id.student_grid_view);
        searchEditText = findViewById(R.id.search_edit_text);
        Button searchButton = findViewById(R.id.search_button);
        Button prevButton = findViewById(R.id.prev_button);
        Button nextButton = findViewById(R.id.next_button);
        Button addButton = findViewById(R.id.add_button);
        Button backButton = findViewById(R.id.back_button1);

        adapter = new StudentGridAdapter(this, new ArrayList<>(), editStudentLauncher);
        studentGridView.setAdapter(adapter);

        dbHelper = new StudentDBHelper(this);
        loadStudents();

        searchButton.setOnClickListener(v -> {
            String keyword = searchEditText.getText().toString();
            searchStudents(keyword);
        });

        prevButton.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                loadStudents();
            }
        });

        nextButton.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadStudents();
            }
        });

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(StudentList.this, AddStudentActivity.class);
            addStudentLauncher.launch(intent); // Sử dụng ActivityResultLauncher
        });

        backButton.setOnClickListener(v -> {
            finish(); // Quay lại màn hình trước đó
        });

        studentGridView.setOnItemClickListener((parent, view, position, id) -> {
            Student selectedStudent = adapter.getItem(position);
            Intent intent = new Intent(StudentList.this, StudentDetailActivity.class);
            intent.putExtra("student_id", selectedStudent.getStudentId());
            intent.putExtra("student_name", selectedStudent.getName());
            intent.putExtra("student_phone", selectedStudent.getPhone());
            intent.putExtra("student_email", selectedStudent.getEmail());
            intent.putExtra("student_address", selectedStudent.getAddress());
            editStudentLauncher.launch(intent);
        });
    }

    private void loadStudents() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("students", null, null, null, null, null, null);

        int totalStudents = cursor.getCount();
        // Số lượng sinh viên trên mỗi trang
        int pageSize = 8;
        totalPages = (int) Math.ceil((double) totalStudents / pageSize);
        int offset = (currentPage - 1) * pageSize;

        cursor.moveToPosition(offset);

        ArrayList<Student> studentList = new ArrayList<>();
        for (int i = 0; i < pageSize && !cursor.isAfterLast(); i++) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String studentId = cursor.getString(cursor.getColumnIndex("student_id"));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));

            Student student = new Student(name, phone, email, address);
            student.setStudentId(studentId);
            studentList.add(student);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        adapter.clear();
        adapter.addAll(studentList);
        adapter.notifyDataSetChanged();

        ((TextView) findViewById(R.id.page_text_view)).setText("Trang " + currentPage + "/" + totalPages);
    }

    private void searchStudents(String keyword) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("students", null, "name LIKE ?", new String[]{"%" + keyword.trim() + "%"}, null, null, null);

        ArrayList<Student> studentList = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String studentId = cursor.getString(cursor.getColumnIndex("student_id"));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));

            Student student = new Student(name, phone, email, address);
            student.setStudentId(studentId);
            studentList.add(student);
        }
        cursor.close();
        db.close();

        adapter.clear();
        adapter.addAll(studentList);
        adapter.notifyDataSetChanged();
    }
}