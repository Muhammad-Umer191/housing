package com.example.housing.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthService
{
    String ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InB4dWJvcWFicmdhYml6cXJ4ZG1iIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjMzNzQwNjgsImV4cCI6MjA3ODk1MDA2OH0.pVlWoKdkdsPKGcIn1EN_ZpEL8kR_Zvb6_2oYLYeCGaM";

    public static final String HEADER_CONTENT_TYPE = "Content-Type: application/json";

    public static final String HEADER_APIKEY_ANON = "apikey: " + ANON_KEY;

    // ========================
    // Email + Password Login
    // ========================
    @Headers({
            HEADER_CONTENT_TYPE,
            HEADER_APIKEY_ANON
    })
    @POST("auth/v1/token?grant_type=password")
    Call<LoginResponse> login(@Body LoginRequest request);

    // ========================
    // Google Sign-in (id_token)
    // ========================
    @Headers({
            HEADER_CONTENT_TYPE,
            HEADER_APIKEY_ANON
    })
    @POST("auth/v1/token?grant_type=id_token")
    Call<LoginResponse> signInWithGoogle(@Body IdTokenRequest request);

    // ========================
    // Signup
    // ========================
    @Headers({
            HEADER_CONTENT_TYPE,
            HEADER_APIKEY_ANON
    })
    @POST("auth/v1/signup")
    Call<LoginResponse> signUp(@Body LoginRequest request);

    // ========================
    // Refresh Token
    // ========================
    // This endpoint is critical for the TokenAuthenticator to silently renew the session.
    @Headers({
            HEADER_CONTENT_TYPE,
            HEADER_APIKEY_ANON
    })
    @POST("auth/v1/token?grant_type=refresh_token")
    Call<LoginResponse> refreshToken(@Body RefreshRequest request);
}