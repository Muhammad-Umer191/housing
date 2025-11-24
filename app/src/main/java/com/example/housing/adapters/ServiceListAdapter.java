package com.example.housing.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.housing.R;
import com.example.housing.activities.ServiceDetail;
import com.example.housing.models.ServiceMutable;

import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceViewHolder> {

    private Context context;
    private List<ServiceMutable> services;
    private boolean isGridLayout;

    public ServiceListAdapter(Context context, List<ServiceMutable> services, boolean isGridLayout) {
        this.context = context;
        this.services = services;
        this.isGridLayout = isGridLayout;
    }

    public void setGridLayout(boolean isGridLayout) {
        this.isGridLayout = isGridLayout;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isGridLayout ? R.layout.item_service_grid_card : R.layout.item_service_list_card;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceMutable service = services.get(position);

        holder.serviceTitle.setText(service.getName());
        holder.priceValue.setText("$" + service.getBasePrice());
        holder.rating.setText(String.valueOf(service.getAvgRating()));

        if (service.getImageUrl() != null && !service.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(service.getImageUrl())
                    .centerCrop()
                    .placeholder(R.color.gray_500)
                    .into(holder.serviceImage);
        } else {
            holder.serviceImage.setBackgroundResource(R.color.gray_500);
        }

        // Handle more options click
        holder.moreOptions.setOnClickListener(v ->
                Toast.makeText(context, "More options clicked for " + service.getName(), Toast.LENGTH_SHORT).show()
        );

        // Handle item click to open ServiceDetail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ServiceDetail.class);
            intent.putExtra("service_id", service.getServiceId());
            intent.putExtra("name", service.getName());
            intent.putExtra("description", service.getDescription());
            intent.putExtra("base_price", service.getBasePrice());
            intent.putExtra("image_url", service.getImageUrl());
            intent.putExtra("avg_rating", service.getAvgRating());
            intent.putExtra("review_count", service.getReviewCount());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {

        TextView serviceTitle, priceValue, rating;
        ImageButton moreOptions;
        ImageView serviceImage;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceTitle = itemView.findViewById(R.id.service_title);
            priceValue = itemView.findViewById(R.id.price_value);
            rating = itemView.findViewById(R.id.rating);
            moreOptions = itemView.findViewById(R.id.more_options_button);
            serviceImage = itemView.findViewById(R.id.service_image);
        }
    }
}
