package com.example.housing;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

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

        icon.postDelayed(() ->
        {
            Intent intent;

            if (prefManager.isOnboardingDone() && prefManager.isLoggedIn())
            {
                intent = new Intent(this, MainActivity.class);
            }
            else
            {
                intent = new Intent(this, OnBoarding.class);
            }
            startActivity(intent);
            finish();
        }, 3000);
    }
}
