package com.example.coursemate;

import java.util.Map;

public class SupabaseClientHelper {

    private static final String BASE_URL = "https://wdqbztkgselheodomnse.supabase.co/rest/v1/";
    private static final String AUTH_BASE_URL = "https://wdqbztkgselheodomnse.supabase.co/auth/v1";

    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndkcWJ6dGtnc2VsaGVvZG9tbnNlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzA4OTM5MzUsImV4cCI6MjA0NjQ2OTkzNX0.L6lu74ufICA5kvwl3UhHSRejwOzRumunQa2oakltS7c";
    private static NetworkUtils networkUtils, authNetworkUtils;

    // Phương thức để lấy đối tượng NetworkUtils (Supabase Client)
    public static NetworkUtils getNetworkUtils() {
        if (networkUtils == null) {
            Map<String, String> headers = Map.of(
                    "Authorization", "Bearer " + API_KEY,
                    "apikey", API_KEY
            );
            networkUtils = new NetworkUtils(BASE_URL, headers);
        }
        return networkUtils;
    }

    // Phương thức để lấy đối tượng NetworkUtils (dành cho AUTH API với AUTH_BASE_URL)
    public static NetworkUtils getAuthNetworkUtils() {
        if (authNetworkUtils == null) {
            Map<String, String> headers = Map.of(
                    "Authorization", "Bearer " + API_KEY,
                    "apikey", API_KEY
            );
            authNetworkUtils = new NetworkUtils(AUTH_BASE_URL, headers);
        }
        return authNetworkUtils;
    }

    public static String getApiKey() {
        return API_KEY;
    }
}
