package com.example.coursemate;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NetworkUtils {
    private final String baseUrl;
    private final Map<String, String> headers;

    public NetworkUtils(String baseUrl, Map<String, String> headers) {
        this.baseUrl = baseUrl;
        this.headers = headers;
    }

    /**
     * Thực hiện một yêu cầu GET (SELECT).
     *
     * @param table tên bảng cần truy vấn.
     * @param query chuỗi truy vấn (SQL-like, ví dụ: "select=id,name").
     * @return CompletableFuture trả về dữ liệu JSON dạng chuỗi.
     */
    public CompletableFuture<String> select(String table, String query) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Tạo URL với truy vấn
                URL url = new URL(baseUrl + table + "?" + query);

                // Kết nối HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Thêm headers
                setHeaders(connection);

                // Kiểm tra phản hồi
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Đọc kết quả trả về
                    return readStream(connection);
                } else {
                    throw new Exception("GET request failed. Response Code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e("NetworkUtils", "Error in SELECT: ", e);
                return null;
            }
        });
    }

    /**
     * Thực hiện một yêu cầu POST (INSERT).
     *
     * @param table tên bảng.
     * @param jsonBody chuỗi JSON chứa dữ liệu cần chèn.
     */
    public CompletableFuture<Void> insert(String table, String jsonBody) {
        return CompletableFuture.runAsync(() -> {
            try {
                // Tạo URL
                URL url = new URL(baseUrl + table);

                // Kết nối HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // Thêm headers
                setHeaders(connection);
                connection.setRequestProperty("Content-Type", "application/json");

                // Gửi dữ liệu JSON
                writeStream(connection, jsonBody);

                // Kiểm tra phản hồi
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_CREATED) {
                    throw new Exception("POST request failed. Response Code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e("NetworkUtils", "Error in INSERT: ", e);
            }
        });
    }

    /**
     * Thực hiện một yêu cầu PATCH (UPDATE).
     *
     * @param table tên bảng.
     * @param query chuỗi truy vấn (SQL-like, ví dụ: "id=eq.1").
     * @param jsonBody chuỗi JSON chứa dữ liệu cần cập nhật.
     */
    public CompletableFuture<Void> update(String table, String query, String jsonBody) {
        return CompletableFuture.runAsync(() -> {
            try {
                // Tạo URL với truy vấn
                URL url = new URL(baseUrl + table + "?" + query);

                // Kết nối HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PATCH");
                connection.setDoOutput(true);

                // Thêm headers
                setHeaders(connection);
                connection.setRequestProperty("Content-Type", "application/json");

                // Gửi dữ liệu JSON
                writeStream(connection, jsonBody);

                // Kiểm tra phản hồi
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
                    throw new Exception("PATCH request failed. Response Code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e("NetworkUtils", "Error in UPDATE: ", e);
            }
        });
    }

    /**
     * Thực hiện một yêu cầu DELETE.
     *
     * @param table tên bảng.
     * @param query chuỗi truy vấn (SQL-like, ví dụ: "id=eq.1").
     */
    public CompletableFuture<Void> delete(String table, String query) {
        return CompletableFuture.runAsync(() -> {
            try {
                // Tạo URL với truy vấn
                URL url = new URL(baseUrl + table + "?" + query);

                // Kết nối HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");

                // Thêm headers
                setHeaders(connection);

                // Kiểm tra phản hồi
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
                    throw new Exception("DELETE request failed. Response Code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e("NetworkUtils", "Error in DELETE: ", e);
            }
        });
    }

    /**
     * Đọc phản hồi từ kết nối HTTP.
     */
    private String readStream(HttpURLConnection connection) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    /**
     * Gửi dữ liệu qua output stream.
     */
    private void writeStream(HttpURLConnection connection, String data) throws Exception {
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = data.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }
    }

    /**
     * Thêm headers vào yêu cầu HTTP.
     */
    private void setHeaders(HttpURLConnection connection) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
