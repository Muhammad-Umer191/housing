package com.example.housing;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Signup extends AppCompatActivity
{

    private EditText email, password, confirmPassword;
    private MaterialButton sign_up, btn_continue_google;
    private TextView loginMain;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        sign_up = findViewById(R.id.sign_up);
        btn_continue_google = findViewById(R.id.btn_continue_google);
        loginMain = findViewById(R.id.loginMain);

        sign_up.setOnClickListener(v ->
        {
            String emailInput = email.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();
            String confirmPasswordInput = confirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(emailInput) || TextUtils.isEmpty(passwordInput) || TextUtils.isEmpty(confirmPasswordInput))
            {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passwordInput.equals(confirmPasswordInput))
            {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                password.setText("");
                confirmPassword.setText("");
                return;
            }

            if (verifySignup(emailInput, passwordInput))
            {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(this, "Signup failed: Invalid credentials", Toast.LENGTH_SHORT).show();
                email.setText("");
                password.setText("");
                confirmPassword.setText("");
            }
        });

        btn_continue_google.setOnClickListener(v ->
        {
            // TODO: Implement Google Sign-Up
        });

        loginMain.setOnClickListener(v ->
        {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });
    }

    private boolean verifySignup(String email, String password)
    {
        // For now, let's just return true for demonstration
        return true;
    }
}
