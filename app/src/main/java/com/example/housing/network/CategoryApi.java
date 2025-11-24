package com.example.housing.network;

import com.google.gson.JsonArray;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CategoryApi {
    @GET("/rest/v1/categories")
    Call<JsonArray> getCategories(
            @Header("Authorization") String token,
            @Header("apiKey") String apiKey
    );

    @GET("/rest/v1/rpc/get_random_categories")
    Call<JsonArray> getRandomCategories(
            @Header("Authorization") String bearerToken,
            @Header("apiKey") String apiKey
    );

}
