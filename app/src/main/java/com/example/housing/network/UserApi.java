package com.example.housing.network;

import com.example.housing.models.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    @POST("/rest/v1/rpc/insert_user_if_not_exists")
    Call<JsonObject> insertUser(
            @Header("Authorization") String bearerToken,
            @Header("apikey") String apiKey,
            @Body JsonObject body
    );


    @GET("/rest/v1/users")
    Call<List<User>> getUserByEmail(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authBearer,
            @Query("email") String emailEq
    );

    @GET("/rest/v1/users")
    Call<JsonArray> getUser(
            @Header("Authorization") String authToken,
            @Header("apikey") String anonKey,
            @Query("id") String idQuery
    );


    @PATCH("/rest/v1/users")
    Call<JsonArray> updateUser(
            @Header("Authorization") String token,
            @Header("apikey") String anonKey,
            @Header("Prefer") String prefer,
            @Query("id") String idFilter,
            @Body JsonObject body
    );



}
