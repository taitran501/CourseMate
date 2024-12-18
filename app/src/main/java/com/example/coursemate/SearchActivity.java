package com.example.coursemate;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursemate.adapter.CourseAdapter;
import com.example.coursemate.model.Course;
import androidx.appcompat.widget.Toolbar;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private RecyclerView recyclerViewSearchResults;
    private CourseAdapter courseAdapter;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        // Khởi tạo các view
        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults);

        // Thiết lập RecyclerView
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter(courseList);
        recyclerViewSearchResults.setAdapter(courseAdapter);

        // Khởi tạo Toolbar và thiết lập làm ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập tiêu đề và nút quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tìm kiếm khoá học");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Sự kiện click vào nút quay lại
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Thiết lập TextWatcher để thực hiện tìm kiếm khi người dùng gõ
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Không cần xử lý gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Gọi hàm tìm kiếm mỗi khi người dùng thay đổi nội dung trong EditText
                performSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần xử lý gì ở đây
            }
        });

        // Lấy tất cả các khóa học khi không có từ khóa
        performSearch("");
    }

    // Hàm thực hiện tìm kiếm
    private void performSearch(String query) {
        // Sử dụng NetworkUtils từ SupabaseClientHelper để lấy dữ liệu khóa học
        NetworkUtils networkUtils = SupabaseClientHelper.getNetworkUtils();

        // Nếu không có từ khóa tìm kiếm, lấy tất cả khóa học
        String searchQuery = TextUtils.isEmpty(query) ? "select=id,name,description" : "select=id,name,description&name=ilike.%25" + query + "%25";

        // Thực hiện truy vấn
        networkUtils.select("Course", searchQuery).thenAccept(response -> {
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    courseList.clear(); // Xóa danh sách khóa học cũ

                    if (jsonArray.length() > 0) {
                        // Duyệt qua các kết quả và thêm vào danh sách
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject courseJson = jsonArray.getJSONObject(i);
                            String courseName = courseJson.optString("name", "N/A");
                            String courseDescription = courseJson.optString("description", "No description available");

                            // Thêm vào danh sách khóa học
                            courseList.add(new Course(courseName, courseDescription));
                        }
                        // Cập nhật RecyclerView
                        runOnUiThread(() -> courseAdapter.notifyDataSetChanged());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        // Xử lý lỗi, nếu cần
                    });
                }
            } else {
                runOnUiThread(() -> {
                    // Xử lý trường hợp không có dữ liệu, nếu cần
                });
            }
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            runOnUiThread(() -> {
                // Xử lý lỗi nếu không thể kết nối với máy chủ
            });
            return null;
        });
    }
}
