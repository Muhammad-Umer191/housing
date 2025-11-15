package com.example.housing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.models.Offer;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder>
{
    private List<Offer> offers;

    public OfferAdapter(List<Offer> offers)
    {
        this.offers = offers;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer_card, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position)
    {
        Offer offer = offers.get(position);
        holder.title.setText(offer.getTitle());
        holder.image.setImageResource(offer.getImageRes());
    }

    @Override
    public int getItemCount()
    {
        return offers.size();
    }

    public void updateData(List<Offer> newOffers)
    {
        offers.clear();
        offers.addAll(newOffers);
        notifyDataSetChanged();
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView title;

        public OfferViewHolder(@NonNull View itemView)
        {
            super(itemView);
//            image = itemView.findViewById(R.id.offer_image);
//            title = itemView.findViewById(R.id.offer_title);
        }
    }
}
