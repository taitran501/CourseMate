package com.example.coursemate.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursemate.R;

public class StudentDetailActivity extends AppCompatActivity {
    private StudentDBHelper dbHelper;
    private TextView nameTextView, phoneTextView, emailTextView, addressTextView;
    private Button editButton, deleteButton;
    private String studentId;

    // Khởi tạo ActivityResultLauncher để xử lý kết quả từ EditStudentActivity
    private final ActivityResultLauncher<Intent> editStudentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadStudentDetails(); // Tải lại thông tin sinh viên sau khi chỉnh sửa
                }
            }
    );

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail); // Đảm bảo layout đúng

        dbHelper = new StudentDBHelper(this);

        nameTextView = findViewById(R.id.student_detail_name);
        phoneTextView = findViewById(R.id.student_detail_phone);
        emailTextView = findViewById(R.id.student_detail_email);
        addressTextView = findViewById(R.id.student_detail_address);
        editButton = findViewById(R.id.edit_button);
        deleteButton = findViewById(R.id.delete_button);

        // Lấy dữ liệu sinh viên từ Intent
        Intent intent = getIntent();
        studentId = intent.getStringExtra("student_id");
        String name = intent.getStringExtra("student_name");
        String phone = intent.getStringExtra("student_phone");
        String email = intent.getStringExtra("student_email");
        String address = intent.getStringExtra("student_address");

        // Hiển thị dữ liệu sinh viên lên các TextView
        nameTextView.setText(name);
        phoneTextView.setText(phone);
        emailTextView.setText(email);
        addressTextView.setText(address);

        // Thiết lập sự kiện cho các nút
        deleteButton.setOnClickListener(v -> confirmDeleteStudent());
        editButton.setOnClickListener(v -> editStudent());

        // Tìm và thiết lập sự kiện cho nút Quay lại
        Button backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                finish(); // Quay lại Activity trước đó
            });
        }
    }

    private void confirmDeleteStudent() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa Sinh viên")
                .setMessage("Bạn có chắc chắn muốn xóa sinh viên này?")
                .setPositiveButton("Có", (dialog, which) -> deleteStudent())
                .setNegativeButton("Không", null)
                .show();
    }

    private void deleteStudent() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("students", "student_id = ?", new String[]{studentId});
        db.close();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("deleted_student_id", studentId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void editStudent() {
        Intent intent = new Intent(this, EditStudentActivity.class);
        intent.putExtra("student_id", studentId);
        intent.putExtra("student_name", nameTextView.getText().toString());
        intent.putExtra("student_phone", phoneTextView.getText().toString());
        intent.putExtra("student_email", emailTextView.getText().toString());
        intent.putExtra("student_address", addressTextView.getText().toString());
        editStudentLauncher.launch(intent); // Sử dụng ActivityResultLauncher để mở EditStudentActivity
    }

    private void loadStudentDetails() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("students", null, "student_id = ?", new String[]{studentId}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));

            nameTextView.setText(name);
            phoneTextView.setText(phone);
            emailTextView.setText(email);
            addressTextView.setText(address);

            cursor.close();
        }
        db.close();
    }
}
