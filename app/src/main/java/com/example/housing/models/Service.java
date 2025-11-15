package com.example.housing.models;

public class Service
{
    private String name;
    private String price;
    private int image_res_id;

    public Service(String name, String price, int image_res_id)
    {
        this.name = name;
        this.price = price;
        this.image_res_id = image_res_id;
    }

    public String getName()
    {
        return name;
    }

    public String getPrice()
    {
        return price;
    }

    public int getImageResId()
    {
        return image_res_id;
    }
}
