package com.example.housing.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtils
{

    public static String getRelativeTime(long timestampMillis)
    {
        long now = System.currentTimeMillis();
        long diff = now - timestampMillis;

        if (diff < TimeUnit.MINUTES.toMillis(1))
        {
            return "just now";
        }
        else if (diff < TimeUnit.HOURS.toMillis(1))
        {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            return minutes + " min ago";
        }
        else if (diff < TimeUnit.DAYS.toMillis(1))
        {
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            return hours + " hr ago";
        }
        else if (diff < TimeUnit.DAYS.toMillis(7))
        {
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        }
        else
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
            return sdf.format(new Date(timestampMillis));
        }
    }
}