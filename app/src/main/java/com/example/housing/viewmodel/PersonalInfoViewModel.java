package com.example.housing.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.housing.models.Category;
import com.example.housing.models.Service;
import com.example.housing.network.CategoryApi;
import com.example.housing.network.RetrofitClient;
import com.example.housing.network.UserApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfoViewModel extends ViewModel {

    private UserApi userApi;
    private CategoryApi categoryApi;

    private MutableLiveData<Map<String, Object>> userDetails = new MutableLiveData<>();
    private MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private MutableLiveData<List<Service>> userServices = new MutableLiveData<>();
    private MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public PersonalInfoViewModel() {
        userApi = RetrofitClient.getClient().create(UserApi.class);
        categoryApi = RetrofitClient.getClient().create(CategoryApi.class);
    }

    public LiveData<Map<String, Object>> getUserDetailsLiveData() {
        return userDetails;
    }

    public LiveData<List<Category>> getCategoriesLiveData() {
        return categories;
    }

    public LiveData<List<Service>> getUserServicesLiveData() {
        return userServices;
    }

    public LiveData<Boolean> getUpdateSuccessLiveData() {
        return updateSuccess;
    }

    public LiveData<String> getErrorMessageLiveData() {
        return errorMessage;
    }

    public void fetchUserDetails(String userId) {
        userApi.getUserDetails(userId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userDetails.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch user details");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void fetchCategories() {
        categoryApi.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to load categories");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void fetchUserServices(String userId, String categoryId) {
//        userApi.getServicesByUserAndCategory(userId, categoryId).enqueue(new Callback<List<Service>>() {
//            @Override
//            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    userServices.postValue(response.body());
//                } else {
//                    errorMessage.postValue("Failed to fetch services");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Service>> call, Throwable t) {
//                errorMessage.postValue(t.getMessage());
//            }
//        });
    }

    public void addService(String userId, String categoryId, String serviceName) {
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId);
        body.put("category_id", categoryId);
        body.put("name", serviceName);

//        userApi.addService(body).enqueue(new Callback<Map<String, Object>>() {
//            @Override
//            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
//                if (response.isSuccessful()) {
//                    fetchUserServices(userId, categoryId); // refresh list
//                } else {
//                    errorMessage.postValue("Failed to add service");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
//                errorMessage.postValue(t.getMessage());
//            }
//        });
    }

    public void deleteService(String serviceId, String userId, String categoryId) {
//        userApi.deleteService(serviceId).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    fetchUserServices(userId, categoryId); // refresh list
//                } else {
//                    errorMessage.postValue("Failed to delete service");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                errorMessage.postValue(t.getMessage());
//            }
//        });
    }

    public void updateUser(String userId, Map<String, Object> updates) {
        userApi.updateUserDetails(userId, updates).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                updateSuccess.postValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
