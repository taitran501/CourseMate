package com.example.coursemate.fragment;

import android.content.SharedPreferences;
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

import com.example.coursemate.R;
import com.example.coursemate.SupabaseClientHelper;
import com.example.coursemate.adapter.ScheduleAdapter;
import com.example.coursemate.model.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> fetchStudentSchedule(year, month, dayOfMonth));

        return view;
    }

    private String formatTime(String time) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return outputFormat.format(inputFormat.parse(time));
        } catch (Exception e) {
            return time; // Nếu xảy ra lỗi, trả về thời gian gốc
        }
    }

    private void fetchStudentSchedule(int year, int month, int dayOfMonth) {
        scheduleList.clear();
        scheduleAdapter.notifyDataSetChanged();

        String userId = getUserId(); // Lấy user_id từ SharedPreferences
        if (userId == null) {
            Log.e(TAG, "User ID is null. Please log in again.");
            return;
        }

        String rpcUrl = SupabaseClientHelper.getNetworkUtils().getBaseUrl() + "rpc/get_student_schedule";
        Log.d(TAG, "RPC URL: " + rpcUrl);
        JSONObject requestBody = new JSONObject();
        try {
            String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            requestBody.put("student_id", userId);
            requestBody.put("selected_date", selectedDate);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating request body", e);
            return;
        }
        Log.d(TAG, "Fetching student schedule for date: " + requestBody.toString());
        SupabaseClientHelper.getNetworkUtils().post(rpcUrl, requestBody).thenAccept(response -> {
            if (response != null && !response.isEmpty()) {
                try {
                    JSONArray resultArray = new JSONArray(response);
                    if (resultArray.length() == 0) {
                        Log.d(TAG, "No schedules found for the selected date.");
                    }
                    scheduleList.clear();
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject obj = resultArray.getJSONObject(i);
                        Schedule schedule = new Schedule(
                                obj.optString("course_name"),
                                obj.optString("teacher_name"),
                                formatTime(obj.optString("start_time")),
                                formatTime(obj.optString("end_time")),
                                obj.optString("classroom_name", "Unknown Room")
                        );
                        scheduleList.add(schedule);
                    }
                    getActivity().runOnUiThread(() -> scheduleAdapter.notifyDataSetChanged());
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing student schedule: ", e);
                }
            } else {
                Log.d(TAG, "Empty or invalid response received from the server.");
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Failed to fetch student schedule", throwable);
            return null;
        });

    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_session", getActivity().MODE_PRIVATE);
        return sharedPreferences.getString("user_id", null); // Trả về user_id từ SharedPreferences
    }
}
