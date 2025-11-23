package com.example.housing.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.housing.models.Category;
import com.example.housing.models.Service;
import com.example.housing.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends ViewModel
{
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<List<Service>> services = new MutableLiveData<>();

    public DashboardViewModel()
    {
        loadCategories();
        loadServices();
    }

    public LiveData<List<Category>> getCategories() { return categories; }
    public LiveData<List<Service>> getServices() { return services; }

    private void loadCategories()
    {
//        RetrofitClient.getSupabaseApi().getAllCategories().enqueue(new Callback<List<Category>>()
//        {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response)
//            {
//                if (response.isSuccessful() && response.body() != null)
//                {
//                    categories.setValue(response.body());
//                }
//                else
//                {
//                    categories.setValue(new ArrayList<>());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t)
//            {
//                categories.setValue(new ArrayList<>());
//            }
//        });
    }

    private void loadServices()
    {
//        // Fetch all services without category filter for dashboard
//        RetrofitClient.getSupabaseApi().getServicesByCategoryId("").enqueue(new Callback<List<Service>>()
//        {
//            @Override
//            public void onResponse(Call<List<Service>> call, Response<List<Service>> response)
//            {
//                if (response.isSuccessful() && response.body() != null)
//                {
//                    services.setValue(response.body());
//                }
//                else
//                {
//                    services.setValue(new ArrayList<>());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Service>> call, Throwable t)
//            {
//                services.setValue(new ArrayList<>());
//            }
//        });
    }
}
