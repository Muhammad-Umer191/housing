package com.example.housing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Login extends AppCompatActivity {

    private EditText email, password;
    private CheckBox rememberMe;
    private MaterialButton log_in, btn_continue_google;
    private TextView forgotPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // your XML

        // Initialize views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rememberMe = findViewById(R.id.rememberMe);
        log_in = findViewById(R.id.log_in);
        btn_continue_google = findViewById(R.id.btn_continue_google);
        forgotPassword = findViewById(R.id.forgotPassword);

        log_in.setOnClickListener(v ->
        {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            boolean loginSuccess = verifyLogin(emailText, passwordText);

            if (loginSuccess) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                email.setError("Wrong email or password");
                password.setError("Wrong email or password");
                email.requestFocus();
            }
        } );

        btn_continue_google.setOnClickListener(v ->
        {
            // TODO: implement Google login later
        });

        forgotPassword.setOnClickListener(v ->
        {
            // TODO: implement forgot password logic
        });
    }

    /** Handles normal email/password login **/
    private void handleLogin() {

    }

    private boolean verifyLogin(String email, String password) {
        // TODO: Replace with real verification
        // Example test credentials:
        return email.equals("test@example.com") && password.equals("123456");
    }
}
