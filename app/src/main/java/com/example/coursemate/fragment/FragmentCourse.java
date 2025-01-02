package com.example.coursemate.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import com.example.coursemate.adapter.StudentCourseAdapter;
import com.example.coursemate.model.Course;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentCourse extends Fragment {

    private static final String TAG = "FragmentCourse";
    private RecyclerView recyclerView;
    private LinearLayout btnCompleted, btnOngoing, btnUpcoming;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        // Ánh xạ view
        recyclerView = rootView.findViewById(R.id.rv_courses);
        btnCompleted = rootView.findViewById(R.id.btn_completed);
        btnOngoing = rootView.findViewById(R.id.btn_ongoing);
        btnUpcoming = rootView.findViewById(R.id.btn_upcoming);

        // Cài đặt RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Gắn sự kiện cho các nút
        btnCompleted.setOnClickListener(v -> fetchCourses("get_completed_courses"));
        btnOngoing.setOnClickListener(v -> fetchCourses("get_ongoing_courses"));
        btnUpcoming.setOnClickListener(v -> fetchCourses("get_upcoming_courses_with_payment"));

        return rootView;
    }

    private void fetchCourses(String endpoint) {
        Log.d(TAG, "Fetching courses from endpoint: " + endpoint);

        String url = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "rpc/" + endpoint;
        JSONObject requestBody = new JSONObject();

        try {
            String userId = getUserId();
            if (userId == null) {
                Log.e(TAG, "User ID is null. Please log in again.");
                showToast("Vui lòng đăng nhập lại.");
                return;
            }
            requestBody.put("user_id", userId);
        } catch (Exception e) {
            Log.e(TAG, "Error creating request body", e);
            return;
        }

        SupabaseClientHelper.getNetworkUtils().post(url, requestBody).thenAccept(response -> {
            if (response != null && !response.isEmpty()) {
                try {
                    JSONArray coursesArray = new JSONArray(response);
                    ArrayList<Course> fetchedCourses = new ArrayList<>();
                    ArrayList<String> registrationDates = new ArrayList<>();

                    for (int i = 0; i < coursesArray.length(); i++) {
                        JSONObject courseObject = coursesArray.getJSONObject(i);

                        // Lấy thông tin khóa học
                        String id = courseObject.optString("course_id", "");
                        String name = courseObject.optString("course_name", "No Name");
                        String description = courseObject.optString("course_description", "Không có mô tả");

                        // Lấy ngày đăng ký từ JSON nếu có
                        String registrationDate = courseObject.optString("registration_date", "Không rõ");

                        fetchedCourses.add(new Course(id, name, description)); // Chỉ tạo Course từ các trường hợp lệ
                        registrationDates.add(registrationDate); // Lưu ngày đăng ký vào danh sách riêng
                    }

                    updateRecyclerView(fetchedCourses, registrationDates);

                } catch (Exception e) {
                    Log.e(TAG, "Error parsing course list", e);
                }
            } else {
                Log.e(TAG, "Empty or invalid response from " + endpoint);
                showToast("Không có dữ liệu từ máy chủ");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch courses from " + endpoint, throwable);
            showToast("Lỗi kết nối đến máy chủ");
            return null;
        });
    }

    private void updateRecyclerView(ArrayList<Course> courses, ArrayList<String> registrationDates) {
        getActivity().runOnUiThread(() -> {
            StudentCourseAdapter studentCourseAdapter = new StudentCourseAdapter(courses, registrationDates);
            recyclerView.setAdapter(studentCourseAdapter);
            Log.d(TAG, "RecyclerView updated with " + courses.size() + " courses using StudentCourseAdapter");
        });
    }

    private void showToast(String message) {
        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_session", getActivity().MODE_PRIVATE);
        return sharedPreferences.getString("user_id", null); // Lấy user_id
    }
}
