package com.example.housing.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.housing.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookingBottomSheet extends BottomSheetDialogFragment
{
    private TextView textSelectedDate, textSelectedTime, textTotalValue;
    private ImageButton buttonClose;
    private Button buttonContinue;

    private Calendar selectedDateTime;

    public BookingBottomSheet()
    {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_date_time, container, false);

        textSelectedDate = view.findViewById(R.id.text_selected_date);
        textSelectedTime = view.findViewById(R.id.text_selected_time);
        textTotalValue = view.findViewById(R.id.text_total_value);
        buttonClose = view.findViewById(R.id.button_close);
        buttonContinue = view.findViewById(R.id.button_continue);

        selectedDateTime = Calendar.getInstance();

        // Close button
        buttonClose.setOnClickListener(v -> dismiss());

        // Date selection
        view.findViewById(R.id.card_date_select).setOnClickListener(v -> showDatePicker());

        // Time selection
        view.findViewById(R.id.card_time_select).setOnClickListener(v -> showTimePicker());

        // Continue button click
        buttonContinue.setOnClickListener(v ->
        {
            // You can pass selectedDateTime back to ServiceDetail or proceed to booking confirmation
            dismiss();
        });

        return view;
    }

    private void showDatePicker()
    {
        int year = selectedDateTime.get(Calendar.YEAR);
        int month = selectedDateTime.get(Calendar.MONTH);
        int day = selectedDateTime.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, month1, dayOfMonth) ->
                {

                    selectedDateTime.set(Calendar.YEAR, year1);
                    selectedDateTime.set(Calendar.MONTH, month1);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    textSelectedDate.setText(sdf.format(selectedDateTime.getTime()));
                }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker()
    {
        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute1) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute1);

                    // Format time as "HH:mm"
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    textSelectedTime.setText(sdf.format(selectedDateTime.getTime()));
                }, hour, minute, false);

        timePickerDialog.show();
    }

    // Optional: allow the activity to get the selected date/time
    public Calendar getSelectedDateTime()
    {
        return selectedDateTime;
    }
}
