package com.example.housing.models;

import com.google.gson.annotations.SerializedName;

public class Category
{

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("icon_url")
    private String iconUrl;

    @SerializedName("created_at")
    private String createdAt;

    public Category() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
