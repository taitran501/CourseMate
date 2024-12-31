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
import com.example.coursemate.adapter.ScheduleAdapter;
import com.example.coursemate.model.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.CompletableFuture;

public class FragmentSchedule extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private ArrayList<Schedule> scheduleList;

    private static final String TAG = "FragmentSchedule";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rv_schedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize schedule list and adapter
        scheduleList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(scheduleList, getContext());
        recyclerView.setAdapter(scheduleAdapter);

        // Initialize CalendarView
        calendarView = view.findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> fetchCoursesForDate(year, month, dayOfMonth));

        return view;
    }

    private void fetchCoursesForDate(int year, int month, int dayOfMonth) {
        scheduleList.clear();
        scheduleAdapter.notifyDataSetChanged(); // Clear RecyclerView immediately

        int dayOfWeek = (dayOfMonth % 7); // Calculate day of the week
        String scheduleQuery = "select=id,day,start_time,end_time,course_id,classroom_id&day=eq." + dayOfWeek;

        NetworkUtils networkUtils = SupabaseClientHelper.getNetworkUtils();

        networkUtils.select("Schedule", scheduleQuery).thenAccept(response -> {
            if (response != null && !response.isEmpty()) {
                try {
                    JSONArray scheduleArray = new JSONArray(response);
                    if (scheduleArray.length() == 0) {
                        Log.d(TAG, "No schedules found for the selected date.");
                        scheduleList.clear();
                        getActivity().runOnUiThread(() -> scheduleAdapter.notifyDataSetChanged());
                        return;
                    }

                    for (int i = 0; i < scheduleArray.length(); i++) {
                        JSONObject scheduleObject = scheduleArray.getJSONObject(i);

                        String startTime = scheduleObject.optString("start_time", "N/A");
                        String endTime = scheduleObject.optString("end_time", "N/A");
                        String courseId = scheduleObject.optString("course_id");
                        String classroomId = scheduleObject.optString("classroom_id");

                        fetchCourseAndTeacher(networkUtils, courseId, classroomId, startTime, endTime, year, month, dayOfMonth);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing Schedule: ", e);
                }
            } else {
                Log.d(TAG, "No schedules found for the selected date.");
                scheduleList.clear();
                getActivity().runOnUiThread(() -> scheduleAdapter.notifyDataSetChanged());
            }
        });
    }

    private void fetchCourseAndTeacher(NetworkUtils networkUtils, String courseId, String classroomId, String startTime, String endTime, int year, int month, int dayOfMonth) {
        String courseQuery = "select=name,teacher_id,end_date&id=eq." + courseId;

        networkUtils.select("Course", courseQuery).thenAccept(courseResponse -> {
            if (courseResponse != null && !courseResponse.isEmpty()) {
                try {
                    JSONObject courseObject = new JSONArray(courseResponse).getJSONObject(0);
                    String courseName = courseObject.optString("name", "No Course Name");
                    String teacherId = courseObject.optString("teacher_id");
                    String endDate = courseObject.optString("end_date");

                    if (isDateAfterEndDate(year, month, dayOfMonth, endDate)) {
                        Log.d(TAG, "Selected date is after course end date. Skipping.");
                        return;
                    }

                    fetchTeacherAndClassroom(networkUtils, teacherId, classroomId, courseName, startTime, endTime);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing Course: ", e);
                }
            }
        });
    }

    private void fetchTeacherAndClassroom(NetworkUtils networkUtils, String teacherId, String classroomId, String courseName, String startTime, String endTime) {
        String userQuery = "select=partner_id&id=eq." + teacherId; // Query partner_id from User table
        String classroomQuery = "select=name&id=eq." + classroomId;

        CompletableFuture<String> userFuture = networkUtils.select("User", userQuery);
        CompletableFuture<String> classroomFuture = networkUtils.select("Classroom", classroomQuery);

        CompletableFuture.allOf(userFuture, classroomFuture).thenRun(() -> {
            try {
                String teacherName = "Unknown Teacher";
                String classroomName = "Unknown Room";

                // Get partner_id from User table
                if (userFuture.get() != null && !userFuture.get().isEmpty()) {
                    JSONArray userArray = new JSONArray(userFuture.get());
                    if (userArray.length() > 0) {
                        JSONObject userObject = userArray.getJSONObject(0);
                        String partnerId = userObject.optString("partner_id");

                        // Query Partner to get teacherName
                        String partnerQuery = "select=name&id=eq." + partnerId;
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

                // Get classroomName from Classroom table
                if (classroomFuture.get() != null && !classroomFuture.get().isEmpty()) {
                    JSONArray classroomArray = new JSONArray(classroomFuture.get());
                    if (classroomArray.length() > 0) {
                        JSONObject classroomObject = classroomArray.getJSONObject(0);
                        classroomName = classroomObject.optString("name", "Unknown Room");
                    }
                }

                // Create Schedule object and add to the list
                Schedule schedule = new Schedule(courseName, teacherName, startTime, endTime, classroomName);
                scheduleList.add(schedule);

                // Update RecyclerView
                getActivity().runOnUiThread(() -> scheduleAdapter.notifyDataSetChanged());

            } catch (Exception e) {
                Log.e(TAG, "Error combining Teacher and Classroom: ", e);
            }
        });
    }

    private boolean isDateAfterEndDate(int year, int month, int day, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date selectedDate = new GregorianCalendar(year, month, day).getTime();
            Date courseEndDate = sdf.parse(endDate);

            return selectedDate.after(courseEndDate);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing date: ", e);
            return false;
        }
    }
}
