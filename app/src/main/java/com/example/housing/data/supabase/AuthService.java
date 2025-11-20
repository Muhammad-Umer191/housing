package com.example.housing.data.supabase;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService
{
    @Headers({
            "Content-Type: application/json",
            "apikey: " + SupabaseConfig.SUPABASE_ANON_KEY
    })
    @POST("/auth/v1/signup")
    Call<JsonObject> signUp(@Body JsonObject body);


    @Headers({
            "Content-Type: application/json",
            "apikey: " + SupabaseConfig.SUPABASE_ANON_KEY
    })
    @POST("/auth/v1/token")
    Call<JsonObject> signIn(
            @Query("grant_type") String grantType,
            @Body JsonObject body
    );
}
