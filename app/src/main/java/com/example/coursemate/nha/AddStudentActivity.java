package com.example.coursemate.nha;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursemate.R;

import java.util.regex.Pattern;

public class AddStudentActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private StudentDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student);

        nameEditText = findViewById(R.id.name_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);

        // Khởi tạo Toolbar và thiết lập làm ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập tiêu đề và nút quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thêm thông tin");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Sự kiện click vào nút quay lại
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        findViewById(R.id.save_button).setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String address = addressEditText.getText().toString();

            // Kiểm tra thông tin đầu vào
            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidName(name)) {
                Toast.makeText(this, "Tên chỉ được nhập chữ cái", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPhone(phone)) {
                Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Email phải có đuôi gmail.com", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu sinh viên vào cơ sở dữ liệu
            StudentDBHelper dbHelper = new StudentDBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("phone", phone);
            values.put("email", email);
            values.put("address", address);

            long newRowId = db.insert("students", null, values);
            db.close();

            if (newRowId != -1) {
                // Trả về thông tin sinh viên mới thêm vào
                Intent resultIntent = new Intent();
                resultIntent.putExtra("student_name", name);
                resultIntent.putExtra("student_phone", phone);
                resultIntent.putExtra("student_email", email);
                resultIntent.putExtra("student_address", address);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi thêm sinh viên", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isValidName(String name) {
        // Kiểm tra tên chỉ chứa chữ cái (có thể có dấu)
        return Pattern.matches("^[\\p{L} ]+$", name);
    }

    private boolean isValidPhone(String phone) {
        // Kiểm tra số điện thoại có 11 chữ số và bắt đầu bằng số 0
        return Pattern.matches("^0\\d{10}$", phone);
    }

    private boolean isValidEmail(String email) {
        // Kiểm tra email có đuôi gmail.com
        return email.endsWith("@gmail.com");
    }
}