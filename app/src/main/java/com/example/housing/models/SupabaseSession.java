package com.example.housing.models;

import com.google.gson.annotations.SerializedName;

public class SupabaseSession
{
    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("refresh_token")
    public String refreshToken;

    @SerializedName("user")
    public User user;
}
