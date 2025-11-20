package com.example.housing.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.housing.R;
import com.example.housing.models.Booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookingViewModel extends ViewModel
{
    private final MutableLiveData<List<Booking>> bookingsLiveData = new MutableLiveData<>();
    private final List<Booking> bookingList = new ArrayList<>();

    public BookingViewModel()
    {
        loadSampleBookings();
    }

    public LiveData<List<Booking>> getBookings() { return bookingsLiveData; }

    private void loadSampleBookings()
    {
        bookingList.clear();
        // Now including phone numbers in the sample data
        bookingList.add(new Booking("AC Installation", "8:00-9:00 AM, 09 Dec", "Westinghouse", "Confirmed", R.drawable.ac_repair, "03074306600"));
        bookingList.add(new Booking("Plumbing Repair", "10:00-11:00 AM, 10 Dec", "ABC Services", "Pending", R.drawable.plumbing, "03074306600"));
        bookingList.add(new Booking("House Cleaning", "2:00-3:00 PM, 11 Dec", "CleanPro", "Confirmed", R.drawable.cleaning, "03074306600"));

        bookingsLiveData.setValue(new ArrayList<>(bookingList));
    }

    public void filterBookings(String filterStatus)
    {
        List<Booking> filtered = new ArrayList<>();
        for (Booking booking : bookingList)
        {
            if (filterStatus.equals("All") || booking.getStatusBadge().equalsIgnoreCase(filterStatus))
                filtered.add(booking);
        }
        bookingsLiveData.setValue(filtered);
    }

    public void sortBookingsByTime(boolean ascending)
    {
        List<Booking> currentList = bookingsLiveData.getValue();
        if (currentList == null) return;

        Collections.sort(currentList, (b1, b2) ->
                ascending ? Long.compare(b1.getTimestamp(), b2.getTimestamp())
                        : Long.compare(b2.getTimestamp(), b1.getTimestamp())
        );

        bookingsLiveData.setValue(currentList);
    }

    public void sortBookingsByName(boolean ascending)
    {
        List<Booking> currentList = bookingsLiveData.getValue();
        if (currentList == null) return;
        Collections.sort(currentList, (b1, b2) -> ascending ? b1.getServiceTitle().compareToIgnoreCase(b2.getServiceTitle())
                : b2.getServiceTitle().compareToIgnoreCase(b1.getServiceTitle()));
        bookingsLiveData.setValue(currentList);
    }
}
