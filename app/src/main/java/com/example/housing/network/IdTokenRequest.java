package com.example.housing.network;

public class IdTokenRequest
{
    // The provider must be explicitly set to "google" for this flow
    private final String provider = "google";

    // This field holds the ID Token received from the Google SDK
    private String id_token;

    public IdTokenRequest(String idToken)
    {
        this.id_token = idToken;
    }

    // Optional: getters
    public String getProvider()
    {
        return provider;
    }

    public String getIdToken()
    {
        return id_token;
    }
}