package com.example.coursemate;

import android.os.Bundle;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.coursemate.adapter.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText editTextSearch;
    private RecyclerView recyclerViewSearchResults;
    private SearchAdapter searchAdapter;
    private List<String> dataList; // Dữ liệu giả lập, bạn có thể thay bằng dữ liệu thực tế
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults);

        // Tham chiếu Toolbar từ layout
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tìm kiếm");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Xử lý sự kiện nút quay lại
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Dữ liệu mẫu
        dataList = new ArrayList<>();
        dataList.add("Lập trình Java");
        dataList.add("Thiết kế đồ họa");
        dataList.add("Trí tuệ nhân tạo");
        dataList.add("Phân tích dữ liệu");

        // Setup RecyclerView
        searchAdapter = new SearchAdapter(dataList);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearchResults.setAdapter(searchAdapter);

        // Xử lý sự kiện tìm kiếm
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kiểm tra độ dài chuỗi trước khi tìm kiếm
                if (s.length() >= 3) { // Chỉ tìm kiếm nếu chuỗi >= 3 ký tự
                    searchAdapter.filter(s.toString());
                } else {
                    // Nếu ít hơn 3 ký tự, xóa kết quả hoặc hiển thị danh sách trống
                    searchAdapter.filter(""); // Hoặc tùy chỉnh logic hiển thị danh sách trống
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }
}
