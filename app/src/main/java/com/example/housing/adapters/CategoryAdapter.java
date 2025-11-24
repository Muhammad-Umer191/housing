package com.example.housing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final JsonArray categories;
    private final OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryId);
    }

    // Constructor
    public CategoryAdapter(JsonArray categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_icon, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        JsonObject category = categories.get(position).getAsJsonObject();

        String name = category.has("name") && !category.get("name").isJsonNull() ? category.get("name").getAsString() : "Category";holder.category_name.setText(name);

        String iconName = category.has("icon_url") && !category.get("icon_url").isJsonNull() ? category.get("icon_url").getAsString() : "housing";
        int resId = holder.itemView.getContext().getResources().getIdentifier(iconName, "drawable", holder.itemView.getContext().getPackageName());

        if (resId != 0) {
            holder.category_icon.setImageResource(resId);
        } else {
            holder.category_icon.setImageResource(R.drawable.housing); // fallback
        }

        holder.itemView.setOnClickListener(v -> {
            try {
                if (category.has("id") && !category.get("id").isJsonNull()) {
                    String categoryId = category.get("id").getAsString();
                    listener.onCategoryClick(categoryId);
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Category ID missing", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(holder.itemView.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView category_name;
        ImageView category_icon;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name = itemView.findViewById(R.id.category_name);
            category_icon = itemView.findViewById(R.id.category_icon);
        }
    }
}
