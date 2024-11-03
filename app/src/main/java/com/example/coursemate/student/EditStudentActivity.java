package com.example.coursemate.student;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursemate.R;

public class EditStudentActivity extends AppCompatActivity {
    private StudentDBHelper dbHelper;
    private EditText nameEditText, phoneEditText, emailEditText, addressEditText;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        dbHelper = new StudentDBHelper(this);

        nameEditText = findViewById(R.id.edit_name);
        phoneEditText = findViewById(R.id.edit_phone);
        emailEditText = findViewById(R.id.edit_email);
        addressEditText = findViewById(R.id.edit_address);
        Button saveButton = findViewById(R.id.save_button);

        // Khởi tạo Toolbar và thiết lập làm ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập tiêu đề và nút quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chỉnh sửa thông tin");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Sự kiện click vào nút quay lại
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Lấy thông tin sinh viên từ Intent
        Intent intent = getIntent();
        studentId = intent.getStringExtra("student_id");
        String name = intent.getStringExtra("student_name");
        String phone = intent.getStringExtra("student_phone");
        String email = intent.getStringExtra("student_email");
        String address = intent.getStringExtra("student_address");

        // Hiển thị thông tin hiện tại của sinh viên trong EditText
        nameEditText.setText(name);
        phoneEditText.setText(phone);
        emailEditText.setText(email);
        addressEditText.setText(address);

        saveButton.setOnClickListener(v -> saveStudentChanges());
    }

    private void saveStudentChanges() {
        String updatedName = nameEditText.getText().toString();
        String updatedPhone = phoneEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();
        String updatedAddress = addressEditText.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", updatedName);
        values.put("phone", updatedPhone);
        values.put("email", updatedEmail);
        values.put("address", updatedAddress);

        db.update("students", values, "student_id = ?", new String[]{studentId});
        db.close();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("updated_student_id", studentId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
