package com.example.coursemate.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.NetworkUtils;
import com.example.coursemate.R;
import com.example.coursemate.adapter.CourseAdapter;
import com.example.coursemate.model.Course;
import com.example.coursemate.SupabaseClientHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentCourse extends Fragment {
    private static final String TAG = "FragmentCourse";

    private View btnCompleted, btnOngoing, btnUpcoming;
    private RecyclerView rvCourses;
    private CourseAdapter courseAdapter;
    private List<Course> courseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        // Ánh xạ các view
        btnCompleted = view.findViewById(R.id.btn_completed);
        btnOngoing = view.findViewById(R.id.btn_ongoing);
        btnUpcoming = view.findViewById(R.id.btn_upcoming);
        rvCourses = view.findViewById(R.id.rv_courses);

        // Cấu hình RecyclerView
        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter(courseList);
        rvCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCourses.setAdapter(courseAdapter);

        // Sự kiện click
        btnCompleted.setOnClickListener(v -> fetchCourses("completed"));
        btnOngoing.setOnClickListener(v -> fetchCourses("ongoing"));
        btnUpcoming.setOnClickListener(v -> fetchCourses("upcoming"));

        return view;
    }

    private void fetchCourses(String type) {
        NetworkUtils networkUtils = SupabaseClientHelper.getNetworkUtils();
        String query = "";

        switch (type) {
            case "completed":
                query = "select=*&end_date=lt.now()";
                break;
            case "ongoing":
                query = "select=*&start_date=lte.now()&end_date=gte.now()";
                break;
            case "upcoming":
                query = "select=*&start_date=gt.now()&payment_status=eq.paid";
                break;
        }

        networkUtils.select("Course", query).thenAccept(response -> {
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    courseList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseObject = jsonArray.getJSONObject(i);
                        Course course = new Course(
                                courseObject.getString("id"),
                                courseObject.optInt("teacher_id", 0),
                                courseObject.getString("name"),
                                R.drawable.ic_course_placeholder, // Placeholder icon
                                courseObject.optString("description", ""),
                                courseObject.optInt("max_students", 0),
                                courseObject.optString("status", ""),
                                courseObject.optString("start_date", ""),
                                courseObject.optString("end_date", ""),
                                courseObject.optString("price", "")
                        );
                        courseList.add(course);
                    }

                    // Cập nhật giao diện
                    getActivity().runOnUiThread(() -> courseAdapter.notifyDataSetChanged());
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing courses", e);
                    showErrorToast();
                }
            } else {
                showErrorToast();
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Error fetching courses", throwable);
            showErrorToast();
            return null;
        });
    }

    private void showErrorToast() {
        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Lỗi khi tải danh sách khóa học", Toast.LENGTH_SHORT).show());
    }
}
