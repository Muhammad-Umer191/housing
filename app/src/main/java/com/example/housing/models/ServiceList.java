package com.example.housing.models;

public class ServiceList
{
    private String service_id;
    private String name;
    private String description;
    private double base_price;
    private String image_url;
    private String category_id;
    private String category_name;
    private double avg_rating;
    private int review_count;

    public ServiceList() {}

    public ServiceList(
            String service_id,
            String name,
            String description,
            double base_price,
            String image_url,
            String category_id,
            String category_name,
            double avg_rating,
            int review_count
    )
    {
        this.service_id = service_id;
        this.name = name;
        this.description = description;
        this.base_price = base_price;
        this.image_url = image_url;
        this.category_id = category_id;
        this.category_name = category_name;
        this.avg_rating = avg_rating;
        this.review_count = review_count;
    }

    // Getters and Setters

    public String getService_id() { return service_id; }
    public void setService_id(String service_id) { this.service_id = service_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getBase_price() { return base_price; }
    public void setBase_price(double base_price) { this.base_price = base_price; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }

    public String getCategory_id() { return category_id; }
    public void setCategory_id(String category_id) { this.category_id = category_id; }

    public String getCategory_name() { return category_name; }
    public void setCategory_name(String category_name) { this.category_name = category_name; }

    public double getAvg_rating() { return avg_rating; }
    public void setAvg_rating(double avg_rating) { this.avg_rating = avg_rating; }

    public int getReview_count() { return review_count; }
    public void setReview_count(int review_count) { this.review_count = review_count; }
}
