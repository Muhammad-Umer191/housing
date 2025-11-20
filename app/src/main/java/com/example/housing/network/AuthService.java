package com.example.housing.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthService
{
    String ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InB4dWJvcWFicmdhYml6cXJ4ZG1iIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjMzNzQwNjgsImV4cCI6MjA3ODk1MDA2OH0.pVlWoKdkdsPKGcIn1EN_ZpEL8kR_Zvb6_2oYLYeCGaM";

    @Headers({
            "Content-Type: application/json",
            "apikey: " + ANON_KEY
    })
    @POST("auth/v1/token?grant_type=password")
    Call<LoginResponse> login(@Body LoginRequest request);

    @Headers({
            "Content-Type: application/json",
            "apikey: " + ANON_KEY
    })
    @POST("auth/v1/token?grant_type=id_token")
    Call<LoginResponse> signInWithGoogle(@Body IdTokenRequest request);

    @Headers({
            "Content-Type: application/json",
            "apikey: " + ANON_KEY
    })
    @POST("auth/v1/signup") // <-- Correct endpoint for user creation
    Call<LoginResponse> signUp(@Body LoginRequest request);
}