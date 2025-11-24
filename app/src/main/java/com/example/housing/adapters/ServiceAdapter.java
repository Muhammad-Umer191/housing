package com.example.housing.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.housing.R;
import com.example.housing.activities.ServiceDetail;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private final JsonArray services;

    public ServiceAdapter(JsonArray services) {
        this.services = services;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_card, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        JsonObject service = services.get(position).getAsJsonObject();

        // Display only name and price
        String name = service.has("name") && !service.get("name").isJsonNull()
                ? service.get("name").getAsString()
                : "Service";
        holder.service_name.setText(name);

        double basePrice = service.has("base_price") && !service.get("base_price").isJsonNull()
                ? service.get("base_price").getAsDouble()
                : 0;
        holder.service_price.setText("$" + basePrice);

        // Display only image
        boolean imageSet = false;
        if (service.has("image_bitmap") && !service.get("image_bitmap").isJsonNull()) {
            String base64Image = service.get("image_bitmap").getAsString().trim();
            if (!base64Image.isEmpty()) {
                try {
                    byte[] decodedString = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.service_image.setImageBitmap(bitmap);
                    imageSet = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (!imageSet) {
            String imageUrl = service.has("image_url") && !service.get("image_url").isJsonNull()
                    ? service.get("image_url").getAsString().trim()
                    : null;

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.no_picture)
                        .error(R.drawable.no_picture)
                        .into(holder.service_image);
                imageSet = true;
            }
        }

        if (!imageSet) {
            holder.service_image.setImageResource(R.drawable.no_picture);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ServiceDetail.class);

            intent.putExtra("service_id", service.has("id") && !service.get("id").isJsonNull()
                    ? service.get("id").getAsString() : "");
            intent.putExtra("name", service.has("name") && !service.get("name").isJsonNull()
                    ? service.get("name").getAsString() : "");
            intent.putExtra("description", service.has("description") && !service.get("description").isJsonNull()
                    ? service.get("description").getAsString() : "");
            intent.putExtra("base_price", service.has("base_price") && !service.get("base_price").isJsonNull()
                    ? service.get("base_price").getAsDouble() : 0);
            intent.putExtra("image_url", service.has("image_url") && !service.get("image_url").isJsonNull()
                    ? service.get("image_url").getAsString() : "");
            intent.putExtra("avg_rating", service.has("avg_rating") && !service.get("avg_rating").isJsonNull()
                    ? service.get("avg_rating").getAsDouble() : 0);
            intent.putExtra("review_count", service.has("review_count") && !service.get("review_count").isJsonNull()
                    ? service.get("review_count").getAsInt() : 0);

            holder.itemView.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return services != null ? services.size() : 0;
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView service_image;
        TextView service_name;
        TextView service_price;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            service_image = itemView.findViewById(R.id.service_image);
            service_name = itemView.findViewById(R.id.service_name);
            service_price = itemView.findViewById(R.id.service_price);
        }
    }
}
