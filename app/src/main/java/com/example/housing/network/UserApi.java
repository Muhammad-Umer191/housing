package com.example.housing.network;

import com.example.housing.models.User;
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


    @GET("users")
    Call<List<User>> getUserByEmail(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authBearer,
            @Query("email") String emailEq
    );


    @GET("/users/{id}")
    Call<Map<String, Object>> getUserDetails(@Path("id") String userId);

    @PUT("/users/{id}")
    Call<Map<String, Object>> updateUserDetails(@Path("id") String userId,
                                                @Body Map<String, Object> updates);

}
