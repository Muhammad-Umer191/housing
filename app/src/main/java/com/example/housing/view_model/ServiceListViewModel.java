package com.example.housing.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.housing.R;
import com.example.housing.models.ServiceItem;

import java.util.ArrayList;
import java.util.List;

public class ServiceListViewModel extends ViewModel
{
    private final MutableLiveData<List<ServiceItem>> servicesLiveData = new MutableLiveData<>();

    public ServiceListViewModel()
    {
        loadServices();
    }

    public LiveData<List<ServiceItem>> getServices()
    {
        return servicesLiveData;
    }

    private void loadServices()
    {
        List<ServiceItem> list = new ArrayList<>();
        list.add(new ServiceItem(R.drawable.ac_repair, "4.9 (248 Services)", "AC Check-Up", "Starts From", "$128", "service_001"));
        list.add(new ServiceItem(R.drawable.plumbing, "4.7 (120 Services)", "Plumbing Inspection", "Starts From", "$95", "service_002"));
        list.add(new ServiceItem(R.drawable.electronics, "4.8 (180 Services)", "Electrical Repair", "Starts From", "$110", "service_003"));

        servicesLiveData.setValue(list);
    }
}
