package com.example.housing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.widget.NestedScrollView;

import com.example.housing.R;
import com.example.housing.adapters.CategoryAdapter;
import com.example.housing.adapters.OfferAdapter;
import com.example.housing.adapters.ServiceAdapter;
import com.example.housing.view_model.DashboardViewModel;
import com.google.android.material.tabs.TabLayout;

public class DashboardFragment extends Fragment
{
    private EditText searchField;
    private ImageButton searchButton;
    private RecyclerView categoryRecyclerView;
    private RecyclerView serviceRecyclerView;
    private RecyclerView offersRecyclerView;
    private TabLayout bottomTabs;
    private NestedScrollView scrollView;
    private View container;

    private DashboardViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup containerParent,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_dashboard, containerParent, false);

        searchField = view.findViewById(R.id.search_edit_text);
        searchButton = view.findViewById(R.id.search_button);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        serviceRecyclerView = view.findViewById(R.id.service_recycler_view);
        offersRecyclerView = view.findViewById(R.id.offers_recycler_view);
        bottomTabs = view.findViewById(R.id.bottom_tabs);
        scrollView = view.findViewById(R.id.dashboard_scroll_view);
        container = view.findViewById(R.id.dashboard_fragment_container);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        setupObservers();

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
                    // Handle other category clicks if needed
                }
            });

            categoryRecyclerView.setLayoutManager(
                    new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
            );
            categoryRecyclerView.setAdapter(adapter);
        });

        viewModel.getServices().observe(getViewLifecycleOwner(), services ->
        {
            ServiceAdapter adapter = new ServiceAdapter(services);
            serviceRecyclerView.setLayoutManager(
                    new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
            );
            serviceRecyclerView.setAdapter(adapter);
        });

        viewModel.getOffers().observe(getViewLifecycleOwner(), offers ->
        {
            OfferAdapter adapter = new OfferAdapter(offers);
            offersRecyclerView.setLayoutManager(
                    new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
            );
            offersRecyclerView.setAdapter(adapter);
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

}
