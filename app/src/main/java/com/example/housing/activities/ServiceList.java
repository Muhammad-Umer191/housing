package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

    private final List<ServiceItem> staticList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        recyclerView = findViewById(R.id.service);
        categoryTitle = findViewById(R.id.category_title);

        getIntentData();
        loadStaticData();
        setupRecycler();
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
    }


    private void setupRecycler()
    {
        ServiceListAdapter adapter = new ServiceListAdapter(this, staticList, categoryName);
        recyclerView.setAdapter(adapter);
    }
}
