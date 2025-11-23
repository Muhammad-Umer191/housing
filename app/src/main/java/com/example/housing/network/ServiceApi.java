package com.example.housing.network;

import com.example.housing.models.Service;
import com.example.housing.models.ServiceItem;
import com.example.housing.models.Category;
import com.example.housing.models.ServiceWithCategory;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Body;
import retrofit2.http.Headers;

public interface ServiceApi {

    // -------------------- CRUD --------------------
    @GET("categories")
    Call<List<Category>> getAllCategories();

    @GET("services")
    Call<List<Service>> getServicesByCategoryId(@Query("category_id") String filter);

    @GET("services")
    Call<List<Service>> getServiceDetails(@Query("id") String filter);

    @GET("services")
    Call<List<Service>> searchServices(@Query("query") String query);

    @GET("service_items")
    Call<List<ServiceItem>> getServiceItems();

    @Headers({"Content-Type: application/json", "Prefer: return=representation"})
    @POST("services")
    Call<Service> createService(@Body Service service);

    // -------------------- RPC --------------------
    @GET("rpc/get_service_with_category")
    Call<List<ServiceWithCategory>> getServiceWithCategory(@Query("service_id") String serviceId);

    @GET("rpc/get_service_items_with_details")
    Call<List<ServiceItem>> getServiceItemsWithDetails(@Query("service_id") String serviceId);
}
