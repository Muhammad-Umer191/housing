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
    private static final String KEY_APP_LANGUAGE = "app_language";

    private static final String KEY_ONBOARDING_DONE = "onboarding_done";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PROVIDER = "provider";

    private static PrefManager instance;

    private SharedPreferences pref;

    private PrefManager(Context context)
    {
        try
        {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            pref = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        }
        catch (GeneralSecurityException | IOException e)
        {
            Log.e(TAG, "Failed to initialize EncryptedSharedPreferences. Using INSECURE fallback.", e);
            pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }

        if (pref == null)
        {
            Log.e(TAG, "PrefManager initialization failed completely.");
        }
    }

    public static synchronized PrefManager getInstance(Context context)
    {
        if (instance == null)
        {
            // Initialize with the Application Context for maximum stability
            instance = new PrefManager(context.getApplicationContext());
        }
        return instance;
    }


    private SharedPreferences.Editor getEditor()
    {
        if (pref != null)
        {
            return pref.edit();
        }
        // ⬅️ CHANGE 2: Instead of crashing, return null if uninitialized (safer)
        Log.e(TAG, "SharedPreferences not initialized. Cannot get editor.");
        return null;
    }

    public void setOnboardingDone(boolean done)
    {
        SharedPreferences.Editor editor = getEditor();
        if (editor != null) editor.putBoolean(KEY_ONBOARDING_DONE, done).apply();
    }

    public boolean isOnboardingDone()
    {
        return pref != null ? pref.getBoolean(KEY_ONBOARDING_DONE, false) : false;
    }

    public void saveLogin(String accessToken, String refreshToken, String userId, String email, String provider)
    {
        SharedPreferences.Editor editor = getEditor();
        if (editor == null) return;

        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PROVIDER, provider);
        editor.apply();
    }

    public String getAccessToken()
    {
        return pref != null ? pref.getString(KEY_ACCESS_TOKEN, null) : null;
    }

    public String getRefreshToken()
    {
        return pref != null ? pref.getString(KEY_REFRESH_TOKEN, null) : null;
    }

    public String getUserId()
    {
        return pref != null ? pref.getString(KEY_USER_ID, null) : null;
    }

    public String getEmail()
    {
        return pref != null ? pref.getString(KEY_EMAIL, null) : null;
    }

    public String getProvider()
    {
        return pref != null ? pref.getString(KEY_PROVIDER, null) : null;
    }

    public boolean isLoggedIn()
    {
        // Session is considered active if both tokens are present
        return getAccessToken() != null && getRefreshToken() != null;
    }

    public void clearLogin()
    {
        SharedPreferences.Editor editor = getEditor();
        if (editor == null) return;

        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_REFRESH_TOKEN);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_PROVIDER);
        editor.apply();
    }


    public void setLanguage(String localeCode)
    {
        SharedPreferences.Editor editor = getEditor();
        if (editor != null) editor.putString(KEY_APP_LANGUAGE, localeCode).apply();
    }

    // Default language is "en" (English)
    public String getLanguage()
    {
        return pref != null ? pref.getString(KEY_APP_LANGUAGE, "en") : "en";
    }
    public void clearAll()
    {
        SharedPreferences.Editor editor = getEditor();
        if (editor != null) editor.clear().apply();
    }
}