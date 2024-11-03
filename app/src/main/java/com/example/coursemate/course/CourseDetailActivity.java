package com.example.coursemate.course;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.coursemate.DatabaseHelper;
import com.example.coursemate.R;
import com.example.coursemate.model.Course;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CourseDetailActivity extends AppCompatActivity {

    private EditText editTextCourseName, editTextDescription, editTextMaxStudents, editTextStartDate, editTextEndDate, editTextPrice;
    private Spinner spinnerStatus;
    private Button buttonSave;
    private DatabaseHelper databaseHelper;
    private int courseId; // ID khóa học sẽ lấy từ Intent
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Khởi tạo Toolbar và thiết lập làm ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chỉnh sửa thông tin");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Khởi tạo các view
        editTextCourseName = findViewById(R.id.editTextCourseName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextMaxStudents = findViewById(R.id.editTextMaxStudents);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        editTextPrice = findViewById(R.id.editTextPrice);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        buttonSave = findViewById(R.id.buttonSave);

        // Thiết lập spinner cho trạng thái
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.course_status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Lấy ID khóa học từ Intent
        courseId = getIntent().getIntExtra("course_id", -1);
        if (courseId != -1) {
            loadCourseData(courseId); // Gọi hàm tải dữ liệu
        }

        // Thiết lập sự kiện cho nút Lưu
        buttonSave.setOnClickListener(v -> saveCourseData());
    }

    // Hàm tải dữ liệu từ database và điền vào các trường
    private void loadCourseData(int courseId) {
        Course course = databaseHelper.getCourseById(courseId); // Lấy dữ liệu khóa học từ cơ sở dữ liệu

        if (course != null) {
            // Điền dữ liệu vào các trường trong form
            editTextCourseName.setText(course.getName());
            editTextDescription.setText(course.getDescription());
            editTextMaxStudents.setText(String.valueOf(course.getMaxStudents()));

            // Chuyển đổi Date thành String để hiển thị
            editTextStartDate.setText(course.getStartDate() != null ? dateFormat.format(course.getStartDate()) : "");
            editTextEndDate.setText(course.getEndDate() != null ? dateFormat.format(course.getEndDate()) : "");

            editTextPrice.setText(course.getPrice());

            // Thiết lập giá trị cho Spinner trạng thái
            String status = course.getStatus();
            if ("open".equalsIgnoreCase(status)) {
                spinnerStatus.setSelection(0);
            } else if ("closed".equalsIgnoreCase(status)) {
                spinnerStatus.setSelection(1);
            } else if ("ongoing".equalsIgnoreCase(status)) {
                spinnerStatus.setSelection(2);
            }
        }
    }

    private void saveCourseData() {
        String name = editTextCourseName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        int maxStudents = Integer.parseInt(editTextMaxStudents.getText().toString().trim());
        String price = editTextPrice.getText().toString().trim();

        // Lấy và chuyển đổi dữ liệu ngày từ EditText thành String
        String startDateStr = editTextStartDate.getText().toString().trim();
        String endDateStr = editTextEndDate.getText().toString().trim();

        // Lấy trạng thái từ Spinner
        String status = spinnerStatus.getSelectedItem().toString();

        // Cập nhật dữ liệu khóa học trong cơ sở dữ liệu
        Course updatedCourse = new Course(courseId, 1, name, R.drawable.ic_course_placeholder, description, maxStudents, status, startDateStr, endDateStr, price, "20");
        int rowsAffected = databaseHelper.updateCourse(updatedCourse);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Đã lưu thông tin khóa học", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity sau khi lưu thành công
        } else {
            Toast.makeText(this, "Lỗi khi lưu thông tin khóa học", Toast.LENGTH_SHORT).show();
        }
    }

}
