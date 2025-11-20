package com.example.housing.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.R;
import com.example.housing.network.LoginRequest;
import com.example.housing.network.LoginResponse;
import com.example.housing.network.RetrofitClient;
import com.example.housing.utils.PrefManager;

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

    // Constants for Google Deep Link flow (even if not used for Retrofit, kept for openGoogleLogin())
    private static final String SUPABASE_URL = "https://pxuboqabrgabizqrxdmb.supabase.co";
    private static final String REDIRECT_URI = "com.example.housing://auth/callback";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        logInBtn = findViewById(R.id.log_in);
        googleSignInBtn = findViewById(R.id.btn_continue_google);
        rememberMe = findViewById(R.id.rememberMe);
        forgotPassword = findViewById(R.id.forgotPassword);

        prefManager = new PrefManager(this);

        // Autofill email if remembered
        String savedEmail = prefManager.getEmail();
        if(savedEmail != null)
        {
            email.setText(savedEmail);
            rememberMe.setChecked(true);
        }

        logInBtn.setOnClickListener(v -> handleEmailLogin());
        googleSignInBtn.setOnClickListener(v -> openGoogleLogin());
        forgotPassword.setOnClickListener(v ->
                startActivity(new Intent(Login.this, ForgotPassword.class)));
    }

    private void handleEmailLogin()
    {
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();

        if(emailInput.isEmpty() || passwordInput.isEmpty())
        {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(emailInput, passwordInput);

        RetrofitClient.getAuthService().login(loginRequest) // <-- Using login endpoint
                .enqueue(new Callback<LoginResponse>()
                {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
                    {
                        if(response.isSuccessful() && response.body() != null)
                        {
                            LoginResponse login = response.body();

                            // Save session tokens and user details
                            prefManager.saveLogin(
                                    login.getAccessToken(),
                                    login.getRefreshToken(),
                                    login.getUserId(),
                                    login.getEmail(),
                                    "email"
                            );

                            Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, HomeActivity.class));
                            finish();
                        }
                        else
                        {
                            String errorMsg = "Login failed: " + response.message();
                            try {
                                if (response.errorBody() != null) {
                                    Log.e("Login", "Error Body: " + response.errorBody().string());
                                }
                            } catch (Exception e) {
                                Log.e("Login", "Error reading error body", e);
                            }
                            Toast.makeText(Login.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t)
                    {
                        Toast.makeText(Login.this, "Login failed: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.e("Login", "Network failure", t);
                    }
                });
    }

    private void openGoogleLogin()
    {
        // Launches the browser redirect flow for Google Auth
        String url = SUPABASE_URL + "/auth/v1/authorize" +
                "?provider=google" +
                "&redirect_to=" + REDIRECT_URI;

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        Toast.makeText(this, "Opening Google login...", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Google login successful", Toast.LENGTH_SHORT).show();
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