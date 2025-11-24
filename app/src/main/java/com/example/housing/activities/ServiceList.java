package com.example.housing.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.adapters.ServiceListAdapter;
import com.example.housing.holders.ServiceListViewModel;
import com.example.housing.models.ServiceMutable;
import com.example.housing.utils.PrefManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView categoryTitle;
    private ImageButton btnListView, btnGridView;
    PrefManager pref;

    private ServiceListViewModel viewModel;


    private boolean isGrid = false;
    private List<ServiceMutable> services = new ArrayList<>();
    private ServiceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        recyclerView = findViewById(R.id.service);
        categoryTitle = findViewById(R.id.category_title);
        btnListView = findViewById(R.id.button_list_view);
        btnGridView = findViewById(R.id.grid_view);
        pref = PrefManager.getInstance(ServiceList.this);

        adapter = new ServiceListAdapter(this, services, isGrid);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setupViewToggle();
        setupViewModel();

        String categoryId = getIntent().getStringExtra("category_id");
        String keyword = getIntent().getStringExtra("keyword");

        if (categoryId != null) {
            categoryTitle.setText("Category Services");
            viewModel.fetchServicesByCategory(
                    this,
                    pref.getAccessToken(),
                    pref.getUserId(),
                    categoryId,
                    20,
                    0
            );
        } else if (keyword != null) {
            categoryTitle.setText("Search: " + keyword);
            viewModel.fetchServicesByKeyword(
                    this,
                    pref.getAccessToken(),
                    pref.getUserId(),
                    keyword,
                    20,
                    0
            );
        }


        ImageButton searchButton = findViewById(R.id.search_button);
        EditText searchInput = findViewById(R.id.search);

        searchButton.setOnClickListener(v -> {
            String keyword_entered = searchInput.getText().toString().trim();
            if (!keyword.isEmpty()) {
                categoryTitle.setText("Search: " + keyword);

                viewModel.fetchServicesByKeyword(
                        this,
                        pref.getAccessToken(),
                        pref.getUserId(),
                        keyword_entered,
                        20,
                        0
                );
            } else {
                Toast.makeText(ServiceList.this, "Please enter a keyword", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setupViewToggle() {
        btnListView.setOnClickListener(v -> {
            isGrid = false;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter.setGridLayout(false);
        });

        btnGridView.setOnClickListener(v -> {
            isGrid = true;
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            adapter.setGridLayout(true);
        });
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ServiceListViewModel.class);
        viewModel.getServiceListLiveData().observe(this, this::updateServices);
    }

    private void updateServices(JsonArray jsonArray) {
        services.clear();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject obj = jsonArray.get(i).getAsJsonObject();
            ServiceMutable service = new ServiceMutable();
            service.setServiceId(obj.get("service_id").getAsString());
            service.setName(obj.get("name").getAsString());
            service.setDescription(obj.get("description").getAsString());
            service.setBasePrice(obj.get("base_price").getAsDouble());
            service.setImageUrl(obj.get("image_url").getAsString());
            service.setAvgRating(obj.get("avg_rating").getAsDouble());
            service.setReviewCount(obj.get("review_count").getAsInt());
            services.add(service);
        }
        adapter.notifyDataSetChanged();
    }
}
