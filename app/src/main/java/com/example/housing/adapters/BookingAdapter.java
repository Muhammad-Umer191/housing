package com.example.housing.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
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

        holder.serviceTitle.setText(booking.getServiceTitle());
        holder.schedule.setText(booking.getSchedule());
        holder.provider.setText(booking.getProvider());
        holder.statusBadge.setText(booking.getStatusBadge());
        holder.image.setImageResource(booking.getImageResId());

        holder.callButton.setOnClickListener(v ->
        {
            String phone = booking.getPhoneNumber();

            if (phone != null && !phone.isEmpty())
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return bookingList.size(); }

    public static class BookingViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView serviceTitle, schedule, provider, statusBadge;
        AppCompatButton callButton;

        public BookingViewHolder(@NonNull View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.service);
            serviceTitle = itemView.findViewById(R.id.service_title);
            schedule = itemView.findViewById(R.id.text_schedule);
            provider = itemView.findViewById(R.id.text_provider);
            statusBadge = itemView.findViewById(R.id.status_badge);
            callButton = itemView.findViewById(R.id.call_button);
        }
    }
}
