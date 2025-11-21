package com.example.housing.view_model;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.housing.models.Category;
import com.example.housing.models.Service;

import com.example.housing.R;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel
{
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<List<Service>> services = new MutableLiveData<>();
    private final MutableLiveData<List<Offer>> offers = new MutableLiveData<>();

    public DashboardViewModel()
    {
        loadCategories();
        loadServices();
        loadOffers();
    }

    private void loadCategories()
    {
        List<Category> list = new ArrayList<>();
        list.add(new Category("Cleaning", R.drawable.cleaning));
        list.add(new Category("Plumbing", R.drawable.plumbing));
        list.add(new Category("Electric", R.drawable.ac_repair));
        list.add(new Category("See all", R.drawable.see_all));
        categories.setValue(list);
    }

    private void loadServices()
    {
        List<Service> list = new ArrayList<>();
        list.add(new Service("Home Cleaning", "$25/hr", R.drawable.cleaning));
        list.add(new Service("Plumbing Fix", "$40/hr", R.drawable.plumbing));
        list.add(new Service("Electrical Repair", "$35/hr", R.drawable.electronics));
        services.setValue(list);
    }

    private void loadOffers()
    {
        List<Offer> list = new ArrayList<>();
        offers.setValue(list);
    }

    public LiveData<List<Category>> getCategories() { return categories; }

    public LiveData<List<Service>> getServices() { return services; }

    public LiveData<List<Offer>> getOffers() { return offers; }
}
