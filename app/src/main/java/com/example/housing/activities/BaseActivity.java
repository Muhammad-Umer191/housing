package com.example.housing.activities;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.utils.LocaleHelper;

public class BaseActivity extends AppCompatActivity
{

    @Override
    protected void attachBaseContext(Context newBase)
    {
        String language = LocaleHelper.getPersistedLanguage(newBase);
        Context context = LocaleHelper.setLocale(newBase, language);
        super.attachBaseContext(context);
    }
}