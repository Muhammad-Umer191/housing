package com.example.housing;

import android.app.Application;
import com.example.housing.network.RetrofitClient;
import com.example.housing.utils.PrefManager;

public class HousingApplication extends Application
{
    private static PrefManager prefManagerInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();

        prefManagerInstance = PrefManager.getInstance(this);

        RetrofitClient.initialize(this);
    }

    public static PrefManager getPrefManagerInstance()
    {
        return prefManagerInstance;
    }
}