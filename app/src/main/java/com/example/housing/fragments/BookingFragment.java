package com.example.housing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.housing.R;
import com.example.housing.adapters.BookingAdapter;
import com.example.housing.models.ServiceBooking;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment
{
    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private List<ServiceBooking> currentBookings = new ArrayList<>();
    private Button btnFilterRecent;
//    private BookingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_bookings);
        btnFilterRecent = view.findViewById(R.id.filter_recent);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingAdapter(currentBookings);
        recyclerView.setAdapter(adapter);

//        viewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        observeBookings();

        btnFilterRecent.setOnClickListener(v -> showSortMenu(v));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
//                String filterStatus = "All";
//                if (tab.getPosition() == 0) filterStatus = "Confirmed";
//                else if (tab.getPosition() == 1) filterStatus = "Pending";
//                viewModel.filterBookings(filterStatus);
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private void observeBookings()
    {
//        viewModel.getBookings().observe(getViewLifecycleOwner(), bookings -> {
//            currentBookings.clear();
//            currentBookings.addAll(bookings);
//            adapter.notifyDataSetChanged();
//        });
    }

    private void showSortMenu(View anchor)
    {
        PopupMenu popup = new PopupMenu(getContext(), anchor);
        popup.getMenuInflater().inflate(R.menu.sort_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item ->
        {
//            int id = item.getItemId();
//            if (id == R.id.sort_time_asc) viewModel.sortBookingsByTime(true);
//            else if (id == R.id.sort_time_desc) viewModel.sortBookingsByTime(false);
//            else if (id == R.id.sort_name_asc) viewModel.sortBookingsByName(true);
//            else if (id == R.id.sort_name_desc) viewModel.sortBookingsByName(false);
//            else return false;

            return true;
        });

        popup.show();
    }
}
