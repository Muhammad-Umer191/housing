package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.adapters.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity
{
    private EditText searchField;
    private ImageButton searchButton;
    private RecyclerView categoryRecyclerView;

    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category); // Use a new layout for activity

        searchField = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.search_button);
        categoryRecyclerView = findViewById(R.id.category_recycler_view);

        setupRecyclerView();
        fetchCategoriesFromDb();
        setupSearch();
    }

    private void setupRecyclerView()
    {
//        categoryAdapter = new CategoryAdapter(this, categoryList, (category, position) -> {
//            Intent intent = new Intent(Category.this, ServiceList.class);
//            intent.putExtra("category_name", category.getName());
//            startActivity(intent);
//        });
//
//        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void fetchCategoriesFromDb()
    {
        // Uncomment and adjust for your API
//        RetrofitClient.getSupabaseApi().getAllCategories().enqueue(new Callback<List<Category>>() {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response)
//            {
//                if (response.isSuccessful() && response.body() != null)
//                {
//                    categoryList.clear();
//                    categoryList.addAll(response.body());
//                    categoryAdapter.notifyDataSetChanged();
//                }
//                else
//                {
//                    Toast.makeText(Category.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                Toast.makeText(Category.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void setupSearch()
    {
        searchButton.setOnClickListener(v -> {
            String query = searchField.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                Intent intent = new Intent(Category.this, ServiceList.class);
                intent.putExtra("search_query", query);
                startActivity(intent);
            } else {
                Toast.makeText(Category.this, "Enter something to search", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
