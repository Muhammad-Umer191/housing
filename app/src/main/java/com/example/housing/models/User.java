package com.example.housing.models;

import com.google.gson.annotations.SerializedName;

public class User
{

    @SerializedName("id") // Maps to auth.users(id). Primary Key (UUID).
    private String id;

    // BASIC INFO
    @SerializedName("email")
    private String email;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("profile_image")
    private String profileImage;

    // ROLE
    @SerializedName("role") // 'customer' or 'professional'
    private String role;

    // PROFESSIONAL FIELDS (Nullable)
    @SerializedName("service_category_id")
    private String serviceCategoryId; // Foreign key (UUID)

    @SerializedName("service_id")
    private String serviceId; // Foreign key (UUID)

    @SerializedName("experience_years")
    private Integer experienceYears; // Use Integer for nullable int

    @SerializedName("skills") // text[] array
    private String[] skills;

    @SerializedName("description")
    private String description;

    @SerializedName("cnic_number")
    private String cnicNumber;

    @SerializedName("verified")
    private Boolean verified;

    // LOCATION
    @SerializedName("city")
    private String city;

    @SerializedName("latitude") // double precision
    private Double latitude;

    @SerializedName("longitude") // double precision
    private Double longitude;

    @SerializedName("created_at")
    private String createdAt; // timestamptz

    @SerializedName("updated_at")
    private String updatedAt; // timestamptz

    public UserModel() {}

    // --- Getters and Setters (Partial set shown for brevity, all required methods should be included in a final file) ---
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public String getServiceCategoryId() { return serviceCategoryId; }
    public String[] getSkills() { return skills; }
    public Double getLatitude() { return latitude; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setCity(String city) { this.city = city; }
    public void setVerified(Boolean verified) { this.verified = verified; }
}
