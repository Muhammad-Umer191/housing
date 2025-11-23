package com.example.housing.models;

import java.util.UUID;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ServiceBookingWithDetails
{

    private UUID booking_id;
    private String service_name;
    private String professional_name;
    private OffsetDateTime scheduled_at;
    private String status;
    private BigDecimal price;

    // Getters & Setters
    public UUID getBooking_id() { return booking_id; }
    public void setBooking_id(UUID booking_id) { this.booking_id = booking_id; }

    public String getService_name() { return service_name; }
    public void setService_name(String service_name) { this.service_name = service_name; }

    public String getProfessional_name() { return professional_name; }
    public void setProfessional_name(String professional_name) { this.professional_name = professional_name; }

    public OffsetDateTime getScheduled_at() { return scheduled_at; }
    public void setScheduled_at(OffsetDateTime scheduled_at) { this.scheduled_at = scheduled_at; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
