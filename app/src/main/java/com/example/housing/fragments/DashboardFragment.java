package com.example.housing.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.activities.Category;
import com.example.housing.activities.ServiceDetail;
import com.example.housing.activities.ServiceList;
import com.example.housing.adapters.CategoryAdapter;
import com.example.housing.adapters.ServiceAdapter;
import com.example.housing.holders.DashboardViewModel;
import com.example.housing.utils.PrefManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private EditText search_edit_text;
    private ImageButton search_button, see_all;
    private RecyclerView category_recycler_view, service_recycler_view;
    private DashboardViewModel dashboardViewModel;
    private PrefManager pref;

    private TextView address;
    private FusedLocationProviderClient fusedLocationClient;
    private int categoryRetryCount = 0;
    private int serviceRetryCount = 0;
    private final int MAX_RETRIES = 5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        search_edit_text = view.findViewById(R.id.search_edit_text);
        search_button = view.findViewById(R.id.search_button);
        category_recycler_view = view.findViewById(R.id.category_recycler_view);
        service_recycler_view = view.findViewById(R.id.service_recycler_view);
        address = view.findViewById(R.id.address);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        see_all = view.findViewById(R.id.see_all);

        pref = PrefManager.getInstance(getContext());
        category_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        service_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        fetchCategoriesWithRetry();
        fetchServicesWithRetry();

        search_button.setOnClickListener(v -> {
            String keyword = search_edit_text.getText().toString().trim();
            if (keyword.isEmpty()) {
                Toast.makeText(getContext(), "Enter keyword", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getContext(), ServiceList.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
            }
        });

        see_all.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), Category.class);startActivity(intent);
        });

        getCurrentLocation();

        return view;
    }

    private void fetchCategoriesWithRetry() {
        dashboardViewModel.fetchRandomCategories(getContext(), pref.getAccessToken());
        dashboardViewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && categories.size() > 0) {
                updateCategories(categories);
            } else {
                categoryRetryCount++;
                if (categoryRetryCount <= MAX_RETRIES) {
                    fetchCategoriesWithRetry();
                } else {
                    Toast.makeText(getContext(), "Failed to load categories after multiple attempts", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchServicesWithRetry() {
        dashboardViewModel.fetchRandomServices(getContext(), pref.getAccessToken());
        dashboardViewModel.getServicesLiveData().observe(getViewLifecycleOwner(), services -> {
            if (services != null && services.size() > 0) {
                updateServices(services);
            } else {
                serviceRetryCount++;
                if (serviceRetryCount <= MAX_RETRIES) {
                    fetchServicesWithRetry();
                } else {
                    Toast.makeText(getContext(), "Failed to load services after multiple attempts", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateCategories(JsonArray categories) {
        if (categories != null && categories.size() > 0) {
            CategoryAdapter adapter = new CategoryAdapter(categories, categoryId -> {
                if (categoryId != null && !categoryId.isEmpty()) {
                    Intent intent = new Intent(requireContext(), ServiceList.class);
                    intent.putExtra("category_id", categoryId);
                    startActivity(intent);
                } else {
                    Toast.makeText(requireContext(), "Invalid category", Toast.LENGTH_SHORT).show();
                }
            });
            category_recycler_view.setAdapter(adapter);
        }
    }




    private void updateServices(JsonArray services) {
        if (services != null && services.size() > 0) {
            ServiceAdapter adapter = new ServiceAdapter(services);
            service_recycler_view.setAdapter(adapter);
        }
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;

                Location location = locationResult.getLastLocation();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            address.setText(addresses.get(0).getAddressLine(0));
                        }
                    } catch (IOException e) {
                        address.setText("Unknown Location");
                    }
                }
                fusedLocationClient.removeLocationUpdates(this);
            }
        }, Looper.getMainLooper());
    }
}
