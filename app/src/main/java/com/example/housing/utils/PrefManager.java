package com.example.housing.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager
{

    private static final String PREF_NAME = "housing_pref";
    private static final String KEY_ONBOARDING_DONE = "onboarding_done";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public PrefManager(Context context)
    {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setOnboardingDone(boolean done)
    {
        editor.putBoolean(KEY_ONBOARDING_DONE, done);
        editor.apply();
    }

    public boolean isOnboardingDone()
    {
        return pref.getBoolean(KEY_ONBOARDING_DONE, false);
    }
    public void saveLogin(String username, String password)
    {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public boolean isLoggedIn()
    {
        return getUsername() != null && getPassword() != null;
    }

    public String getUsername()
    {
        return pref.getString(KEY_USERNAME, null);
    }

    public String getPassword()
    {
        return pref.getString(KEY_PASSWORD, null);
    }

    public void clearLogin()
    {
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }

    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}
