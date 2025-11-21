package com.example.housing.network;

import com.google.gson.annotations.SerializedName;

public class RefreshRequest
{
    @SerializedName("refresh_token")
    private String refreshToken;

    public RefreshRequest(String refreshToken)
    {
        this.refreshToken = refreshToken;
    }
}