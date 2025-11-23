package com.example.housing.network;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ResendApi
{
    @Headers({
            "Content-Type: application/json"
    })
    @POST("emails")
    Call<Void> sendEmail(
            @Header("Authorization") String apiKey,
            @Body Map<String, Object> body
    );
}
