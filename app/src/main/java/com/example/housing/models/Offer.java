package com.example.housing.models;

public class Offer
{
    private String title;
    private int imageRes;

    public Offer(String title, int imageRes)
    {
        this.title = title;
        this.imageRes = imageRes;
    }

    public String getTitle()
    {
        return title;
    }

    public int getImageRes()
    {
        return imageRes;
    }
}