package com.example.housing.network;

import com.example.housing.BuildConfig;
import com.google.gson.JsonObject;
import com.example.housing.utils.PrefManager;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/auth/v1/recover")
    Call<Void> resetPassword(
            @Header("apikey") String apiKey,
            @Body Map<String, String> body
    );

    @PATCH("/auth/v1/user")
    Call<JsonObject> updatePassword(
            @Header("Authorization") String bearerToken,
            @Body JsonObject body
    );

    @POST("/auth/v1/token?grant_type=password")
    @Headers({
            "Content-Type: application/json",
            "apikey: " + BuildConfig.SUPABASE_ANON_KEY
    })
    Call<JsonObject> signIn(@Body JsonObject body);

    @POST("/auth/v1/signup")
    @Headers({
            "Content-Type: application/json",
            "apikey: " + BuildConfig.SUPABASE_ANON_KEY
    })
    Call<JsonObject> signUp(@Body JsonObject body);

    @POST("/auth/v1/token?grant_type=refresh_token")
    @Headers({
            "Content-Type: application/json",
            "apikey: " + BuildConfig.SUPABASE_ANON_KEY
    })
    Call<JsonObject> refreshSession(@Body JsonObject body);

}
