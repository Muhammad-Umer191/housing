package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.R;
import com.example.housing.fragments.DashboardFragment;
import com.example.housing.utils.PrefManager;

public class SplashScreen extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView icon = findViewById(R.id.housing_icon);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_screen_animation);
        icon.startAnimation(animation);

        PrefManager prefManager = new PrefManager(this);

        new Handler(getMainLooper()).postDelayed(() ->
        {
            Intent intent;
            if (prefManager.isOnboardingDone() && prefManager.isLoggedIn())
            {
                intent = new Intent(SplashScreen.this, DashboardFragment.class);
            }
            else
            {
                intent = new Intent(SplashScreen.this, OnBoarding.class);
            }
            startActivity(intent);
            finish();
        }, 3000);

    }
}
