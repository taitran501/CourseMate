package com.example.coursemate;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

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

    // Post method
    public CompletableFuture<String> post(String urlString, JSONObject requestBody) {
        return CompletableFuture.supplyAsync(() -> {
            HttpURLConnection connection = null;
            try {
                // Mở kết nối
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + SupabaseClientHelper.getApiKey());
                connection.setRequestProperty("apikey", SupabaseClientHelper.getApiKey());
                connection.setDoOutput(true);

                // Ghi dữ liệu body
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(requestBody.toString().getBytes());
                    os.flush();
                }

                // Kiểm tra phản hồi
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
                } else {
                    Log.e("NetworkUtils", "POST request failed. Response Code: " + responseCode);
                    return null;
                }
            } catch (Exception e) {
                Log.e("NetworkUtils", "Error during POST request", e);
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
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

                Log.d("NetworkUtils", "Attempting connection to " + baseUrl + table + "?" + query);
                // Kết nối HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                Log.d("NetworkUtils", "Connection established");
                // Thêm headers
//                setHeaders(connection);
                for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                    Log.d("NetworkUtils", "Header: " + entry.getKey() + " = " + entry.getValue());
                }
                connection.setRequestProperty("Content-Type", "application/json");

                Log.d("NetworkUtils", "Headers set");

                // Kiểm tra phản hồi
//                Thread.sleep(1000);
                int responseCode = connection.getResponseCode();
                Log.d("NetworkUtils", "Response code: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Đọc kết quả trả về
                    Log.d("NetworkUtils", "Reading response");
                    return readStream(connection);
                } else if (isRedirect(responseCode)) {
                    return handleRedirect(connection);
                } else {
                    Log.e("NetworkUtils", "GET request failed. Response Code: " + responseCode);
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
                if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("NetworkUtils", "POST request succeeded. Response Code: " + responseCode);
                } else if (isRedirect(responseCode)) {
                    handleRedirect(connection);
                } else {
                    Log.e("NetworkUtils", "POST request failed. Response Code: " + responseCode);
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
     * Kiểm tra xem phản hồi có phải là chuyển hướng hay không.
     */
    private boolean isRedirect(int responseCode) {
        return responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                responseCode == HttpURLConnection.HTTP_SEE_OTHER;
    }

    /**
     * Xử lý chuyển hướng nếu phản hồi có mã chuyển hướng.
     */
    private String handleRedirect(HttpURLConnection connection) throws Exception {
        String redirectUrl = connection.getHeaderField("Location");
        if (redirectUrl == null) {
            throw new Exception("Redirect URL is null");
        }
        Log.d("NetworkUtils", "Redirecting to: " + redirectUrl);
        URL newUrl = new URL(redirectUrl);
        HttpURLConnection redirectConnection = (HttpURLConnection) newUrl.openConnection();
        setHeaders(redirectConnection);
        redirectConnection.setRequestMethod("GET");

        int responseCode = redirectConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return readStream(redirectConnection);
        } else {
            Log.e("NetworkUtils", "Redirected request failed. Response Code: " + responseCode);
            throw new Exception("Redirected request failed. Response Code: " + responseCode);
        }
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

    public CompletableFuture<Void> updatePassword(String username, String newPassword) {
        return CompletableFuture.runAsync(() -> {
            try {
                String query = "username=eq." + username;
                String jsonBody = "{\"password\": \"" + newPassword + "\"}";
                URL url = new URL(baseUrl + "User?" + query);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PATCH");
                connection.setDoOutput(true);

                setHeaders(connection);
                connection.setRequestProperty("Content-Type", "application/json");

                writeStream(connection, jsonBody);

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
                    throw new Exception("PATCH request failed. Response Code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e("NetworkUtils", "Error in updatePassword: ", e);
            }
        });
    }

    private void fetchRegisteredCourses(String studentId) {
        String query = "select=registration_id,course_id,course_name,course_description,payment_status,amount,registration_date" +
                "&student_id=eq." + studentId;

        SupabaseClientHelper.getNetworkUtils().select("CourseRegistration", query).thenAccept(response -> {
            if (response != null && !response.isEmpty()) {
                try {
                    JSONArray registrations = new JSONArray(response);
                    for (int i = 0; i < registrations.length(); i++) {
                        JSONObject registration = registrations.getJSONObject(i);
                        String courseName = registration.getString("course_name");
                        String paymentStatus = registration.getString("payment_status");
                        Log.d("FetchCourses", "Course: " + courseName + ", Status: " + paymentStatus);
                    }
                } catch (Exception e) {
                    Log.e("FetchCourses", "Error parsing response", e);
                }
            } else {
                Log.e("FetchCourses", "No data found for studentId: " + studentId);
            }
        }).exceptionally(throwable -> {
            Log.e("FetchCourses", "Error fetching registered courses", throwable);
            return null;
        });
    }

}
