package com.example.housing.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

public class Signup extends AppCompatActivity
{
    private EditText email, password, confirmPassword;
    private Button signUpBtn, googleSignUpBtn;
    private TextView loginRedirect;

    private PrefManager prefManager;

    private static final String SUPABASE_URL = "https://pxuboqabrgabizqrxdmb.supabase.co";
    private static final String REDIRECT_URI = "com.example.housing://auth/callback";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUpBtn = findViewById(R.id.sign_up);
        googleSignUpBtn = findViewById(R.id.btn_continue_google);
        loginRedirect = findViewById(R.id.loginMain);

        prefManager = new PrefManager(this);

        signUpBtn.setOnClickListener(v -> handleEmailSignup());
        googleSignUpBtn.setOnClickListener(v -> openGoogleLogin());
        loginRedirect.setOnClickListener(v ->
                startActivity(new Intent(Signup.this, Login.class)));
    }

    private void handleEmailSignup()
    {
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();
        String confirmPasswordInput = confirmPassword.getText().toString().trim();

        if(emailInput.isEmpty() || passwordInput.isEmpty() || confirmPasswordInput.isEmpty())
        {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!passwordInput.equals(confirmPasswordInput))
        {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (passwordInput.length() < 6)
        {
            Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest signupRequest = new LoginRequest(emailInput, passwordInput);

        RetrofitClient.getAuthService().signUp(signupRequest) // <-- Using signUp endpoint
                .enqueue(new Callback<LoginResponse>()
                {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
                    {
                        if(response.isSuccessful() && response.body() != null)
                        {
                            LoginResponse user = response.body();

                            prefManager.saveLogin(
                                    user.getAccessToken(),
                                    user.getRefreshToken(),
                                    user.getUserId(),
                                    user.getEmail(),
                                    "email"
                            );

                            Toast.makeText(Signup.this, "See email for verification" ,
                                    Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(Signup.this, HomeActivity.class));
                            finish();
                        }
                        else
                        {
                            String errorMsg = "Signup failed: " + response.message();
                            try {
                                if (response.errorBody() != null) {
                                    Log.e("Signup", "Error Body: " + response.errorBody().string());
                                }
                            } catch (Exception e) {
                                Log.e("Signup", "Error reading error body", e);
                            }

                            Toast.makeText(Signup.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t)
                    {
                        Toast.makeText(Signup.this, "Signup failed: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void openGoogleLogin()
    {
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
                    Toast.makeText(this, "Google sign-up successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Signup.this, HomeActivity.class));
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