package com.example.housing;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

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

        icon.postDelayed(() -> {
             Intent intent = new Intent(this, OnBoarding.class);
             startActivity(intent);
             finish();
        }, 3000);

    }
}
