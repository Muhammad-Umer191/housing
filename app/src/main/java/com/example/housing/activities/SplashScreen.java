package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.R;
import com.example.housing.utils.PrefManager;

public class SplashScreen extends AppCompatActivity
{
    private static final long SPLASH_DELAY_MS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            ImageView icon = findViewById(R.id.housing_icon);
            Animation splashAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_animation);
            icon.startAnimation(splashAnim);
        }
        catch (Exception e) {

        }

        final PrefManager prefManagerInstance = PrefManager.getInstance(this);

        new Handler(getMainLooper()).postDelayed(() ->
        {
            Intent intent;

            if (prefManagerInstance.isLoggedIn())
            {
                intent = new Intent(SplashScreen.this, HomeActivity.class);
            }
            else if (!prefManagerInstance.isOnboardingDone())
            {
                intent = new Intent(SplashScreen.this, OnBoarding.class);
            }
            else
            {
                intent = new Intent(SplashScreen.this, LoginSignup.class);
            }

            // Start the next activity and close the splash screen
            startActivity(intent);
            finish();

        }, SPLASH_DELAY_MS);
    }
}