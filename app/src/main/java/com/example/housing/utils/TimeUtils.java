package com.example.housing.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    /**
     * Formats a timestamp string into a user-friendly date/time.
     * Example: "2025-11-21T14:30:00Z" → "Nov 21, 2025, 02:30 PM"
     *
     * @param timestamp ISO 8601 format timestamp
     * @return formatted date/time string
     */
    public static String getFormattedTime(String timestamp)
    {
        if (timestamp == null || timestamp.isEmpty()) return "";

        // Parse the input timestamp
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        inputFormat.setLenient(false);

        // Desired output format
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm a", Locale.getDefault());

        try {
            Date date = inputFormat.parse(timestamp);
            return date != null ? outputFormat.format(date) : "";
        } catch (ParseException e) {
            e.printStackTrace();
            return timestamp; // fallback to original if parsing fails
        }
    }
}
