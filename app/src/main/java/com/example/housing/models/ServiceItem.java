package com.example.housing.models;

public class ServiceItem
{
    private final int image;
    private final String rating;
    private final String title;
    private final String price;
    private final String priceValue;
    private final String id;

    public ServiceItem(int image, String rating, String title, String price, String priceValue, String id)
    {
        this.image = image;
        this.rating = rating;
        this.title = title;
        this.price = price;
        this.priceValue = priceValue;
        this.id = id;
    }

    public int getImage()
    {
        return image;
    }

    public String getRating()
    {
        return rating;
    }

    public double getRatingDouble() {
        try {
            return Double.parseDouble(rating);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public String getTitle()
    {
        return title;
    }

    public String getPrice()
    {
        return price;
    }

    public String getPriceValue()
    {
        return priceValue;
    }

    public double getPriceValueDouble() {
        try {
            return Double.parseDouble(priceValue.replaceAll("[^\\d.]", "")); // removes currency symbols
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public String getId() {
        return id;
    }
}
