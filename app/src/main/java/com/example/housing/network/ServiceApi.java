package com.example.housing.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface ServiceApi {

    // Get all services of a user
    @GET("/rest/v1/services")
    Call<JsonArray> getUserServices(
            @Header("Authorization") String token,
            @Header("apiKey") String apiKey,
            @Query("user_id") String userId
    );


    @POST("/rest/v1/rpc/add_service_for_current_user")
    Call<JsonObject> createService(
            @Header("Authorization") String token,
            @Header("apiKey") String apiKey,
            @Body JsonObject body
    );

    @GET("/rest/v1/rpc/get_random_services")
    Call<JsonArray> getRandomServices(
            @Header("Authorization") String bearerToken,
            @Header("apiKey") String apiKey
    );

    @POST("/rest/v1/rpc/get_services_page_auth")
    Call<JsonArray> getServices(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Body JsonObject body
    );

    @POST("/rest/v1/rpc/get_service_reviews_auth")
    Call<JsonArray> getServiceReviews(
            @Header("Authorization") String token,
            @Header("apikey") String apiKey,
            @Body JsonObject body
    );

}
