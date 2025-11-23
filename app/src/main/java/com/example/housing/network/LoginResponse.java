package com.example.housing.network;

import com.google.gson.annotations.SerializedName;

public class LoginResponse
{
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("user")
    private User user;

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getUserId() { return user != null ? user.id : null; }
    public String getEmail() { return user != null ? user.email : null; }

    public static class User
    {
        @SerializedName("id")
        public String id;

        @SerializedName("email")
        public String email;
    }
}