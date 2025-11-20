package com.example.housing.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.activities.ServiceList;
import com.example.housing.adapters.CategoryAdapter;
import com.example.housing.adapters.ServiceAdapter;
import com.example.housing.view_model.DashboardViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment
{
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private FusedLocationProviderClient fusedLocationClient;

    private EditText searchField;
    private ImageButton searchButton;
    private RecyclerView categoryRecyclerView;
    private RecyclerView serviceRecyclerView;
    private TabLayout bottomTabs;
    private NestedScrollView scrollView;
    private View container;

    private TextView addressTextView;
    private DashboardViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup containerParent,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_dashboard, containerParent, false);

        addressTextView = view.findViewById(R.id.address);
        searchField = view.findViewById(R.id.search_edit_text);
        searchButton = view.findViewById(R.id.search_button);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        serviceRecyclerView = view.findViewById(R.id.service_recycler_view);
        bottomTabs = view.findViewById(R.id.bottom_tabs);
        scrollView = view.findViewById(R.id.dashboard_scroll_view);
        container = view.findViewById(R.id.dashboard_fragment_container);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        setupObservers();
        fetchCurrentLocation();

        return view;
    }

    private void setupObservers()
    {
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories ->
        {
            CategoryAdapter adapter = new CategoryAdapter(categories, (category, position) ->
            {
                if (position == 3)
                {
                    scrollView.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);

                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.dashboard_fragment_container, new CategoryFragment())
                            .addToBackStack(null)
                            .commit();
                }
                else
                {
                    Intent intent = new Intent(requireContext(), ServiceList.class);
                    intent.putExtra("category_name", category.getName());
                    startActivity(intent);
                }
            });

            categoryRecyclerView.setLayoutManager(
                    new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
            );
            categoryRecyclerView.setAdapter(adapter);
        });

        viewModel.getServices().observe(getViewLifecycleOwner(), services -> {
            ServiceAdapter adapter = new ServiceAdapter(services);
            serviceRecyclerView.setLayoutManager(
                    new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
            );
            serviceRecyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();

        getChildFragmentManager().addOnBackStackChangedListener(() ->
        {
            if (getChildFragmentManager().getBackStackEntryCount() == 0)
            {
                container.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void fetchCurrentLocation()
    {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        setCityAddress(location);
                    } else {
                        addressTextView.setText(R.string.unknown_city);
                    }
                })
                .addOnFailureListener(e -> addressTextView.setText(R.string.error_getting_address));
    }

    private void setCityAddress(Location location)
    {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1
            );

            if (addresses != null && !addresses.isEmpty())
            {
                String city = addresses.get(0).getLocality();
                addressTextView.setText(city != null ? city : "Unknown city");
            }
            else
            {
                addressTextView.setText(R.string.unknown_city);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            addressTextView.setText(R.string.error_getting_address);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                fetchCurrentLocation();
            }
            else
            {
                addressTextView.setText(R.string.permission_denied);
            }
        }
    }
}
