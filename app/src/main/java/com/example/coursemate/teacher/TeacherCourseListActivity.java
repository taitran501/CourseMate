package com.example.coursemate.teacher;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import com.example.coursemate.adapter.TeacherCourseAdapter;
import com.example.coursemate.model.Course;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherCourseListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TeacherCourseAdapter adapter;
    private List<Course> courseList;

    private static final String TAG = "TeacherCourseList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_list);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTeacherCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách khóa học
        courseList = new ArrayList<>();
        adapter = new TeacherCourseAdapter(this, courseList);
        recyclerView.setAdapter(adapter);

        // Fetch dữ liệu từ Supabase
        fetchTeacherCourses();
    }

    private void fetchTeacherCourses() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String teacherId = sharedPreferences.getString("user_id", null);

        if (teacherId == null) {
            Toast.makeText(this, "Không tìm thấy thông tin giáo viên. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        String query = "select=name,status,start_date,end_date&teacher_id=eq." + teacherId + "&order=status.desc";
        SupabaseClientHelper.getNetworkUtils().select("Course", query).thenAccept(response -> {
            if (response != null && !response.isEmpty()) {
                Log.d("FETCH_RESPONSE", response); // Log toàn bộ JSON trả về
                try {
                    JSONArray coursesArray = new JSONArray(response);
                    List<Course> fetchedCourses = new ArrayList<>();

                    for (int i = 0; i < coursesArray.length(); i++) {
                        JSONObject courseObject = coursesArray.getJSONObject(i);
                        Log.d("COURSE_OBJECT", courseObject.toString()); // Log từng object trong mảng JSON

                        Course course = new Course(
                                null, // Không sử dụng ID
                                courseObject.getString("name"), // Tên khóa học
                                courseObject.getString("status"), // Trạng thái khóa học
                                courseObject.optString("start_date", ""), // Ngày bắt đầu
                                courseObject.optString("end_date", "") // Ngày kết thúc
                        );
                        fetchedCourses.add(course);
                        Log.d("FETCH_RESPONSE", "Fetched course: " + courseObject.toString());
                        Log.d("FETCH_RESPONSE", "Status: " + courseObject.optString("status"));
                    }

                    runOnUiThread(() -> {
                        courseList.clear();
                        courseList.addAll(fetchedCourses);
                        adapter.notifyDataSetChanged();
                    });

                } catch (Exception e) {
                    Log.e("FETCH_ERROR", "Lỗi phân tích dữ liệu khóa học", e);
                    runOnUiThread(() -> Toast.makeText(this, "Lỗi khi xử lý dữ liệu từ máy chủ.", Toast.LENGTH_SHORT).show());
                }
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Không tìm thấy khóa học nào.", Toast.LENGTH_SHORT).show();
                });
            }
        }).exceptionally(throwable -> {
            Log.e("FETCH_ERROR", "Lỗi khi fetch dữ liệu từ Supabase", throwable);
            runOnUiThread(() -> Toast.makeText(this, "Lỗi kết nối đến máy chủ.", Toast.LENGTH_SHORT).show());
            return null;
        });
    }
}
