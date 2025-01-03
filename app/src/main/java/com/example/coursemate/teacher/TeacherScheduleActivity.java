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
import com.example.coursemate.adapter.TeacherScheduleAdapter;
import com.example.coursemate.model.Schedule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTeacherSchedule;
    private TeacherScheduleAdapter scheduleAdapter;
    private List<Schedule> scheduleList;

    private static final String TAG = "TeacherSchedule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_schedule);

        // Initialize RecyclerView
        recyclerViewTeacherSchedule = findViewById(R.id.recyclerViewTeacherSchedule);
        recyclerViewTeacherSchedule.setLayoutManager(new LinearLayoutManager(this));

        // Initialize schedule list and adapter
        scheduleList = new ArrayList<>();
        scheduleAdapter = new TeacherScheduleAdapter(this, scheduleList);
        recyclerViewTeacherSchedule.setAdapter(scheduleAdapter);

        // Fetch teacher schedules
        fetchTeacherSchedules();
    }

    private void fetchTeacherSchedules() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String teacherId = sharedPreferences.getString("user_id", null);

        if (teacherId == null) {
            Toast.makeText(this, "Không tìm thấy thông tin giáo viên. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        String rpcUrl = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "rpc/get_teacher_schedule";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("teacher_id", teacherId);
        } catch (Exception e) {
            Log.e(TAG, "Error creating request body", e);
            return;
        }

        SupabaseClientHelper.getNetworkUtils().post(rpcUrl, requestBody).thenAccept(response -> {
            Log.d(TAG, "Raw Supabase Response: " + response); // Log toàn bộ phản hồi
            if (response != null && !response.isEmpty()) {
                try {
                    JSONArray scheduleArray = new JSONArray(response);
                    List<Schedule> fetchedSchedules = new ArrayList<>();

                    for (int i = 0; i < scheduleArray.length(); i++) {
                        JSONObject scheduleObject = scheduleArray.getJSONObject(i);
                        Log.d(TAG, "Fetched schedule JSON: " + scheduleObject.toString());

                        String day = scheduleObject.optString("day", "Không rõ");
                        Log.d(TAG, "Parsed day value: " + day);

                        Schedule schedule = new Schedule(
                                scheduleObject.getString("course_name"),
                                day, // Truyền giá trị "day"
                                scheduleObject.getString("start_time"),
                                scheduleObject.getString("end_time"),
                                scheduleObject.optString("classroom_name", "Unknown Room")
                        );
                        fetchedSchedules.add(schedule);
                    }

                    runOnUiThread(() -> {
                        scheduleList.clear();
                        scheduleList.addAll(fetchedSchedules);
                        scheduleAdapter.notifyDataSetChanged();
                    });

                } catch (Exception e) {
                    Log.e(TAG, "Lỗi phân tích dữ liệu lịch dạy", e);
                    runOnUiThread(() -> Toast.makeText(this, "Lỗi khi xử lý dữ liệu từ máy chủ.", Toast.LENGTH_SHORT).show());
                }
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Không tìm thấy lịch dạy nào.", Toast.LENGTH_SHORT).show());
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Lỗi khi fetch dữ liệu từ Supabase", throwable);
            runOnUiThread(() -> Toast.makeText(this, "Lỗi kết nối đến máy chủ.", Toast.LENGTH_SHORT).show());
            return null;
        });
    }
}
