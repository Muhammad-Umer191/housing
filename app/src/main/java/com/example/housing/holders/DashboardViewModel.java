package com.example.housing.holders;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.housing.BuildConfig;
import com.example.housing.network.CategoryApi;
import com.example.housing.network.ServiceApi;
import com.example.housing.network.RetrofitClient;
import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<JsonArray> categoriesLiveData = new MutableLiveData<>();
    private MutableLiveData<JsonArray> servicesLiveData = new MutableLiveData<>();

    public MutableLiveData<JsonArray> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public MutableLiveData<JsonArray> getServicesLiveData() {
        return servicesLiveData;
    }

    public void fetchRandomCategories(Context context, String bearerToken) {
        CategoryApi api = RetrofitClient.getClient().create(CategoryApi.class);
        api.getRandomCategories("Bearer " + bearerToken, BuildConfig.SUPABASE_ANON_KEY)
                .enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            categoriesLiveData.setValue(response.body());
                        } else {
                            Toast.makeText(context, "Failed to load categories", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Toast.makeText(context, "Error fetching categories: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void fetchRandomServices(Context context, String bearerToken) {
        ServiceApi api = RetrofitClient.getClient().create(ServiceApi.class);
        api.getRandomServices("Bearer " + bearerToken, BuildConfig.SUPABASE_ANON_KEY)
                .enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            servicesLiveData.setValue(response.body());
                        } else {
                            Toast.makeText(context, "Failed to load services", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Toast.makeText(context, "Error fetching services: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
