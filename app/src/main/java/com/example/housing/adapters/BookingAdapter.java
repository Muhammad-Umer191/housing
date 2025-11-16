package com.example.housing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.models.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder>
{
    private final List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList)
    {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_card, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position)
    {
        Booking booking = bookingList.get(position);
        holder.service_title.setText(booking.getServiceTitle());
        holder.text_schedule.setText(booking.getSchedule());
        holder.text_provider.setText(booking.getProvider());
        holder.status_badge.setText(booking.getStatusBadge());
        holder.service.setImageResource(booking.getImageResId()); // set image
    }

    @Override
    public int getItemCount()
    {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder
    {
        ImageView service;
        TextView service_title;
        TextView text_schedule;
        TextView text_provider;
        TextView status_badge;

        public BookingViewHolder(@NonNull View itemView)
        {
            super(itemView);
            service = itemView.findViewById(R.id.service);
            service_title = itemView.findViewById(R.id.service_title);
            text_schedule = itemView.findViewById(R.id.text_schedule);
            text_provider = itemView.findViewById(R.id.text_provider);
            status_badge = itemView.findViewById(R.id.status_badge);
        }
    }
}
