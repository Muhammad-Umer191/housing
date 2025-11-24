package com.example.housing.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.utils.PrefManager;
import com.example.housing.R;
import com.example.housing.network.AuthApi;
import com.example.housing.network.RetrofitClient;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {

    private EditText email, password, confirmPassword;

    private Button signupButton;
    private PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signupButton = findViewById(R.id.signupButton);
        pref = PrefManager.getInstance(Signup.this);

        // --- Signup Button ---
        signupButton.setOnClickListener(v -> handleEmailSignup());
    }

    private void handleEmailSignup() {
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();
        String confirmPasswordInput = confirmPassword.getText().toString().trim();

        if (!passwordInput.equals(confirmPasswordInput)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (passwordInput.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObject signupBody = new JsonObject();
        signupBody.addProperty("email", emailInput);
        signupBody.addProperty("password", passwordInput);

        AuthApi api = RetrofitClient.getClient().create(AuthApi.class);
        api.signUp(signupBody).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Toast.makeText(Signup.this, "Check the mail to verify", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(Signup.this, "Sign up failed" , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(Signup.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
