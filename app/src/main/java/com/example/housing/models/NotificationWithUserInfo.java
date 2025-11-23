package com.example.housing.models;

import java.util.UUID;
import java.time.OffsetDateTime;

public class NotificationWithUserInfo
{

    private UUID notification_id;
    private String title;
    private String body;
    private boolean is_read;
    private String recipient_name;
    private OffsetDateTime created_at;

    // Getters & Setters
    public UUID getNotification_id() { return notification_id; }
    public void setNotification_id(UUID notification_id) { this.notification_id = notification_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public boolean isIs_read() { return is_read; }
    public void setIs_read(boolean is_read) { this.is_read = is_read; }

    public String getRecipient_name() { return recipient_name; }
    public void setRecipient_name(String recipient_name) { this.recipient_name = recipient_name; }

    public OffsetDateTime getCreated_at() { return created_at; }
    public void setCreated_at(OffsetDateTime created_at) { this.created_at = created_at; }
}
