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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.adapters.CategoryAdapter;
import com.example.housing.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment
{
    private EditText searchField;
    private ImageButton searchButton;
    private RecyclerView categoryRecyclerView;

    private List<Category> categoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        searchField = view.findViewById(R.id.search_edit_text);
        searchButton = view.findViewById(R.id.search_button);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);

        loadCategories();
        setupRecyclerView();

        return view;
    }

    private void loadCategories()
    {
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Cleaning", R.drawable.cleaning));
        categoryList.add(new Category("Plumbing", R.drawable.plumbing));
        categoryList.add(new Category("Electric", R.drawable.electronics));
        categoryList.add(new Category("Painting", R.drawable.painting));
        categoryList.add(new Category("AC Repair", R.drawable.ac_repair));
        categoryList.add(new Category("Men_Saloon", R.drawable.men_saloon));
        categoryList.add(new Category("Beauty", R.drawable.beauty));
    }

    private void setupRecyclerView()
    {
        CategoryAdapter adapter = new CategoryAdapter(categoryList, (category, position) ->
        {
            // TODO: Handle category click
        });

        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        categoryRecyclerView.setAdapter(adapter);
    }
}
