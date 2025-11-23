package com.example.housing.network;

import com.example.housing.models.Review;
import com.example.housing.models.ReviewWithUserInfo;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;
import retrofit2.http.Headers;

public interface ReviewApi {

    // -------------------- CRUD --------------------
    @Headers({"Content-Type: application/json"})
    @POST("reviews")
    Call<Review> submitReview(@Body Review review);

    @GET("reviews")
    Call<List<Review>> getReviewsForProfessional(@Query("professional_id") String professionalId);

    // -------------------- RPC --------------------
    @GET("rpc/get_reviews_with_user_info")
    Call<List<ReviewWithUserInfo>> getReviewsWithUserInfo(@Query("professional_id") String professionalId);
}
