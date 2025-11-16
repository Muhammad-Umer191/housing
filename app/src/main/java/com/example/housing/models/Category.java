package com.example.housing.models;

public class Category
{
    private String name;
    private int icon_res_id;

    public Category(String name, int icon_res_id)
    {
        this.name = name;
        this.icon_res_id = icon_res_id;
    }

    public String getName()
    {
        return name;
    }

    public int getIconResId()
    {
        return icon_res_id;
    }
}
