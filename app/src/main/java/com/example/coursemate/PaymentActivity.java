package com.example.coursemate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PaymentActivity extends AppCompatActivity {
    private TextView tvCourseName, tvTotalPrice, tvPaymentMethod;
    private Button btnPay;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvCourseName = findViewById(R.id.tvCourseName);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        btnPay = findViewById(R.id.btn_pay);

        // Khởi tạo Toolbar và thiết lập làm ActionBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thanh toán");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Nhận dữ liệu từ Intent
        String courseName = getIntent().getStringExtra("courseName");
        String coursePrice = getIntent().getStringExtra("coursePrice");

        // Hiển thị dữ liệu lên UI
        tvCourseName.setText(courseName);
        tvTotalPrice.setText(coursePrice);
        tvPaymentMethod.setText("Phương thức thanh toán: Tiền mặt");

        btnPay.setOnClickListener(v -> {
            String email = ((EditText) findViewById(R.id.etEmail)).getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(PaymentActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            String courseId = getIntent().getStringExtra("courseId");
            if (courseId == null || courseId.isEmpty() || coursePrice == null || coursePrice.isEmpty()) {
                Toast.makeText(PaymentActivity.this, "Không tìm thấy thông tin khóa học hoặc giá khóa học", Toast.LENGTH_SHORT).show();
                return;
            }

            // Xử lý coursePrice để loại bỏ định dạng (loại bỏ dấu '.')
            String cleanedCoursePrice = coursePrice.replace(".", ""); // Loại bỏ dấu '.'
            double parsedPrice;
            try {
                parsedPrice = Double.parseDouble(cleanedCoursePrice); // Chuyển thành số double
            } catch (NumberFormatException e) {
                Toast.makeText(PaymentActivity.this, "Lỗi định dạng giá khóa học", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            NetworkUtils networkUtils = SupabaseClientHelper.getNetworkUtils();
            String userQuery = "select=id&username=eq." + email;

            networkUtils.select("User", userQuery).thenAccept(userResponse -> {
                if (userResponse == null || userResponse.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(PaymentActivity.this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show());
                    return;
                }

                try {
                    JSONArray userArray = new JSONArray(userResponse);
                    if (userArray.length() == 0) {
                        runOnUiThread(() -> Toast.makeText(PaymentActivity.this, "Người dùng không tồn tại", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    String studentId = userArray.getJSONObject(0).getString("id");

                    // Định dạng thời gian thành ISO 8601 bằng SimpleDateFormat
                    SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                    iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String registrationDate = iso8601Format.format(new Date());

                    // Tạo JSON để thêm bản ghi vào CourseRegistration
                    JSONObject courseRegistration = new JSONObject();
                    courseRegistration.put("student_id", studentId);
                    courseRegistration.put("course_id", courseId);
                    courseRegistration.put("amount", parsedPrice); // Thêm giá khóa học đã được xử lý
                    courseRegistration.put("payment_status", "unpaid");
                    courseRegistration.put("registration_date", registrationDate);

                    // Gửi yêu cầu INSERT vào Supabase
                    networkUtils.insert("CourseRegistration", courseRegistration.toString()).thenRun(() -> {
                        runOnUiThread(() -> Toast.makeText(PaymentActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show());
                    }).exceptionally(throwable -> {
                        runOnUiThread(() -> Toast.makeText(PaymentActivity.this, "Lỗi khi đăng ký khóa học", Toast.LENGTH_SHORT).show());
                        return null;
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(PaymentActivity.this, "Lỗi xử lý dữ liệu người dùng", Toast.LENGTH_SHORT).show());
                    e.printStackTrace();
                }
            }).exceptionally(throwable -> {
                runOnUiThread(() -> Toast.makeText(PaymentActivity.this, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show());
                return null;
            });
        });
    }
}