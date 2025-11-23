package com.example.housing.network;

import com.example.housing.models.Category;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApi {
    @GET("categories")
    Call<List<Category>> getAllCategories();
}
