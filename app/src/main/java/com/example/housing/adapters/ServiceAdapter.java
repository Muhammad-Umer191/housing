package com.example.housing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.models.Service;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>
{
    private final List<Service> serviceList;

    public ServiceAdapter(List<Service> serviceList)
    {
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_card, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position)
    {
        Service service = serviceList.get(position);
        holder.serviceName.setText(service.getName());
        holder.servicePrice.setText(service.getPrice());
        holder.serviceImage.setImageResource(service.getImageResId());
    }

    @Override
    public int getItemCount()
    {
        return serviceList.size();
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
