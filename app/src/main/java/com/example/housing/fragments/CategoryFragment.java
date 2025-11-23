package com.example.housing.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.activities.ServiceList;
import com.example.housing.adapters.CategoryAdapter;
import com.example.housing.models.Category;
import com.example.housing.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment
{
    private EditText searchField;
    private ImageButton searchButton;
    private RecyclerView categoryRecyclerView;

    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        searchField = view.findViewById(R.id.search_edit_text);
        searchButton = view.findViewById(R.id.search_button);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);

        setupRecyclerView();
        fetchCategoriesFromDb();
        setupSearch();

        return view;
    }

    private void setupRecyclerView()
    {
        categoryAdapter = new CategoryAdapter(getContext(), categoryList, (category, position) -> {
            Intent intent = new Intent(requireContext(), ServiceList.class);
            intent.putExtra("category_name", category.getName());
            startActivity(intent);
        });

        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void fetchCategoriesFromDb()
    {
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
//                    Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void setupSearch()
    {
        searchButton.setOnClickListener(v -> {
            String query = searchField.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                Intent intent = new Intent(requireContext(), ServiceList.class);
                intent.putExtra("search_query", query);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Enter something to search", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
