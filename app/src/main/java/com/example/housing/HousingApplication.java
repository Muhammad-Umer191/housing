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

        // Initialize the PrefManager instance with the Application Context
        // This runs only once when the app process starts.
        prefManagerInstance = new PrefManager(this);

        // Initialize RetrofitClient
        RetrofitClient.initialize(this);
    }

    // Public method to get the single instance from anywhere in the app
    public static PrefManager getPrefManagerInstance()
    {
        return prefManagerInstance;
    }
}