package com.example.housing.holders;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.housing.BuildConfig;
import com.example.housing.network.RetrofitClient;
import com.example.housing.network.ServiceApi;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceListViewModel extends ViewModel
{
    private MutableLiveData<JsonArray> serviceListLiveData = new MutableLiveData<>();

    public MutableLiveData<JsonArray> getServiceListLiveData()
    {
        return serviceListLiveData;
    }

    // ---------------------------------------------------------
    // 1. Load Paginated Services (no filter)
    // ---------------------------------------------------------
    public void fetchServicesByKeyword(Context context, String bearerToken, String userId,String keyword, int limit, int offset)
    {
        ServiceApi api = RetrofitClient.getClient().create(ServiceApi.class);

        JsonObject body = new JsonObject();
        body.addProperty("p_user_id", userId);
        body.addProperty("p_category_id", (String) null);
        body.addProperty("p_keyword", keyword);
        body.addProperty("p_limit", limit);
        body.addProperty("p_offset", offset);

        api.getServices("Bearer " + bearerToken, BuildConfig.SUPABASE_ANON_KEY, body)
                .enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response)
                    {
                        if (response.isSuccessful() && response.body() != null)
                        {
                            serviceListLiveData.setValue(response.body());
                        }
                        else
                        {
                            Toast.makeText(context, "Failed to load services", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t)
                    {
                        Toast.makeText(context, "Error fetching services: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void fetchServicesByCategory(Context context, String bearerToken, String userId, String categoryId, int limit, int offset)
    {
        ServiceApi api = RetrofitClient.getClient().create(ServiceApi.class);

        JsonObject body = new JsonObject();
        body.addProperty("p_user_id", userId);
        body.addProperty("p_category_id", categoryId);
        body.addProperty("p_keyword", (String) null);
        body.addProperty("p_limit", limit);
        body.addProperty("p_offset", offset);


        api.getServices("Bearer " + bearerToken, BuildConfig.SUPABASE_ANON_KEY, body)
                .enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response)
                    {
                        if (response.isSuccessful() && response.body() != null)
                        {
                            serviceListLiveData.setValue(response.body());
                        }
                        else
                        {
                            Toast.makeText(context, "Failed to load category services", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t)
                    {
                        Toast.makeText(context, "Error fetching category services: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
