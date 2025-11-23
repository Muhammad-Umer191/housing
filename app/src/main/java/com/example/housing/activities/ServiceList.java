package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.adapters.ServiceListAdapter;
import com.example.housing.models.Service;
import com.example.housing.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceList extends AppCompatActivity
{
    private String categoryName;
    private String searchQuery;

    private RecyclerView recyclerView;
    private TextView categoryTitle;

    private ImageButton btnListView, btnGridView;
    private boolean isGrid = false;

    private final List<Service> serviceList = new ArrayList<>();
    private ServiceListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        recyclerView = findViewById(R.id.service);
        categoryTitle = findViewById(R.id.category_title);
        btnListView = findViewById(R.id.button_list_view);
        btnGridView = findViewById(R.id.grid_view);

        getIntentData();
        setupRecycler();
        setupViewToggle();
        loadServicesFromDb();
    }

    private void getIntentData()
    {
        Intent i = getIntent();
        categoryName = i.getStringExtra("category_name");
        searchQuery = i.getStringExtra("search_query");

        if (categoryName != null)
        {
            categoryTitle.setText(categoryName);
        }
        else if (searchQuery != null)
        {
            categoryTitle.setText("Search: " + searchQuery);
        }
    }

    private void setupRecycler()
    {
        adapter = new ServiceListAdapter(this, serviceList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupViewToggle()
    {
        btnListView.setOnClickListener(v -> switchToListView());
        btnGridView.setOnClickListener(v -> switchToGridView());
    }

    private void switchToListView()
    {
        if (!isGrid) return;
        isGrid = false;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setGridLayout(false);

        btnListView.setColorFilter(getColor(R.color.purple_500));
        btnGridView.setColorFilter(getColor(R.color.gray_500));
    }

    private void switchToGridView()
    {
        if (isGrid) return;
        isGrid = true;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter.setGridLayout(true);

        btnGridView.setColorFilter(getColor(R.color.purple_500));
        btnListView.setColorFilter(getColor(R.color.gray_500));
    }

    private void loadServicesFromDb()
    {
//        String query = searchQuery != null ? searchQuery : categoryName;
//
//        if (query == null || query.isEmpty()) return;
//
//        RetrofitClient.getSupabaseApi().searchServices(query)
//                .enqueue(new Callback<List<Service>>() {
//                    @Override
//                    public void onResponse(Call<List<Service>> call, Response<List<Service>> response)
//                    {
//                        if (response.isSuccessful() && response.body() != null)
//                        {
//                            serviceList.clear();
//                            serviceList.addAll(response.body());
//                            adapter.notifyDataSetChanged();
//                        }
//                        else
//                        {
//                            Toast.makeText(ServiceList.this, "No services found", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Service>> call, Throwable t)
//                    {
//                        Toast.makeText(ServiceList.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }
}
