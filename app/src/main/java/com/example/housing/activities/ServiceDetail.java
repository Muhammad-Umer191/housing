package com.example.housing.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.housing.R;

public class ServiceDetail extends AppCompatActivity {

    private TextView serviceTitle, textRating;
    private ImageView serviceImage;
    private EditText descriptionInput;
    private TextView textTotalValue;
    private Button buttonBookNow, buttonSaveDraft;
    String serviceId;
    String name;
    String description;
    double basePrice;
    double avgRating;
    int reviewCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        // Initialize views
        serviceTitle = findViewById(R.id.service_title);
        textRating = findViewById(R.id.text_rating);
        serviceImage = findViewById(R.id.service_image);
        descriptionInput = findViewById(R.id.description_input);
        textTotalValue = findViewById(R.id.text_total_value);
        buttonBookNow = findViewById(R.id.button_book_now);
        buttonSaveDraft = findViewById(R.id.button_save_draft);

        serviceId = getIntent().getStringExtra("service_id");
        if(serviceId == null) serviceId = "";

        name = getIntent().getStringExtra("name");
        if(name == null) name = "";

        description = getIntent().getStringExtra("description");
        if(description == null) description = "";

        basePrice = getIntent().getDoubleExtra("base_price", 0);
        String imageUrl = getIntent().getStringExtra("image_url");
        if(imageUrl == null) imageUrl = "";

        avgRating = getIntent().getDoubleExtra("avg_rating", 0);
        reviewCount = getIntent().getIntExtra("review_count", 0);


        // Set values to views
        serviceTitle.setText(name);
        textRating.setText(String.valueOf(avgRating));

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.color.gray_500)
                    .into(serviceImage);
        } else {
            serviceImage.setBackgroundResource(R.color.gray_500);
        }

        descriptionInput.setText(description);
        textTotalValue.setText("USD " + basePrice);

        // Handle buttons
        buttonBookNow.setOnClickListener(v -> {
            // Implement booking logic here
            Toast.makeText(ServiceDetail.this, "Booked " + name, Toast.LENGTH_SHORT).show();
        });

        buttonSaveDraft.setOnClickListener(v -> {
            String userDescription = descriptionInput.getText().toString().trim();
            Toast.makeText(ServiceDetail.this, "Draft saved for " + name, Toast.LENGTH_SHORT).show();
        });
    }
}
