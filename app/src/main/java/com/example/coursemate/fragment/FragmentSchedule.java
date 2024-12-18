package com.example.coursemate.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemate.NetworkUtils;
import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import com.example.coursemate.adapter.CourseAdapter;
import com.example.coursemate.model.Course;
import com.example.coursemate.model.Schedule;
import com.example.coursemate.adapter.ScheduleAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FragmentSchedule extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter; // Adapter cho RecyclerView
    private ArrayList<Schedule> scheduleList; // Danh sách dữ liệu

    private static final String TAG = "FragmentSchedule";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.rv_schedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo danh sách và adapter
        scheduleList = new ArrayList<>(); // Khởi tạo danh sách rỗng
        scheduleAdapter = new ScheduleAdapter(scheduleList, getContext()); // Khởi tạo adapter
        recyclerView.setAdapter(scheduleAdapter); // Gắn adapter vào RecyclerView

        // Khởi tạo CalendarView
        calendarView = view.findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            fetchCoursesForDate(year, month, dayOfMonth);
        });

        return view;
    }

    private void fetchCoursesForDate(int year, int month, int dayOfMonth) {
        scheduleList.clear();

        int dayOfWeek = (dayOfMonth % 7); // Tính ngày trong tuần
        String scheduleQuery = "select=id,day,start_time,end_time,course_id,classroom_id&day=eq." + dayOfWeek;

        NetworkUtils networkUtils = SupabaseClientHelper.getNetworkUtils();

        networkUtils.select("Schedule", scheduleQuery).thenAccept(response -> {
            if (response != null && !response.isEmpty()) {
                try {
                    JSONArray scheduleArray = new JSONArray(response);
                    for (int i = 0; i < scheduleArray.length(); i++) {
                        JSONObject scheduleObject = scheduleArray.getJSONObject(i);

                        String startTime = scheduleObject.optString("start_time", "N/A");
                        String endTime = scheduleObject.optString("end_time", "N/A");
                        String courseId = scheduleObject.optString("course_id");
                        String classroomId = scheduleObject.optString("classroom_id");

                        fetchCourseAndTeacher(networkUtils, courseId, classroomId, startTime, endTime);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing Schedule: ", e);
                }
            }
        });
    }

    private void fetchCourseAndTeacher(NetworkUtils networkUtils, String courseId, String classroomId, String startTime, String endTime) {
        String courseQuery = "select=name,teacher_id&id=eq." + courseId;

        networkUtils.select("Course", courseQuery).thenAccept(courseResponse -> {
            if (courseResponse != null && !courseResponse.isEmpty()) {
                try {
                    JSONObject courseObject = new JSONArray(courseResponse).getJSONObject(0);
                    String courseName = courseObject.optString("name", "No Course Name");
                    String teacherId = courseObject.optString("teacher_id");

                    fetchTeacherAndClassroom(networkUtils, teacherId, classroomId, courseName, startTime, endTime);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing Course: ", e);
                }
            }
        });
    }

    private void fetchTeacherAndClassroom(NetworkUtils networkUtils, String teacherId, String classroomId, String courseName, String startTime, String endTime) {
        String userQuery = "select=partner_id&id=eq." + teacherId; // Truy vấn partner_id từ bảng User
        String classroomQuery = "select=name&id=eq." + classroomId;

        // Log truy vấn
        Log.d(TAG, "User Query: " + userQuery);
        Log.d(TAG, "Classroom Query: " + classroomQuery);

        CompletableFuture<String> userFuture = networkUtils.select("User", userQuery);
        CompletableFuture<String> classroomFuture = networkUtils.select("Classroom", classroomQuery);

        CompletableFuture.allOf(userFuture, classroomFuture).thenRun(() -> {
            try {
                String teacherName = "Unknown Teacher";
                String classroomName = "Unknown Room";

                // Lấy partner_id từ bảng User
                if (userFuture.get() != null && !userFuture.get().isEmpty()) {
                    JSONArray userArray = new JSONArray(userFuture.get());
                    if (userArray.length() > 0) {
                        JSONObject userObject = userArray.getJSONObject(0);
                        String partnerId = userObject.optString("partner_id");

                        // Truy vấn Partner để lấy teacherName
                        String partnerQuery = "select=name&id=eq." + partnerId;
                        Log.d(TAG, "Partner Query: " + partnerQuery);

                        String partnerResponse = networkUtils.select("Partner", partnerQuery).get();
                        if (partnerResponse != null && !partnerResponse.isEmpty()) {
                            JSONArray partnerArray = new JSONArray(partnerResponse);
                            if (partnerArray.length() > 0) {
                                JSONObject partnerObject = partnerArray.getJSONObject(0);
                                teacherName = partnerObject.optString("name", "Unknown Teacher");
                            }
                        }
                    }
                }

                // Lấy classroomName từ bảng Classroom
                if (classroomFuture.get() != null && !classroomFuture.get().isEmpty()) {
                    JSONArray classroomArray = new JSONArray(classroomFuture.get());
                    if (classroomArray.length() > 0) {
                        JSONObject classroomObject = classroomArray.getJSONObject(0);
                        classroomName = classroomObject.optString("name", "Unknown Room");
                    }
                }

                // Tạo đối tượng Schedule và thêm vào danh sách
                Schedule schedule = new Schedule(courseName, teacherName, startTime, endTime, classroomName);
                scheduleList.add(schedule);

                // Cập nhật RecyclerView
                getActivity().runOnUiThread(() -> scheduleAdapter.notifyDataSetChanged());

            } catch (Exception e) {
                Log.e(TAG, "Error combining Teacher and Classroom: ", e);
            }
        });
    }

}
