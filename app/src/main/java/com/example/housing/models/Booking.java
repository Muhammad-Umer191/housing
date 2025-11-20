package com.example.housing.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Booking
{
    private String serviceTitle;
    private String schedule;
    private String provider;
    private String statusBadge;
    private int imageResId;
    private String phoneNumber;


    public Booking(String serviceTitle, String schedule, String provider, String statusBadge, int imageResId,  String phoneNumber)
    {
        this.serviceTitle = serviceTitle;
        this.schedule = schedule;
        this.provider = provider;
        this.statusBadge = statusBadge;
        this.imageResId = imageResId;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    public String getServiceTitle() { return serviceTitle; }
    public String getSchedule() { return schedule; }
    public String getProvider() { return provider; }
    public String getStatusBadge() { return statusBadge; }
    public int getImageResId() { return imageResId; }

    // Convert schedule string to timestamp for sorting
    public long getTimestamp()
    {
        try
        {
            // Example schedule: "10:00-11:00 AM, 10 Dec"
            String[] parts = schedule.split(", ");
            if (parts.length < 2) return 0;

            String timePart = parts[0];
            String datePart = parts[1];
            String startTime = timePart.split("-")[0].trim();

            String amPm = timePart.endsWith("AM") || timePart.endsWith("PM") ?
                    timePart.substring(timePart.length() - 2) : "AM";

            String fullDate = datePart + " " + startTime + " " + amPm + " 2025";

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM h:mm a yyyy", Locale.getDefault());
            Date date = sdf.parse(fullDate);

            return date != null ? date.getTime() : 0;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

}
