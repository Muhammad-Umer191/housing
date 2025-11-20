package com.example.housing.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.activities.ServiceDetail;
import com.example.housing.models.ServiceItem;

import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceViewHolder>
{
    private final Context context;
    private final List<ServiceItem> list;
    private final String category;

    private boolean isGrid = false;

    public ServiceListAdapter(Context context, List<ServiceItem> list, String category)
    {
        this.context = context;
        this.list = list;
        this.category = category;
    }

    // ---------- FIXED: Only ONE onCreateViewHolder ----------
    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        int layout = isGrid ? R.layout.item_service_grid_card
                : R.layout.item_service_list_card;

        View view = LayoutInflater.from(context).inflate(layout, parent, false);

        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position)
    {
        ServiceItem item = list.get(position);

        holder.title.setText(item.getTitle());
        holder.rating.setText(item.getRating());
        holder.priceLabel.setText(item.getPrice());
        holder.priceValue.setText("$" + item.getPriceValue());

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, ServiceDetail.class);
            i.putExtra("service_id", item.getId());
            i.putExtra("title", item.getTitle());
            i.putExtra("price", item.getPrice());
            i.putExtra("rating", item.getRating());
            i.putExtra("category", category);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder
    {
        TextView rating, title, priceLabel, priceValue;

        public ServiceViewHolder(@NonNull View itemView)
        {
            super(itemView);

            rating = itemView.findViewById(R.id.rating);
            title = itemView.findViewById(R.id.service_title);
            priceLabel = itemView.findViewById(R.id.price);
            priceValue = itemView.findViewById(R.id.price_value);
        }
    }

    public void setGridLayout(boolean grid)
    {
        this.isGrid = grid;
        notifyDataSetChanged();
    }
}
