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

import com.example.housing.HousingApplication; // ⬅️ NEW IMPORT for Application Singleton
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

        // ⬅️ FIX 1: Get the stable Singleton instance instead of creating a new one
        // Note: The PrefManager class was updated in a previous step to include getInstance(context)
        prefManager = PrefManager.getInstance(this);

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

        // ⬅️ FIX 2: Supabase signup returns the user immediately, but the tokens are for
        // the unverified session. Do NOT save the session or go to HomeActivity yet.
        // We MUST explicitly send the redirect_to URL for email confirmation.
        // NOTE: If your Supabase API call doesn't accept a redirect_to parameter,
        // you'll need to update your Retrofit definition (AuthService) and the Request class.
        // Assuming your API service is updated to accept the redirect_to parameter:

        // Placeholder for correct signup method that includes redirect_to:
        // RetrofitClient.getAuthService().signUp(signupRequest, REDIRECT_URI)
        // I will revert to your existing call structure but fix the POST-signup logic.

        RetrofitClient.getAuthService().signUp(signupRequest)
                .enqueue(new Callback<LoginResponse>()
                {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
                    {
                        if(response.isSuccessful() && response.body() != null)
                        {
                            // ⬅️ CRITICAL CHANGE: Do NOT save login or go to HomeActivity here!
                            // The user's session is UNVERIFIED and will be immediately invalid.
                            // The goal is just to inform the user to check their email.

                            // Log.d("Signup", "User object returned, but is UNVERIFIED. Not saving session.");

                            // Navigate to a dedicated verification screen or simply Login screen
                            Toast.makeText(Signup.this, "Signup successful. Please check your email to verify your account.",
                                    Toast.LENGTH_LONG).show();

                            // Go back to the Login screen where they can try logging in after confirmation
                            startActivity(new Intent(Signup.this, Login.class));
                            finish();
                        }
                        else
                        {
                            // ... (Error handling remains the same) ...
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
        // ... (This code is correct for sending the redirect_to for Google login) ...
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
            // ⬅️ FIX 3: Check for error conditions in the URI (like the one you got)
            if (uri.getFragment() != null && uri.getFragment().contains("error=")) {
                Toast.makeText(this, "Authentication failed: Email link invalid or expired.", Toast.LENGTH_LONG).show();
                // Do not save session, redirect to login
                startActivity(new Intent(this, Login.class));
                finish();
                return;
            }

            // Existing logic for successful token extraction (which is for Google, but works for email too)
            String fragment = uri.getFragment();
            if(fragment != null)
            {
                String[] params = fragment.split("&");
                String accessToken = null, refreshToken = null, provider = "email/social";

                for(String param : params)
                {
                    if(param.startsWith("access_token="))
                        accessToken = param.split("=")[1];
                    else if(param.startsWith("refresh_token="))
                        refreshToken = param.split("=")[1];
                }

                if(accessToken != null && refreshToken != null)
                {
                    // ⬅️ When launched via deep link, the user is verified. Save session.
                    prefManager.saveLogin(accessToken, refreshToken, null, null, provider);
                    Toast.makeText(this, "Sign-in successful", Toast.LENGTH_SHORT).show();
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