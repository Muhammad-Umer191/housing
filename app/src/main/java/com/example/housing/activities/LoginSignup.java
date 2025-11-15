package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.housing.data.FirebaseAuthManager;
import com.example.housing.R;
import com.example.housing.utils.PrefManager;

public class LoginSignup extends AppCompatActivity
{

    private Button btnLogin, btnSignUp, btnContinueGoogle;

    private PrefManager prefManager;
//    private FirebaseAuthManager firebaseAuthManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup); // your XML file

        // Initialize views
        btnLogin = findViewById(R.id.log_in);
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnContinueGoogle = findViewById(R.id.btn_continue_google);

        // Initialize backend helpers
        prefManager = new PrefManager(this);
//        firebaseAuthManager = new FirebaseAuthManager(this, prefManager);

        // Click listeners
        btnLogin.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });
        btnSignUp.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, Signup.class);
            startActivity(intent);
        });
        btnContinueGoogle.setOnClickListener(view ->
        {
            // TODO: Implement Google Sign-In logic later
        });
    }
}
