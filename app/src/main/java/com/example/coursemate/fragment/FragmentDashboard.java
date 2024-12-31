package com.example.coursemate.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.coursemate.SupabaseClientHelper;

import org.json.JSONArray;
import org.json.JSONObject;

public class FragmentDashboard extends Fragment {

    private static final String TAG = "FragmentDashboard";

    private TextView tvWelcome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_dashboard, container, false);

        // Khởi tạo TextView
        tvWelcome = view.findViewById(R.id.tv_welcome);

        // Lấy thông tin từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String partnerId = sharedPreferences.getString("partner_id", null);
        String username = sharedPreferences.getString("username", "Unknown User");

        if (partnerId != null) {
            fetchPartnerAndDisplayName(partnerId, username);
        } else {
            tvWelcome.setText("Xin chào, " + username);
            Toast.makeText(requireContext(), "Không tìm thấy partner_id trong SharedPreferences", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Không tìm thấy partner_id trong SharedPreferences. Username: " + username);
        }


        return view;
    }

    /**
     * Truy vấn thông tin đối tác (Partner) từ Supabase bằng partner_id
     * và hiển thị cả username từ bảng User.
     */
    private void fetchPartnerAndDisplayName(String partnerId, String username) {
        Log.d(TAG, "Đang sử dụng partner_id để truy vấn: " + partnerId);

        NetworkUtils networkUtils = SupabaseClientHelper.getNetworkUtils();
        String queryPartner = "select=name&id=eq." + partnerId;

        networkUtils.select("Partner", queryPartner).thenAccept(response -> {
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    // Kiểm tra nếu mảng trả về rỗng
                    if (jsonArray.length() > 0) {
                        JSONObject partner = jsonArray.getJSONObject(0);
                        String partnerName = partner.optString("name", "Partner");

                        // Cập nhật giao diện với tên đối tác (Partner name) và username
                        requireActivity().runOnUiThread(() ->
                                tvWelcome.setText("Xin chào, " + partnerName)
                        );
                        Log.d(TAG, "Lấy thông tin đối tác thành công: " + partnerName);
                    } else {
                        // Xử lý lỗi nếu không tìm thấy đối tác
                        requireActivity().runOnUiThread(() -> {
                            tvWelcome.setText("Xin chào, " + username);
                            Toast.makeText(requireContext(), "Không tìm thấy đối tác với partner_id: " + partnerId, Toast.LENGTH_SHORT).show();
                        });
                        Log.e(TAG, "Không tìm thấy thông tin đối tác cho partner_id: " + partnerId);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Lỗi xử lý dữ liệu JSON từ đối tác", e);
                    requireActivity().runOnUiThread(() -> {
                        tvWelcome.setText("Xin chào, " + username);
                        Toast.makeText(requireContext(), "Lỗi khi xử lý dữ liệu đối tác", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                Log.e(TAG, "Không thể kết nối tới máy chủ để lấy thông tin đối tác");
                requireActivity().runOnUiThread(() -> {
                    tvWelcome.setText("Xin chào, " + username);
                    Toast.makeText(requireContext(), "Không thể kết nối tới máy chủ", Toast.LENGTH_SHORT).show();
                });
            }
        }).exceptionally(throwable -> {
            Log.e(TAG, "Lỗi khi truy vấn thông tin đối tác", throwable);
            requireActivity().runOnUiThread(() -> {
                tvWelcome.setText("Xin chào, " + username);
                Toast.makeText(requireContext(), "Đã xảy ra lỗi khi truy vấn thông tin đối tác", Toast.LENGTH_SHORT).show();
            });
            return null;
        });
    }
}
