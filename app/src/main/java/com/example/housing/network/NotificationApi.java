package com.example.housing.network;

import com.example.housing.models.Notification;
import com.example.housing.models.NotificationWithUserInfo;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PATCH;
import retrofit2.http.Body;
import retrofit2.http.Query;
import retrofit2.http.Headers;
import retrofit2.http.Header;

public interface NotificationApi {

    class ReadStatusUpdate {
        private boolean is_read = true;
        public ReadStatusUpdate() {}
        public boolean isIs_read() { return is_read; }
        public void setIs_read(boolean is_read) { this.is_read = is_read; }
    }

    // -------------------- CRUD --------------------
    @Headers({"Content-Type: application/json"})
    @POST("notifications")
    Call<Notification> createNotification(@Body Notification notification);

    @GET("notifications")
    Call<List<Notification>> getUserNotifications(@Query("user_id") String filter,
                                                  @Query("order") String orderBy);

    @PATCH("notifications")
    Call<Void> markNotificationAsRead(@Header("Content-Type") String contentType,
                                      @Query("id") String filter,
                                      @Body ReadStatusUpdate payload);

    // -------------------- RPC --------------------
    @GET("rpc/get_user_notifications_with_details")
    Call<List<NotificationWithUserInfo>> getUserNotificationsWithDetails(@Query("user_id") String userId);
}
