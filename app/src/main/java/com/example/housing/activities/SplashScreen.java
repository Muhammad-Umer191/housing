package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.R; // Make sure R is imported for resources
import com.example.housing.utils.PrefManager;

public class SplashScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // 1. Set the layout file for the splash screen UI
        setContentView(R.layout.activity_splash_screen);

        // Optional: Run the animation
        try {
            ImageView icon = findViewById(R.id.housing_icon);
            Animation splashAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_animation);
            icon.startAnimation(splashAnim);
        } catch (Exception e) {
            // Log or handle case where R.id.housing_icon or R.anim.splash_animation are missing
        }


        // 2. Initialize the secure preference manager
        PrefManager pref = new PrefManager(this);

        // 3. Post a delayed task to handle navigation after 3 seconds
        new Handler(getMainLooper()).postDelayed(() ->
        {
            Intent intent;

            // --- Navigation Logic ---
            if (!pref.isOnboardingDone())
            {
                // Show onboarding first
                intent = new Intent(SplashScreen.this, OnBoarding.class);
            }
            else if (!pref.isLoggedIn())
            {
                // Not logged in → go to login/signup
                intent = new Intent(SplashScreen.this, LoginSignup.class);
            }
            else
            {
                // Already logged in → go to Home
                intent = new Intent(SplashScreen.this, HomeActivity.class);
            }

            // 4. Start the next activity and close the splash screen
            startActivity(intent);
            finish();
        }, 3000); // 3.0s delay
    }
}