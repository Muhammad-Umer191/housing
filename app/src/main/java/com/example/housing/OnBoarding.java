package com.example.housing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OnBoarding extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        Button nextButton = findViewById(R.id.next);

        nextButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(OnBoarding.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
