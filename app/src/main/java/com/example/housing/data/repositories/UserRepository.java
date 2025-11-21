package com.example.housing.data.repositories;

import android.util.Log;

import com.example.housing.data.supabase.AuthManager;
import com.example.housing.data.supabase.SupabaseApi;
import com.example.housing.data.supabase.SupabaseApiClient;
import com.example.housing.data.supabase.SupabaseConfig;
import com.example.housing.models.User;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles data operations for the 'users' table (profiles).
 * Manages the crucial step of creating a profile upon first sign-in.
 */
public class UserRepository {

    private static final String TAG = "UserRepository";
    private final SupabaseApi apiService;
    private final AuthManager authManager;
    private final String apiKey = SupabaseConfig.SUPABASE_ANON_KEY;
    private static final String SCHEMA = "public"; // Use your schema name

    public interface ProfileCallback {
        void onSuccess(User user);
        void onFailure(String error);
    }

    public UserRepository() {
        this.apiService = SupabaseApiClient.getClient().create(SupabaseApi.class);
        this.authManager = AuthManager.getInstance();
    }

    /**
     * Main entry point: Fetches the profile if it exists, otherwise creates a default one.
     */
    public void getOrCreateProfile(ProfileCallback callback) {
        String userId = authManager.getUserId();
        String authToken = authManager.getAuthToken();

        if (userId.isEmpty() || authToken.isEmpty()) {
            callback.onFailure("User not authenticated.");
            return;
        }

        // Try to fetch existing profile
        fetchProfile(userId, authToken, new ProfileCallback() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(String error) {
                // If fetch fails (likely profile not found), create it
                Log.w(TAG, "Profile not found or fetch failed. Attempting to create new profile.");
                createNewProfile(userId, authToken, callback);
            }
        });
    }

    private void fetchProfile(String userId, String authToken, ProfileCallback callback) {
        String authHeader = "Bearer " + authToken;

        Call<List<User>> call = apiService.getUserProfile(
                apiKey,
                authHeader,
                SCHEMA,
                "eq." + userId
        );

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    callback.onSuccess(response.body().get(0));
                } else {
                    // Fail the profile fetch to trigger the createNewProfile flow
                    callback.onFailure("Profile not found.");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onFailure("Network Failure: " + t.getMessage());
            }
        });
    }

    private void createNewProfile(String userId, String authToken, ProfileCallback callback) {
        String authHeader = "Bearer " + authToken;

        // Create a minimal profile object with default role (tenant)
        User newProfile = new User(
                userId,
                "New User",
                "",
                "tenant"
        );

        Call<User> call = apiService.createUserProfile(
                apiKey,
                authHeader,
                SCHEMA,
                SupabaseApi.PREFER_RETURN_REPRESENTATION,
                newProfile
        );

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "New profile created for user: " + userId);
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        callback.onFailure("Profile creation failed: " + response.code() + " - " + errorBody);
                    } catch (IOException e) {
                        callback.onFailure("Profile creation failed: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure("Profile creation network failure: " + t.getMessage());
            }
        });
    }
}