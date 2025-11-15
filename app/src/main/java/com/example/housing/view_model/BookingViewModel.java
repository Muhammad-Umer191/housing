package com.example.housing.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.housing.R;
import com.example.housing.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingViewModel extends ViewModel
{
    private final MutableLiveData<List<Booking>> bookingsLiveData = new MutableLiveData<>();
    private final List<Booking> bookingList = new ArrayList<>();

    public BookingViewModel()
    {
        loadSampleBookings();
    }

    public LiveData<List<Booking>> getBookings()
    {
        return bookingsLiveData;
    }

    private void loadSampleBookings()
    {
        bookingList.clear();
        bookingList.add(new Booking("AC Installation", "8:00-9:00 AM, 09 Dec", "Westinghouse", "Confirmed", R.drawable.ac_repair));
        bookingList.add(new Booking("Plumbing Repair", "10:00-11:00 AM, 10 Dec", "ABC Services", "Pending", R.drawable.plumbing));
        bookingList.add(new Booking("House Cleaning", "2:00-3:00 PM, 11 Dec", "CleanPro", "Confirmed", R.drawable.cleaning));

        bookingsLiveData.setValue(new ArrayList<>(bookingList));
    }


    public void filterBookings(String filterStatus)
    {
        List<Booking> filtered = new ArrayList<>();
        for (Booking booking : bookingList)
        {
            if (filterStatus.equals("All") || booking.getStatusBadge().equalsIgnoreCase(filterStatus))
            {
                filtered.add(booking);
            }
        }
        bookingsLiveData.setValue(filtered);
    }
}
