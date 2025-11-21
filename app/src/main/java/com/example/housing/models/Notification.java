package com.example.housing.models;

import com.example.housing.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Notification
{
    // IMPORTANT: UUID primary key requires a String
    private String id;
    private String userId; // Added for context, though not used in your snippet
    private String title;
    private String body;
    private boolean isRead;
    private long createdAtMillis;

    public Notification(String id, String userId, String title, String body, boolean isRead, long createdAtMillis) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.isRead = isRead;
        this.createdAtMillis = createdAtMillis;
    }

    public static Notification fromJson(JSONObject json) throws JSONException {
        // 1. UUID primary key is stored as a String
        String id = json.getString("id");
        String userId = json.getString("user_id");
        String title = json.getString("title");
        String body = json.getString("body");
        boolean isRead = json.getBoolean("is_read");
        String createdAtStr = json.getString("created_at");

        long createdAtMillis = 0;
        try {
            // Handle the Supabase timestamp string (e.g., "2023-10-26T15:00:00.123456+00:00")
            // Clean up the string format for Java SimpleDateFormat parsing
            String cleanTime = createdAtStr.replace('Z', '+').replaceAll("\\+([0-9]{2}):([0-9]{2})", "+$1$2");

            // Use the specific parser for Supabase's format
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", Locale.US);
            parser.setTimeZone(TimeZone.getTimeZone("UTC")); // Supabase returns UTC

            Date date = parser.parse(cleanTime);
            if (date != null) {
                createdAtMillis = date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Default to current time if parsing fails
            createdAtMillis = System.currentTimeMillis();
        }

        return new Notification(id, userId, title, body, isRead, createdAtMillis);
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public long getCreatedAtMillis() {
        return createdAtMillis;
    }

    /** Returns the formatted relative time string (e.g., "5 min ago"). */
    public String getFormattedTime() {
        // Uses the TimeUtils helper class from Step 2a
        return TimeUtils.getRelativeTime(createdAtMillis);
    }

    public boolean isRead() {
        return isRead;
    }

    // --- Setters/Mutators ---

    public void setRead(boolean read) {
        isRead = read;
    }
}