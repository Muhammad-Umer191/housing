package com.example.housing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.housing.R;
import com.example.housing.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>
{
    private final List<Category> categoryList;
    private final OnCategoryClickListener clickListener;
    private final Context context;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int position);
    }

    public CategoryAdapter(Context context, List<Category> categoryList, OnCategoryClickListener clickListener)
    {
        this.context = context;
        this.categoryList = categoryList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_icon, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position)
    {
        Category category = categoryList.get(position);
        holder.bind(category, clickListener, context);
    }

    @Override
    public int getItemCount()
    {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView categoryName;
        private final ImageView categoryIcon;

        public CategoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            categoryIcon = itemView.findViewById(R.id.category_icon);
        }

        public void bind(Category category, OnCategoryClickListener clickListener, Context context)
        {
            categoryName.setText(category.getName());

            if (category.getIconUrl() != null && !category.getIconUrl().isEmpty())
            {
                Glide.with(context)
                        .load(category.getIconUrl())
                        .placeholder(R.drawable.housing)
                        .into(categoryIcon);
            }
            else
            {
                categoryIcon.setImageResource(R.drawable.housing);
            }

            itemView.setOnClickListener(v -> clickListener.onCategoryClick(category, getAdapterPosition()));
        }
    }
}
