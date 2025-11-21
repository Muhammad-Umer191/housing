package com.example.housing.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.R;
import com.example.housing.utils.PrefManager;

public class PersonalInfo extends AppCompatActivity
{

    private TextView userNameText;
    private TextView userEmailText;

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        prefManager = PrefManager.getInstance(this);

        initViews();
        loadData();
    }

    private void initViews()
    {
        userNameText = findViewById(R.id.info_user_name);
        userEmailText = findViewById(R.id.info_user_email);

    }

    private void loadData()
    {
        String email = prefManager.getEmail();
        String name = prefManager.getUserId();

        userEmailText.setText(email != null ? email : getString(R.string.not_available));
        userNameText.setText(name != null ? name : getString(R.string.not_available));
    }
}