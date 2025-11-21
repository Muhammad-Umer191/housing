package com.example.housing.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleHelper
{
    public static Context setLocale(Context context, String language)
    {
        if (language == null || language.isEmpty())
        {
            return context;
        }

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            config.setLocale(locale);
            return context.createConfigurationContext(config);
        }

        config.locale = locale;

        resources.updateConfiguration(config, resources.getDisplayMetrics());
        return context;
    }

    public static String getPersistedLanguage(Context context)
    {
        PrefManager prefManager = PrefManager.getInstance(context);
        return prefManager.getLanguage();
    }
}