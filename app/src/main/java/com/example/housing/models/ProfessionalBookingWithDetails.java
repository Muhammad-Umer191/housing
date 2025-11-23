package com.example.housing.models;

import java.util.UUID;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ProfessionalBookingWithDetails
{

    private UUID booking_id;
    private String service_name;
    private String customer_name;
    private OffsetDateTime scheduled_at;
    private String status;
    private BigDecimal price;

    // Getters & Setters
    public UUID getBooking_id() { return booking_id; }
    public void setBooking_id(UUID booking_id) { this.booking_id = booking_id; }

    public String getService_name() { return service_name; }
    public void setService_name(String service_name) { this.service_name = service_name; }

    public String getCustomer_name() { return customer_name; }
    public void setCustomer_name(String customer_name) { this.customer_name = customer_name; }

    public OffsetDateTime getScheduled_at() { return scheduled_at; }
    public void setScheduled_at(OffsetDateTime scheduled_at) { this.scheduled_at = scheduled_at; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
