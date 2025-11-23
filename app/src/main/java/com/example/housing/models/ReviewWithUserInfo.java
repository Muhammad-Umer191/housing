package com.example.housing.models;

import java.util.UUID;
import java.time.OffsetDateTime;

public class ReviewWithUserInfo
{

    private UUID review_id;
    private int rating;
    private String comment;
    private String customer_name;
    private String service_name;
    private OffsetDateTime created_at;

    // Getters & Setters
    public UUID getReview_id() { return review_id; }
    public void setReview_id(UUID review_id) { this.review_id = review_id; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getCustomer_name() { return customer_name; }
    public void setCustomer_name(String customer_name) { this.customer_name = customer_name; }

    public String getService_name() { return service_name; }
    public void setService_name(String service_name) { this.service_name = service_name; }

    public OffsetDateTime getCreated_at() { return created_at; }
    public void setCreated_at(OffsetDateTime created_at) { this.created_at = created_at; }
}
