package com.example.housing.holders;

import android.os.Build;
import android.text.style.BulletSpan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.housing.BuildConfig;
import com.example.housing.network.CategoryApi;
import com.example.housing.network.RetrofitClient;
import com.example.housing.network.ServiceApi;
import com.example.housing.network.UserApi;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfoViewModel extends ViewModel {

    private MutableLiveData<JsonObject> userDetailsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<JsonObject>> categoriesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<JsonObject>> userServicesLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> updateSuccessLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    private UserApi userApi;
    private ServiceApi serviceApi;

    private CategoryApi categoryApi;

    public PersonalInfoViewModel() {
        userApi = RetrofitClient.getClient().create(UserApi.class);
        serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
        categoryApi = RetrofitClient.getClient().create(CategoryApi.class);

    }

    public LiveData<JsonObject> getUserDetailsLiveData() { return userDetailsLiveData; }
    public LiveData<Boolean> getUpdateSuccessLiveData() { return updateSuccessLiveData; }
    public LiveData<String> getErrorMessageLiveData() { return errorMessageLiveData; }

    public void fetchUserDetails(String token, String userId) {
        userApi.getUser("Bearer " + token, BuildConfig.SUPABASE_ANON_KEY, "eq." + userId)
                .enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            JsonObject userObj = response.body().get(0).getAsJsonObject();
                            userDetailsLiveData.setValue(userObj);
                        } else {
                            errorMessageLiveData.setValue("Failed to fetch user data from DB");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        errorMessageLiveData.setValue(t.getMessage());
                    }
                });
    }


    public void updateUser(String token, String userId, JsonObject updates) {
        userApi.updateUser("Bearer " + token, BuildConfig.SUPABASE_ANON_KEY,"return=representation", "eq." + userId, updates).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    updateSuccessLiveData.setValue(true);
                } else {
                    errorMessageLiveData.setValue("Failed to update profile");
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                errorMessageLiveData.setValue(t.getMessage());
            }
        });
    }

    public void fetchCategories(String token) {
        categoryApi.getCategories("Bearer" +token, BuildConfig.SUPABASE_ANON_KEY).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<JsonObject> list = new ArrayList<>();
                    for (int i = 0; i < response.body().size(); i++) {
                        list.add(response.body().get(i).getAsJsonObject());
                    }
                    categoriesLiveData.setValue(list);
                } else {
                    errorMessageLiveData.setValue("Failed to fetch categories");
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                errorMessageLiveData.setValue(t.getMessage());
            }
        });
    }

    public void fetchUserServices(String token, String userId, String categoryId) {
        serviceApi.getUserServices("Bearer" + token, BuildConfig.SUPABASE_ANON_KEY, userId).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<JsonObject> list = new ArrayList<>();
                    for (int i = 0; i < response.body().size(); i++) {
                        JsonObject s = response.body().get(i).getAsJsonObject();
                        if (s.has("category_id") && s.get("category_id").getAsString().equals(categoryId)) {
                            list.add(s);
                        }
                    }
                    userServicesLiveData.setValue(list);
                } else {
                    errorMessageLiveData.setValue("Failed to fetch services");
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                errorMessageLiveData.setValue(t.getMessage());
            }
        });
    }

    public LiveData<List<JsonObject>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public LiveData<List<JsonObject>> getUserServicesLiveData() {
        return userServicesLiveData;
    }


    public void addService(String token, String userId, String categoryId, String serviceName, String description, int price, String imageUrl) {
        JsonObject body = new JsonObject();
        body.addProperty("p_category_id", categoryId);
        body.addProperty("p_name", serviceName);
        body.addProperty("p_description", description);
        body.addProperty("p_base_price", price);
        body.addProperty("p_image_url", imageUrl);

        serviceApi.createService("Bearer " + token, BuildConfig.SUPABASE_ANON_KEY, body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    fetchUserServices(token, userId, categoryId);
                } else {
                    errorMessageLiveData.setValue("Failed to add service");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                errorMessageLiveData.setValue(t.getMessage());
            }
        });

    }

}
