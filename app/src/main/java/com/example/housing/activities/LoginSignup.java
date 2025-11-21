package com.example.housing.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.R;
import com.example.housing.utils.PrefManager;

public class LoginSignup extends AppCompatActivity
{
    private Button btnLogin, btnSignUp, btnContinueGoogle;
    private PrefManager prefManager; // ⬅️ The instance variable is still needed
    private static final String SUPABASE_URL = "https://pxuboqabrgabizqrxdmb.supabase.co";
    private static final String REDIRECT_URI = "com.example.housing://auth/callback";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        btnLogin = findViewById(R.id.log_in);
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnContinueGoogle = findViewById(R.id.btn_continue_google);

        // ⬅️ FIX: Use the stable Singleton instance
        prefManager = PrefManager.getInstance(this);

        // -------------------------
        // Open Login activity
        // -------------------------
        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(LoginSignup.this, Login.class)));

        // -------------------------
        // Open Signup activity
        // -------------------------
        btnSignUp.setOnClickListener(v ->
                startActivity(new Intent(LoginSignup.this, Signup.class)));

        // -------------------------
        // Open Google OAuth in browser
        // -------------------------
        btnContinueGoogle.setOnClickListener(v -> openGoogleLogin());
    }

    // -------------------------
    // Open Google OAuth in browser
    // -------------------------
    private void openGoogleLogin()
    {
        // 1. Construct the authorization URL
        String url = SUPABASE_URL + "/auth/v1/authorize" +
                "?provider=google" +
                "&redirect_to=" + REDIRECT_URI;

        // 2. Launch the browser
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        Toast.makeText(this, "Opening Google login...", Toast.LENGTH_SHORT).show();
    }

    // -------------------------
    // Handle redirect from Google OAuth (or Email Confirmation Deep Link)
    // -------------------------
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Uri uri = intent.getData();

        // Check if the URI is a valid callback with the expected scheme/host
        if(uri != null && uri.toString().startsWith(REDIRECT_URI))
        {
            // The tokens are returned in the fragment portion of the URL
            String fragment = uri.getFragment();

            if(fragment != null)
            {
                // ⬅️ FIX: Add error handling for invalid/expired links here too
                if (fragment.contains("error=")) {
                    Toast.makeText(this, "Authentication failed: Link invalid or expired.", Toast.LENGTH_LONG).show();
                    // Do not save session, redirect to login
                    startActivity(new Intent(this, Login.class));
                    finish();
                    return;
                }

                String[] params = fragment.split("&");
                String accessToken = null, refreshToken = null, provider = "google";

                // Parse the fragment parameters
                for(String param : params)
                {
                    if(param.startsWith("access_token="))
                        accessToken = param.split("=")[1];
                    else if(param.startsWith("refresh_token="))
                        refreshToken = param.split("=")[1];
                }

                if(accessToken != null && refreshToken != null)
                {
                    // Supabase returns null for user ID and email on this deep link flow,
                    // as it's a client-side implicit grant. We save the tokens.
                    prefManager.saveLogin(accessToken, refreshToken, null, null, provider);

                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                    // Proceed to the main application
                    startActivity(new Intent(LoginSignup.this, HomeActivity.class));
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