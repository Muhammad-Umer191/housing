package com.example.housing.network;

import retrofit2.Retrofit;

public class SupabaseApi {

    private final UserApi userApi;
    private final ServiceApi serviceApi;
    private final BookingApi bookingApi;
    private final ReviewApi reviewApi;
    private final NotificationApi notificationApi;


    public SupabaseApi(UserApi userApi, ServiceApi serviceApi, BookingApi bookingApi,
                       ReviewApi reviewApi, NotificationApi notificationApi,
                       Retrofit retrofit) {
        this.userApi = userApi;
        this.serviceApi = serviceApi;
        this.bookingApi = bookingApi;
        this.reviewApi = reviewApi;
        this.notificationApi = notificationApi;

    }

    public UserApi user() { return userApi; }
    public ServiceApi service() { return serviceApi; }
    public BookingApi booking() { return bookingApi; }
    public ReviewApi review() { return reviewApi; }
    public NotificationApi notification() { return notificationApi; }

}
