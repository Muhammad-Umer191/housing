package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.adapters.ServiceListAdapter;
import com.example.housing.models.ServiceItem;

import java.util.ArrayList;
import java.util.List;

public class ServiceList extends AppCompatActivity
{
    private String categoryName;

    private RecyclerView recyclerView;
    private TextView categoryTitle;

    private ImageButton btnListView, btnGridView;

    private boolean isGrid = false;

    private final List<ServiceItem> staticList = new ArrayList<>();

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
        loadStaticData();
        setupRecycler();
        setupViewToggle();
    }

    private void getIntentData()
    {
        Intent i = getIntent();
        categoryName = i.getStringExtra("category_name");

        if (categoryName != null)
        {
            categoryTitle.setText(categoryName);
        }
    }

    private void loadStaticData()
    {
        staticList.add(new ServiceItem(
                R.drawable.ac_repair,
                "4.9",
                "AC Check-Up",
                "Starts From",
                "128",
                "s1"
        ));

        staticList.add(new ServiceItem(
                R.drawable.plumbing,
                "4.7",
                "Plumbing Inspection",
                "Starts From",
                "95",
                "s2"
        ));

        staticList.add(new ServiceItem(
                R.drawable.electronics,
                "4.8",
                "Electrical Repair",
                "Starts From",
                "110",
                "s3"
        ));
        staticList.add(new ServiceItem(
                R.drawable.plumbing,
                "4.7",
                "Plumbing Inspection",
                "Starts From",
                "95",
                "s2"
        ));

        staticList.add(new ServiceItem(
                R.drawable.electronics,
                "4.8",
                "Electrical Repair",
                "Starts From",
                "110",
                "s3"
        ));
        staticList.add(new ServiceItem(
                R.drawable.plumbing,
                "4.7",
                "Plumbing Inspection",
                "Starts From",
                "95",
                "s2"
        ));

        staticList.add(new ServiceItem(
                R.drawable.electronics,
                "4.8",
                "Electrical Repair",
                "Starts From",
                "110",
                "s3"
        ));
    }

    private void setupRecycler()
    {
        adapter = new ServiceListAdapter(this, staticList, categoryName);

        // Default view → LIST VIEW
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
}
