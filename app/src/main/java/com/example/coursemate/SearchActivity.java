package com.example.coursemate;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursemate.adapter.SearchAdapter;
import com.example.coursemate.SupabaseClientHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private RecyclerView recyclerViewSearchResults;
    private SearchAdapter searchAdapter;
    private List<String> courseNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        // Khởi tạo các view
        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults);

        // Thiết lập RecyclerView
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        courseNameList = new ArrayList<>();
        searchAdapter = new SearchAdapter(courseNameList);
        recyclerViewSearchResults.setAdapter(searchAdapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tìm kiếm khóa học");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        // Thiết lập TextWatcher để thực hiện tìm kiếm
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchAdapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Gọi API để hiển thị danh sách mặc định
        fetchCoursesFromSupabase();
    }


    private void fetchCoursesFromSupabase() {
        // Sử dụng SupabaseClientHelper để gọi API
        String query = "select=name";
        SupabaseClientHelper.getNetworkUtils().select("Course", query).thenAccept(response -> {
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    courseNameList.clear(); // Xóa danh sách cũ
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String courseName = jsonObject.optString("name", "Unnamed Course");
                        courseNameList.add(courseName);
                    }
                    // Cập nhật RecyclerView
                    runOnUiThread(() -> {
                        searchAdapter.filter(""); // Hiển thị toàn bộ danh sách
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}
