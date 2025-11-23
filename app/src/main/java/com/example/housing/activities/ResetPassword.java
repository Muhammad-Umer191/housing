package com.example.housing.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.BuildConfig;
import com.example.housing.R;
import com.example.housing.network.AuthApi;
import com.example.housing.network.RetrofitClient;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends AppCompatActivity {

    private EditText passwordInput, confirmPasswordInput;
    private Button resetButton;

    String accessToken;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirmPassword);
        resetButton = findViewById(R.id.sign_up);

        Uri data = getIntent().getData();
        if (data != null) {
            accessToken = data.getQueryParameter("access_token");
        }


        email = getIntent().getStringExtra("email");

        resetButton.setOnClickListener(v -> handleResetPassword());
    }

    private void handleResetPassword() {
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObject body = new JsonObject();
        body.addProperty("password", password);

        AuthApi api = RetrofitClient.getClient().create(AuthApi.class);
        api.updatePassword("Bearer " + accessToken, body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ResetPassword.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ResetPassword.this, Login.class));
                    finish();
                } else {
                    Toast.makeText(ResetPassword.this, "Failed to reset password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ResetPassword.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
