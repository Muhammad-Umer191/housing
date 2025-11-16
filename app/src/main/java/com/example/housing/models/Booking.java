package com.example.housing.models;

public class Booking
{
    private String serviceTitle;
    private String schedule;
    private String provider;
    private String statusBadge;
    private int imageResId; // Resource ID for the image

    public Booking(String serviceTitle, String schedule, String provider, String statusBadge, int imageResId)
    {
        this.serviceTitle = serviceTitle;
        this.schedule = schedule;
        this.provider = provider;
        this.statusBadge = statusBadge;
        this.imageResId = imageResId;
    }

    public String getServiceTitle()
    {
        return serviceTitle;
    }

    public String getSchedule()
    {
        return schedule;
    }

    public String getProvider()
    {
        return provider;
    }

    public String getStatusBadge()
    {
        return statusBadge;
    }

    public int getImageResId()
    {
        return imageResId;
    }
}
