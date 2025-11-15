package com.example.housing.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.adapters.BookingAdapter;
import com.example.housing.models.Booking;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment
{
    private RecyclerView recyclerView;

    private final MutableLiveData<List<Booking>> bookingsLiveData = new MutableLiveData<>();

    private List<Booking> bookingList;
    private BookingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_bookings);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(bookingList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadSampleBookings();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                filterBookings(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });

        return view;
    }

    private void loadSampleBookings()
    {
        bookingList.clear();
        bookingList.add(new Booking("AC Installation", "8:00-9:00 AM, 09 Dec", "Westinghouse", "Confirmed", R.drawable.ac_repair));
        bookingList.add(new Booking("Plumbing Repair", "10:00-11:00 AM, 10 Dec", "ABC Services", "Pending", R.drawable.plumbing));
        bookingList.add(new Booking("House Cleaning", "2:00-3:00 PM, 11 Dec", "CleanPro", "Confirmed", R.drawable.cleaning));

        bookingsLiveData.setValue(new ArrayList<>(bookingList));
    }


    private void filterBookings(int tabPosition)
    {

        List<Booking> filteredList = new ArrayList<>();
        for (Booking booking : bookingList)
        {
            if (tabPosition == 0 && booking.getStatusBadge().equals("Confirmed")) filteredList.add(booking);
            else if (tabPosition == 1 && booking.getStatusBadge().equals("Pending")) filteredList.add(booking);
            else if (tabPosition == 2) filteredList.add(booking);
        }
        adapter = new BookingAdapter(filteredList);
        recyclerView.setAdapter(adapter);
    }
}
