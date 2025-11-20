package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.R;
import com.example.housing.fragments.BookingBottomSheet;

public class ServiceDetail extends AppCompatActivity
{
    private TextView text_rating, service_title, text_total_value;
    private LinearLayout property_home, property_office, property_villa;

    private LinearLayout units_counter, bedrooms_counter;
    private TextView units_value, bedrooms_value;

    private EditText description_input;

    private Button button_book_now, button_save_draft;

    private String serviceId;
    private String title;
    private double basePrice;
    private double rating;
    private String category;

    private int units = 1;
    private int bedrooms = 1;
    private String selectedProperty = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        getIntentData();

        initViews();
        setupPropertySelection();
        setupUnitCounter();
        setupBedroomCounter();
        updateTotalBill();

        button_book_now.setOnClickListener(v ->
        {
            // Validation
            if (selectedProperty == null || selectedProperty.isEmpty()) {
                Toast.makeText(this, "Please select a property type", Toast.LENGTH_SHORT).show();
                return;
            }

            if (units <= 0) {
                Toast.makeText(this, "Please select at least 1 unit", Toast.LENGTH_SHORT).show();
                return;
            }

            if (bedrooms <= 0) {
                Toast.makeText(this, "Please select at least 1 bedroom", Toast.LENGTH_SHORT).show();
                return;
            }

            BookingBottomSheet bottomSheet = new BookingBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "bookingBottomSheet");
        });

        button_save_draft.setOnClickListener(view ->
        {
            // TODO: 11/15/25 Add draft logic
            Toast.makeText(this, "Draft saved", Toast.LENGTH_SHORT).show();
        });
    }

    private void getIntentData()
    {
        Intent i = getIntent();

        serviceId = i.getStringExtra("service_id");
        title = i.getStringExtra("title");
        basePrice = i.getDoubleExtra("price", 0.0);
        rating = i.getDoubleExtra("rating", 0.0);
        category = i.getStringExtra("category");
    }

    private void initViews()
    {
        text_rating = findViewById(R.id.text_rating);
        service_title = findViewById(R.id.service_title);
        text_total_value = findViewById(R.id.text_total_value);

        property_home = findViewById(R.id.property_home);
        property_office = findViewById(R.id.property_office);
        property_villa = findViewById(R.id.property_villa);

        ((ImageView) property_home.findViewById(R.id.property_icon)).setImageResource(R.drawable.home);
        ((TextView) property_home.findViewById(R.id.property_label)).setText(R.string.home);

        ((ImageView) property_office.findViewById(R.id.property_icon)).setImageResource(R.drawable.office);
        ((TextView) property_office.findViewById(R.id.property_label)).setText(R.string.office);

        ((ImageView) property_villa.findViewById(R.id.property_icon)).setImageResource(R.drawable.villa);
        ((TextView) property_villa.findViewById(R.id.property_label)).setText(R.string.villa);

        units_counter = findViewById(R.id.units_counter);
        bedrooms_counter = findViewById(R.id.bedrooms_counter);

        units_value = units_counter.findViewById(R.id.text_value);
        bedrooms_value = bedrooms_counter.findViewById(R.id.text_value);

        description_input = findViewById(R.id.description_input);

        button_book_now = findViewById(R.id.button_book_now);
        button_save_draft = findViewById(R.id.button_save_draft);

        text_rating.setText(String.valueOf(rating));
        service_title.setText(title);
    }

    private void setupPropertySelection()
    {
        property_home.setOnClickListener(view ->
        {
            selectedProperty = "Home";
            highlightSelected(property_home, property_office, property_villa);
        });

        property_office.setOnClickListener(view ->
        {
            selectedProperty = "Office";
            highlightSelected(property_office, property_home, property_villa);
        });

        property_villa.setOnClickListener(view ->
        {
            selectedProperty = "Villa";
            highlightSelected(property_villa, property_home, property_office);
        });
    }

    private void highlightSelected(View selected, View... others)
    {
        selected.setBackgroundResource(R.drawable.property_selected_border);

        for (View v : others)
        {
            v.setBackgroundResource(R.drawable.property_unselected_border);
        }
    }

    private void setupUnitCounter()
    {
        ImageButton dec = units_counter.findViewById(R.id.button_decrement);
        ImageButton inc = units_counter.findViewById(R.id.button_increment);

        dec.setOnClickListener(view ->
        {
            if (units > 1)
            {
                units--;
                units_value.setText(String.valueOf(units));
                updateTotalBill();
            }
        });

        inc.setOnClickListener(view ->
        {
            units++;
            units_value.setText(String.valueOf(units));
            updateTotalBill();
        });
    }

    private void setupBedroomCounter()
    {
        ImageButton dec = bedrooms_counter.findViewById(R.id.button_decrement);
        ImageButton inc = bedrooms_counter.findViewById(R.id.button_increment);

        dec.setOnClickListener(view ->
        {
            if (bedrooms > 1)
            {
                bedrooms--;
                bedrooms_value.setText(String.valueOf(bedrooms));
                updateTotalBill();
            }
        });

        inc.setOnClickListener(view ->
        {
            bedrooms++;
            bedrooms_value.setText(String.valueOf(bedrooms));
            updateTotalBill();
        });
    }

    private void updateTotalBill()
    {
        text_total_value.setText("USD " + calculateTotal());
    }

    private double calculateTotal()
    {
        return basePrice * units * bedrooms;
    }
}
