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
    String url;
    Map<String, String> headers;

    public NetworkUtils(String url, Map<String, String> headers){
        this.url = url;
        this.headers = headers;
    }

    public String getUrl(){
        return url;
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public CompletableFuture<String> select(String table, String query) {
        return CompletableFuture.supplyAsync(() -> {
            String response = "";
            try {
                // Create URL object
                URL url = new URL(this.url + table + "?" + query);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Set the headers
                for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }

                // Get the response code
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder responseBuilder = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }
                    in.close();
                    response = responseBuilder.toString();
                } else {
                    response = "GET request failed. Response Code: " + responseCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        });

    }



    public void insert(final String table, String dataString) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // URL for the POST request
                String jsonInputString = dataString;
                Log.d("JsonString", jsonInputString);

                try {
                    // Create the URL object
                    URL urlObj = new URL(NetworkUtils.this.url + table);

                    // Open connection
                    HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

                    // Set HTTP method to POST
                    connection.setRequestMethod("POST");

                    // Set the headers
                    for (Map.Entry<String, String> entry : NetworkUtils.this.headers.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Prefer", "return=minimal");

                    // Enable output stream for sending data
                    connection.setDoOutput(true);

                    // Write the data to the output stream
                    try (OutputStream outputStream = connection.getOutputStream()) {
                        outputStream.write(jsonInputString.getBytes());
                        outputStream.flush();
                    }

                    // Read the response
                    int responseCode = connection.getResponseCode();
                    Log.d("Response Code: ", String.valueOf(responseCode));

                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        Log.d("Response: ", response.toString());
                    }
                    connection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void update(final String table, String query, String dataString) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // URL for the POST request
                String jsonInputString = dataString;
                Log.d("JsonString", jsonInputString);

                try {
                    // Create the URL object
                    URL urlObj = new URL(NetworkUtils.this.url + table + "?" + query);

                    // Open connection
                    HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

                    // Set HTTP method to POST
                    connection.setRequestMethod("PATCH");

                    // Set the headers
                    for (Map.Entry<String, String> entry : NetworkUtils.this.headers.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Prefer", "return=minimal");

                    // Enable output stream for sending data
                    connection.setDoOutput(true);

                    // Write the data to the output stream
                    try (OutputStream outputStream = connection.getOutputStream()) {
                        outputStream.write(jsonInputString.getBytes());
                        outputStream.flush();
                    }

                    // Read the response
                    int responseCode = connection.getResponseCode();
                    Log.d("Response Code: ", String.valueOf(responseCode));

                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        Log.d("Response: ", response.toString());
                    }
                    connection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void delete(final String table, final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // URL for the DELETE request (assuming the ID is part of the URL)
                String urlString = NetworkUtils.this.url + table + "?" + query;

                try {
                    // Create the URL object
                    URL urlObj = new URL(urlString);

                    // Open connection
                    HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

                    // Set HTTP method to DELETE
                    connection.setRequestMethod("DELETE");

                    // Set the headers
                    for (Map.Entry<String, String> entry : NetworkUtils.this.headers.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                    connection.setRequestProperty("Content-Type", "application/json");

                    // Enable input/output streams (even if we're not sending data)
                    connection.setDoOutput(true);

                    // Read the response
                    int responseCode = connection.getResponseCode();
                    Log.d("Response Code: ", String.valueOf(responseCode));

                    // Handle success or failure response
                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                        try (BufferedReader br = new BufferedReader(
                                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                            StringBuilder response = new StringBuilder();
                            String responseLine;
                            while ((responseLine = br.readLine()) != null) {
                                response.append(responseLine.trim());
                            }
                            Log.d("Response: ", response.toString());
                        }
                    } else {
                        // If response code is not 200 or 204, print error stream
                        try (BufferedReader br = new BufferedReader(
                                new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                            StringBuilder errorResponse = new StringBuilder();
                            String errorLine;
                            while ((errorLine = br.readLine()) != null) {
                                errorResponse.append(errorLine.trim());
                            }
                            Log.e("Error Response: ", errorResponse.toString());
                        }
                    }

                    connection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("DELETE Error", String.valueOf(e));
                }
            }
        }).start();
    }


}




    // khoi tao headers

//Map<String, String> headers = Map.of(
//        "Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFuZW1zbmlqcWh0emlrYXR2dm1tIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzA4ODk1MzQsImV4cCI6MjA0NjQ2NTUzNH0.omdglU20eJmYByHg-NGbLhLaY6roUTPTyLEkY54P4DI",
//        "apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFuZW1zbmlqcWh0emlrYXR2dm1tIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzA4ODk1MzQsImV4cCI6MjA0NjQ2NTUzNH0.omdglU20eJmYByHg-NGbLhLaY6roUTPTyLEkY54P4DI"
//);
    // khoi tao url
//String url = "https://anemsnijqhtzikatvvmm.supabase.co/rest/v1/";

    // tao class
//NetworkUtils connector = new NetworkUtils(url, headers);
    // read du lieu
//CompletableFuture<String> future = connector.select("cabins", "select=status,totalPrice&status=eq.unconfirmed");
//select = future.join();
//        Log.d("asasdasd", select);
//        Gson gson = new Gson();
//        Type listType = new TypeToken<List<Student>>(){}.getType();
//        List<Cabin> cabins = gson.fromJson(select, listType);


    // Create du lieu
//        Cabin cb = new Cabin("test", 10, 10, 1000, "description");
//        Gson gson2 = new Gson();
//        String jsonString = gson2.toJson(cb);
//        Log.d("json", jsonString);
//
//        connector.insert("cabins", jsonString);


    // Update du lieu
//Cabin cb = new Cabin("test_update", 10, 10, 1000, "description");
//Gson gson2 = new Gson();
//String jsonString = gson2.toJson(cb);
//        Log.d("json", jsonString);
//
//        connector.update("cabins", "id=eq.36",  jsonString);
    // Delete du lieu
//            connector.delete("cabins", "name=eq.tét");
