package com.example.coursemate.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coursemate.ChangePasswordActivity;
import com.example.coursemate.R;
import com.example.coursemate.auth.Login;

public class FragmentSetting extends Fragment {

    private LinearLayout btnEditInfo, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho FragmentSetting
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Ánh xạ các nút
        btnEditInfo = view.findViewById(R.id.btn_edit_info);
        btnLogout = view.findViewById(R.id.btn_logout);

        // Xử lý sự kiện nút "Chỉnh sửa thông tin"
        btnEditInfo.setOnClickListener(v -> {
            // Chuyển đến màn hình ChangePasswordActivity
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện nút "Đăng xuất"
        btnLogout.setOnClickListener(v -> handleLogout());

        return view;
    }

    private void handleLogout() {
        // Xóa thông tin đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Hiển thị thông báo
        Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

        // Chuyển đến màn hình đăng nhập
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);

        // Kết thúc hoạt động hiện tại
        requireActivity().finish();
    }
}
