package com.example.coursemate.course;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coursemate.DatabaseHelper;
import com.example.coursemate.DateUtils;
import com.example.coursemate.R;
import com.example.coursemate.model.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CourseDetailActivity extends AppCompatActivity {

    private EditText editTextCourseName, editTextDescription, editTextMaxStudents, editTextStartDate, editTextEndDate, editTextPrice;
    private Spinner spinnerStatus;
    private Button buttonSave;
    private ImageView iconStartDate, iconEndDate; // Thêm ImageView cho icon ngày
    private DatabaseHelper databaseHelper;
    private DateUtils dateUtils;
    private int courseId; // ID khóa học sẽ lấy từ Intent

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
        iconStartDate = findViewById(R.id.iconStartDate);
        iconEndDate = findViewById(R.id.iconEndDate);

        // Thiết lập spinner cho trạng thái
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.course_status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        // Khởi tạo DatabaseHelper và DateUtils
        databaseHelper = new DatabaseHelper(this);
        dateUtils = new DateUtils();

        // Lấy ID khóa học từ Intent
        courseId = getIntent().getIntExtra("course_id", -1);
        if (courseId != -1) {
            loadCourseData(courseId); // Gọi hàm tải dữ liệu
        } else {
            Toast.makeText(this, "Không tìm thấy khóa học", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity nếu không có course_id
        }

        // Thiết lập sự kiện cho nút Lưu
        buttonSave.setOnClickListener(v -> saveCourseData());

        // Thiết lập sự kiện cho các icon chọn ngày
        iconStartDate.setOnClickListener(v -> showDatePickerDialog(editTextStartDate));
        iconEndDate.setOnClickListener(v -> showDatePickerDialog(editTextEndDate));
    }

    // Hàm tải dữ liệu từ database và điền vào các trường
    private void loadCourseData(int courseId) {
        Course course = databaseHelper.getCourseById(courseId); // Lấy dữ liệu khóa học từ cơ sở dữ liệu

        if (course != null) {
            // Điền dữ liệu vào các trường trong form
            editTextCourseName.setText(course.getName());
            editTextDescription.setText(course.getDescription());
            editTextMaxStudents.setText(String.valueOf(course.getMaxStudents()));
            editTextPrice.setText(course.getPrice());

            // Chuyển đổi và điền dữ liệu ngày tháng từ "yyyy-MM-dd" sang "dd-MM-yyyy"
            String formattedStartDate = dateUtils.convertDateFormat(course.getStartDate());
            String formattedEndDate = dateUtils.convertDateFormat(course.getEndDate());

            editTextStartDate.setText(formattedStartDate != null ? formattedStartDate : "");
            editTextEndDate.setText(formattedEndDate != null ? formattedEndDate : "");

            // Thiết lập giá trị cho Spinner trạng thái
            String status = course.getStatus();
            if ("open".equalsIgnoreCase(status)) {
                spinnerStatus.setSelection(0);
            } else if ("closed".equalsIgnoreCase(status)) {
                spinnerStatus.setSelection(1);
            } else if ("ongoing".equalsIgnoreCase(status)) {
                spinnerStatus.setSelection(2);
            }
        } else {
            Toast.makeText(this, "Không tìm thấy khóa học", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity nếu không tìm thấy khóa học
        }
    }

    // Hàm hiển thị DatePickerDialog
    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        String currentDate = editText.getText().toString().trim();

        // Nếu EditText đã có ngày, đặt mặc định DatePicker tại ngày đó
        if (!currentDate.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = sdf.parse(currentDate);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CourseDetailActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Tạo chuỗi ngày theo định dạng "dd-MM-yyyy"
                    String selectedDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear);
                    editText.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void saveCourseData() {
        String name = editTextCourseName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String maxStudentsStr = editTextMaxStudents.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (name.isEmpty() || maxStudentsStr.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int maxStudents;
        try {
            maxStudents = Integer.parseInt(maxStudentsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lượng sinh viên tối đa không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy và chuyển đổi dữ liệu ngày từ EditText thành String theo định dạng gốc để lưu vào database
        String startDateStr = editTextStartDate.getText().toString().trim();
        String endDateStr = editTextEndDate.getText().toString().trim();

        // Chuyển đổi định dạng ngày từ "dd-MM-yyyy" về "yyyy-MM-dd" trước khi lưu
        String formattedStartDate = dateUtils.reverseConvertDateFormat(startDateStr);
        String formattedEndDate = dateUtils.reverseConvertDateFormat(endDateStr);

        // Lấy trạng thái từ Spinner
        String status = spinnerStatus.getSelectedItem().toString();

        // Lấy teacherId từ khóa học hiện tại
        Course currentCourse = databaseHelper.getCourseById(courseId);
        if (currentCourse == null) {
            Toast.makeText(this, "Không tìm thấy khóa học để cập nhật", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật dữ liệu khóa học trong cơ sở dữ liệu
        Course updatedCourse = new Course(
                courseId,
                currentCourse.getTeacherId(), // Giữ nguyên teacher_id
                name,
                R.drawable.ic_course_placeholder, // Placeholder cho icon
                description,
                maxStudents,
                status,
                formattedStartDate,
                formattedEndDate,
                price
        );

        int rowsAffected = databaseHelper.updateCourse(updatedCourse);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Đã lưu thông tin khóa học", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity sau khi lưu thành công
        } else {
            Toast.makeText(this, "Lỗi khi lưu thông tin khóa học", Toast.LENGTH_SHORT).show();
        }
    }
}
