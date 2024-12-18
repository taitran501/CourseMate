package com.example.coursemate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.coursemate.NetworkUtils;
import com.example.coursemate.R;
import com.example.coursemate.SearchActivity;
import com.example.coursemate.SupabaseClientHelper;  // Import lớp cấu hình kết nối

import org.json.JSONArray;
import org.json.JSONObject;

public class FragmentDashboard extends Fragment {

    private TextView tvWelcome, tvCourseSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho FragmentDashboard
        View view = inflater.inflate(R.layout.fragment_student_dashboard, container, false);

        // Khởi tạo textView
        tvWelcome = view.findViewById(R.id.tv_welcome);
        tvCourseSearch = view.findViewById(R.id.tv2_course_search);

        // Lấy đối tượng NetworkUtils từ SupabaseClientHelper
        NetworkUtils networkUtils = SupabaseClientHelper.getNetworkUtils();

        // Thiết lập sự kiện click cho tvCourseSearch
        tvCourseSearch.setOnClickListener(v -> {
            // Chuyển sang SearchActivity khi click vào tvCourseSearch
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });

        // Truy vấn bảng User để lấy partner_id của student (role = student)
        String queryUser = "select=id, partner_id&role=eq.student";

        networkUtils.select("User", queryUser).thenAccept(response -> {
            if (response != null) {
                try {
                    JSONArray jsonArrayUser = new JSONArray(response);
                    if (jsonArrayUser.length() > 0) {
                        // Lấy partner_id của student
                        JSONObject user = jsonArrayUser.getJSONObject(0);
                        String partnerId = user.optString("partner_id", "");

                        if (!partnerId.isEmpty()) {
                            // Truy vấn bảng Partner để lấy tên của partner với partner_id
                            String queryPartner = "select=name&id=eq." + partnerId;

                            networkUtils.select("Partner", queryPartner).thenAccept(responsePartner -> {
                                if (responsePartner != null) {
                                    try {
                                        JSONArray jsonArrayPartner = new JSONArray(responsePartner);
                                        if (jsonArrayPartner.length() > 0) {
                                            // Lấy tên partner
                                            JSONObject partner = jsonArrayPartner.getJSONObject(0);
                                            String partnerName = partner.optString("name", "Partner");

                                            // Cập nhật textView với tên partner
                                            getActivity().runOnUiThread(() -> tvWelcome.setText("Xin chào, " + partnerName));
                                        } else {
                                            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Không tìm thấy đối tác", Toast.LENGTH_SHORT).show());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Lỗi khi lấy dữ liệu Partner", Toast.LENGTH_SHORT).show());
                                    }
                                } else {
                                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Không thể kết nối tới bảng Partner", Toast.LENGTH_SHORT).show());
                                }
                            }).exceptionally(throwable -> {
                                throwable.printStackTrace();
                                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Đã xảy ra lỗi khi lấy dữ liệu Partner", Toast.LENGTH_SHORT).show());
                                return null;
                            });
                        } else {
                            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Không có partner_id cho student này", Toast.LENGTH_SHORT).show());
                        }

                    } else {
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Không tìm thấy student với role là 'student'", Toast.LENGTH_SHORT).show());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Lỗi khi lấy dữ liệu User", Toast.LENGTH_SHORT).show());
                }
            } else {
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Không thể kết nối tới máy chủ", Toast.LENGTH_SHORT).show());
            }
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Đã xảy ra lỗi khi lấy dữ liệu User", Toast.LENGTH_SHORT).show());
            return null;
        });

        return view;
    }
}
