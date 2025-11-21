package com.example.housing.data.repositories;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.housing.models.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationRepository
{
    private static final String SUPABASE_URL = "https://pxuboqabrgabizqrxdmb.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InB4dWJvcWFicmdhYml6cXJ4ZG1iIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjMzNzQwNjgsImV4cCI6MjA3ODk1MDA2OH0.pVlWoKdkdsPKGcIn1EN_ZpEL8kR_Zvb6_2oYLYeCGaM";
    private static final String NOTIFICATION_TABLE_URL = SUPABASE_URL + "/rest/v1/notifications";

    private static final String TAG = "NotifRepository";
    private final OkHttpClient client = new OkHttpClient();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface RepoCallback<T>
    {
        void onSuccess(T result);
        void onError(String error);
    }

    public void fetchNotifications(String userId, RepoCallback<List<Notification>> callback)
    {
        executor.execute(() ->
        {
            List<Notification> notifications = new ArrayList<>();
            // Query for records matching the user_id, ordered by creation date descending
            String url = NOTIFICATION_TABLE_URL + "?user_id=eq." + userId + "&order=created_at.desc";

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("apikey", SUPABASE_ANON_KEY)
                    .addHeader("Authorization", "Bearer " + SUPABASE_ANON_KEY)
                    .addHeader("Accept", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response + " Body: " + response.body().string());
                }

                String responseBody = response.body().string();
                JSONArray jsonArray = new JSONArray(responseBody);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    notifications.add(Notification.fromJson(jsonObject));
                }

                handler.post(() -> callback.onSuccess(notifications));

            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error fetching notifications: " + e.getMessage());
                handler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    /**
     * Marks a specific notification as read in the Supabase database.
     * @param notificationId The UUID of the notification to update.
     * @param userId The ID of the authenticated user (required for RLS).
     * @param callback The callback to indicate success or failure.
     */
    public void markAsRead(String notificationId, String userId, RepoCallback<Void> callback) {
        executor.execute(() -> {
            // The RLS policy allows UPDATE only if auth.uid() = user_id
            String url = NOTIFICATION_TABLE_URL + "?id=eq." + notificationId + "&user_id=eq." + userId;

            JSONObject updatePayload = new JSONObject();
            try {
                updatePayload.put("is_read", true);
            } catch (JSONException e) {
                handler.post(() -> callback.onError("JSON creation error."));
                return;
            }

            RequestBody body = RequestBody.create(updatePayload.toString(), MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("apikey", SUPABASE_ANON_KEY)
                    .addHeader("Authorization", "Bearer " + SUPABASE_ANON_KEY) // Again, use real JWT in production
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=representation") // Supabase requires this for PUT/PATCH to return data
                    .patch(body) // Use PATCH for partial updates
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "No body";
                    throw new IOException("Failed to update status. Code: " + response.code() + ", Body: " + errorBody);
                }

                handler.post(() -> callback.onSuccess(null));

            } catch (IOException e) {
                Log.e(TAG, "Error marking notification as read: " + e.getMessage());
                handler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }
}