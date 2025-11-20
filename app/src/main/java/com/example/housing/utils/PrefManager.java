package com.example.housing.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class PrefManager
{
    private static final String TAG = "SecurePrefManager";

    private static final String PREF_NAME = "housing_secure_pref";

    private static final String KEY_ONBOARDING_DONE = "onboarding_done";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PROVIDER = "provider";

    private SharedPreferences pref;

    public PrefManager(Context context)
    {
        try
        {
            // 1. Create or retrieve the Master Key for encryption
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // 2. Initialize EncryptedSharedPreferences
            pref = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    // Scheme for encrypting keys (the name of the preference)
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    // Scheme for encrypting values (the data stored)
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        }
        catch (GeneralSecurityException | IOException e)
        {
            // Fatal error: Encryption setup failed. Log the error and fall back or crash.
            Log.e(TAG, "Failed to initialize EncryptedSharedPreferences.", e);
            // In a production app, you might fall back to standard prefs or force a crash.
            // For this example, we'll keep the preference null and log the error.
        }

        if (pref == null) {
            // Fallback for demonstration if encryption failed (not recommended for prod)
            Log.e(TAG, "Using insecure fallback SharedPreferences!");
            pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    // --- Utility Method to Get Editor ---
    private SharedPreferences.Editor getEditor()
    {
        if (pref != null)
        {
            return pref.edit();
        }
        // Should not happen if initialization was successful
        throw new IllegalStateException("SharedPreferences not initialized.");
    }

    // --- Onboarding ---
    public void setOnboardingDone(boolean done)
    {
        getEditor().putBoolean(KEY_ONBOARDING_DONE, done).apply();
    }

    public boolean isOnboardingDone()
    {
        return pref.getBoolean(KEY_ONBOARDING_DONE, false);
    }

    // --- Session Management ---
    public void saveLogin(String accessToken, String refreshToken, String userId, String email, String provider)
    {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PROVIDER, provider);
        editor.apply();
    }

    public String getAccessToken()
    {
        return pref.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken()
    {
        return pref.getString(KEY_REFRESH_TOKEN, null);
    }

    public String getUserId()
    {
        return pref.getString(KEY_USER_ID, null);
    }

    public String getEmail()
    {
        return pref.getString(KEY_EMAIL, null);
    }

    public String getProvider()
    {
        return pref.getString(KEY_PROVIDER, null);
    }

    public boolean isLoggedIn()
    {
        // Session is considered active if both tokens are present
        return getAccessToken() != null && getRefreshToken() != null;
    }

    public void clearLogin()
    {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_REFRESH_TOKEN);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PROVIDER);
        editor.apply();
    }

    public void clearAll()
    {
        getEditor().clear().apply();
    }
}