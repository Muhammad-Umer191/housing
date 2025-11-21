package com.example.housing.data.supabase;

import com.example.housing.models.Notification;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Query;

/**
 * Retrofit interface defining the API endpoints for the Supabase 'notifications' table.
 * Uses PostgREST syntax for filtering and ordering.
 */
public interface SupabaseApi
{

    String SUPABASE_BASE_URL = SupabaseConfig.SUPABASE_URL;
    String SUPABASE_API_KEY = SupabaseConfig.SUPABASE_ANON_KEY;

    String SUPABASE_SCHEMA = "public";

    /**
     * Data structure for updating the 'is_read' status via PATCH.
     */
    class UpdatePayload {
        @SerializedName("is_read")
        private final boolean isRead;

        public UpdatePayload(boolean isRead) {
            this.isRead = isRead;
        }
    }

    /**
     * Fetches all notifications for a specific user, ordered by creation time descending.
     */
    @GET("notifications")
    Call<List<Notification>> getNotifications(
            @Header("Apikey") String apiKey,
            @Header("Authorization") String authHeader,
            @Header("Accept-Profile") String schema, // Supabase schema (e.g., "public")
            @Query("user_id") String userId,       // Filter by user ID
            @Query("order") String order           // Ordering
    );

    /**
     * Marks a specific notification as read using a PATCH request.
     */
    @PATCH("notifications")
    Call<Void> updateNotificationStatus(
            @Header("Apikey") String apiKey,
            @Header("Authorization") String authHeader,
            @Header("Content-Profile") String schema,
            @Query("id") String notificationId, // Filter by ID
            @Body UpdatePayload payload
    );
}