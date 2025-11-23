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
import com.example.housing.models.Service;

import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceViewHolder>
{
    private final Context context;
    private final List<Service> serviceList;
    private boolean isGridLayout = false;

    public ServiceListAdapter(Context context, List<Service> serviceList)
    {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context)
                .inflate(isGridLayout ? R.layout.item_service_grid_card : R.layout.item_service_list_card, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position)
    {
        Service service = serviceList.get(position);

        holder.serviceName.setText(service.getName());
        holder.servicePrice.setText("Starts from: " + service.getBasePrice());

        // Load image using Glide
        Glide.with(context)
                .load(service.getImageUrl())
                .placeholder(R.drawable.housing)
                .into(holder.serviceImage);
    }

    @Override
    public int getItemCount()
    {
        return serviceList.size();
    }

    public void setGridLayout(boolean isGrid)
    {
        this.isGridLayout = isGrid;
        notifyDataSetChanged();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder
    {
        ImageView serviceImage;
        TextView serviceName, servicePrice;

        public ServiceViewHolder(@NonNull View itemView)
        {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.service_image);
            serviceName = itemView.findViewById(R.id.service_name);
            servicePrice = itemView.findViewById(R.id.service_price);
        }
    }
}
