package com.example.housing.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.BuildConfig;
import com.example.housing.R;
import com.example.housing.network.AuthApi;
import com.example.housing.network.RetrofitClient;
import com.example.housing.network.UserApi;
import com.example.housing.utils.PrefManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity
{
    private EditText email, password;
    private Button logInBtn, googleSignInBtn;
    private CheckBox rememberMe;
    private TextView forgotPassword;

    private PrefManager prefManager;
    private static final String REDIRECT_URI = BuildConfig.REDIRECT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        logInBtn = findViewById(R.id.log_in);
        rememberMe = findViewById(R.id.rememberMe);
        forgotPassword = findViewById(R.id.forgotPassword);

        prefManager = PrefManager.getInstance(this);

        // Autofill email if remembered
        String savedEmail = prefManager.getEmail();
        if(savedEmail != null)
        {
            email.setText(savedEmail);
            rememberMe.setChecked(true);
        }

        logInBtn.setOnClickListener(v -> handleEmailLogin());
        forgotPassword.setOnClickListener(v ->
                startActivity(new Intent(Login.this, ForgotPassword.class)));
    }

    private void handleEmailLogin() {
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();

        if(emailInput.isEmpty() || passwordInput.isEmpty()) {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObject loginBody = new JsonObject();
        loginBody.addProperty("email", emailInput);
        loginBody.addProperty("password", passwordInput);

        AuthApi authApi = RetrofitClient.getClient().create(AuthApi.class);
        authApi.signIn(loginBody).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful() && response.body() != null) {
                    JsonObject body = response.body();
                    String accessToken = body.get("access_token").getAsString();
                    String refreshToken = body.get("refresh_token").getAsString();
                    String uid = body.getAsJsonObject("user").get("id").getAsString();
                    prefManager.saveLogin(accessToken, refreshToken, uid, emailInput, "email");
                    // --- Build JSON for users table ---
                    JsonObject userObj = new JsonObject();
                    userObj.addProperty("p_email", emailInput);

                    UserApi userApi = RetrofitClient.getClient().create(UserApi.class);
                    userApi.insertUser("Bearer " + accessToken, BuildConfig.SUPABASE_ANON_KEY,userObj)
                            .enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                        prefManager.saveLogin(accessToken, refreshToken, uid, emailInput, "email");
                                        Intent intent = new Intent(Login.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(Login.this, "DB insert failed. Contact Support", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                }
                            });

                    Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(Login.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Uri uri = intent.getData();

        if(uri != null && uri.toString().startsWith(REDIRECT_URI))
        {
            String fragment = uri.getFragment();
            if(fragment != null)
            {
                if (fragment.contains("error=")) {
                    Toast.makeText(this, "Authentication failed: Link invalid or expired.", Toast.LENGTH_LONG).show();
                    // Do not save session, redirect to login
                    startActivity(new Intent(this, Login.class));
                    finish();
                    return;
                }

                String[] params = fragment.split("&");
                String accessToken = null, refreshToken = null, provider = "google";

                for(String param : params)
                {
                    if(param.startsWith("access_token="))
                        accessToken = param.split("=")[1];
                    else if(param.startsWith("refresh_token="))
                        refreshToken = param.split("=")[1];
                }

                if(accessToken != null && refreshToken != null)
                {
                    prefManager.saveLogin(accessToken, refreshToken, null, null, provider);
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, HomeActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Failed to get tokens from redirect", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}