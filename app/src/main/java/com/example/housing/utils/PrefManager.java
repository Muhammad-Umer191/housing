package com.example.housing.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class PrefManager {

    private static final String TAG = "PrefManager";
    private static final String PREF_NAME = "housing_secure_pref";

    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PROVIDER = "provider";
    private static final String KEY_ON_BOARDED = "on_boarded";
    private static final String KEY_FULL_NAME = "full_name";

    private static PrefManager instance;
    private final SharedPreferences pref;

    private PrefManager(Context context) {
        SharedPreferences tmpPref;
        try {
            String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            tmpPref = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKey,
                    context.getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            Log.e(TAG, "EncryptedSharedPreferences failed, fallback to normal.", e);
            tmpPref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        pref = tmpPref;
    }

    public static synchronized PrefManager getInstance(Context context) {
        if (instance == null) instance = new PrefManager(context);
        return instance;
    }

    // Internal editor
    private SharedPreferences.Editor editor() {
        return pref.edit();
    }

    // ----------------- LOGIN & SESSION -----------------
    public boolean isLoggedIn() {
        return getAccessToken() != null && getRefreshToken() != null;
    }

    public void saveLogin(String accessToken, String refreshToken, String userId, String email, String provider) {
        editor()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_EMAIL, email)
                .putString(KEY_PROVIDER, provider)
                .putString(KEY_ON_BOARDED, "true")
                .apply();
    }

    public void clearLogin() {
        editor()
                .remove(KEY_ACCESS_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
                .remove(KEY_USER_ID)
                .remove(KEY_EMAIL)
                .remove(KEY_PROVIDER)
                .remove(KEY_ON_BOARDED)
                .remove(KEY_FULL_NAME)
                .apply();
    }

    // ----------------- LANGUAGE -----------------
    public void setLanguage(String languageCode) {
        editor().putString(KEY_LANGUAGE, languageCode).apply();
    }

    public String getLanguage() {
        return pref.getString(KEY_LANGUAGE, "en");
    }

    // ----------------- USER INFO -----------------
    public void setUserId(String userId) {
        editor().putString(KEY_USER_ID, userId).apply();
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, null);
    }

    public void setEmail(String email) {
        editor().putString(KEY_EMAIL, email).apply();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public void setFullName(String fullName) {
        editor().putString(KEY_FULL_NAME, fullName).apply();
    }

    public String getFullName() {
        return pref.getString(KEY_FULL_NAME, "");
    }

    public void setProvider(String provider) {
        editor().putString(KEY_PROVIDER, provider).apply();
    }

    public String getProvider() {
        return pref.getString(KEY_PROVIDER, null);
    }

    public void setOnBoarded(boolean onBoarded) {
        editor().putString(KEY_ON_BOARDED, onBoarded ? "true" : "false").apply();
    }

    public boolean isOnBoarded() {
        return "true".equals(pref.getString(KEY_ON_BOARDED, "false"));
    }

    // ----------------- TOKENS -----------------
    public void setAccessToken(String token) {
        editor().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    public String getAccessToken() {
        return pref.getString(KEY_ACCESS_TOKEN, null);
    }

    public void setRefreshToken(String token) {
        editor().putString(KEY_REFRESH_TOKEN, token).apply();
    }

    public String getRefreshToken() {
        return pref.getString(KEY_REFRESH_TOKEN, null);
    }
}
