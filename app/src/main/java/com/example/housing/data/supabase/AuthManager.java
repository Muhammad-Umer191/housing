package com.example.housing.data.supabase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthManager
{
    private static final String TAG = "AuthManager";
    private static final String PREFS_NAME = "SupabaseAuthPrefs";
    private static final String KEY_JWT = "jwt_token";
    private static final String KEY_USER_ID = "user_id";

    private static AuthManager instance;
    private final SharedPreferences prefs;
    private final com.example.housing.data.supabase.AuthService authService;

    // LiveData to observe user's authentication status
    private final MutableLiveData<String> _currentUserId = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isAuthenticated = new MutableLiveData<>();

    // Public LiveData accessors
    public LiveData<String> getCurrentUserId() { return _currentUserId; }
    public LiveData<Boolean> isAuthenticated() { return _isAuthenticated; }

    private AuthManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Initialize the Retrofit Auth Service
        authService = SupabaseApiClient.getClient().create(com.example.housing.data.supabase.AuthService.class);

        // Load persisted state on startup
        loadAuthState();
    }

    public static synchronized void initialize(Context context) {
        if (instance == null) {
            instance = new AuthManager(context);
        }
    }

    public static AuthManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AuthManager must be initialized via initialize(Context) before use.");
        }
        return instance;
    }
    public void signInUser(String email, String password, AuthCallback callback) {
        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("password", password);

        authService.signIn("password", body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleAuthSuccess(response.body());
                    callback.onSuccess();
                } else {
                    handleAuthError(response, callback);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Sign In Network Failure: " + t.getMessage(), t);
                callback.onFailure("Network error during sign in.");
            }
        });
    }

    private void handleAuthSuccess(JsonObject authResponse) {
        String token = authResponse.get("access_token").getAsString();
        JsonObject user = authResponse.get("user").getAsJsonObject();
        String userId = user.get("id").getAsString();

        // 1. Persist to SharedPreferences
        prefs.edit()
                .putString(KEY_JWT, token)
                .putString(KEY_USER_ID, userId)
                .apply();

        // 2. Update LiveData (in-memory state)
        _currentUserId.postValue(userId);
        _isAuthenticated.postValue(true);
        Log.i(TAG, "Auth successful. User ID: " + userId);
    }

    private void handleAuthError(Response<JsonObject> response, AuthCallback callback) {
        try {
            String errorBody;
            if (response.errorBody() != null)
            {
                errorBody = response.errorBody().string();
            }
            else
            {
                errorBody = "Unknown error";
            }
            Log.e(TAG, "Auth API Error: " + response.code() + " - " + errorBody);
            callback.onFailure("Sign In failed: " + response.code());
        } catch (IOException e) {
            callback.onFailure("Sign In failed: HTTP " + response.code());
        }
    }

    private void loadAuthState()
    {
        String userId = prefs.getString(KEY_USER_ID, null);
        String jwtToken = prefs.getString(KEY_JWT, null);

        if (userId != null && jwtToken != null) {
            _currentUserId.setValue(userId); // Use setValue for initial load
            _isAuthenticated.setValue(true);
            Log.i(TAG, "Auth state loaded from storage. User ID: " + userId);
        } else {
            _isAuthenticated.setValue(false);
            Log.i(TAG, "No auth state found.");
        }
    }

    public void signOut() {
        prefs.edit().clear().apply();
        _currentUserId.postValue(null);
        _isAuthenticated.postValue(false);
    }

    public String getAuthToken()
    {
        return prefs.getString(KEY_JWT, "");
    }

    public String getUserId()
    {
        return _currentUserId.getValue() != null ? _currentUserId.getValue() : "";
    }

    public interface AuthCallback
    {
        void onSuccess();
        void onFailure(String error);
    }
}