package com.example.housing.network;

import com.example.housing.models.ServiceBooking;
import com.example.housing.models.ServiceBookingWithDetails;
import com.example.housing.models.ProfessionalBookingWithDetails;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface BookingApi {

    class BookingStatusUpdate {
        private String status;
        public BookingStatusUpdate(String status) { this.status = status; }
        public String getStatus() { return status; }
    }

    // -------------------- CRUD --------------------
    @PATCH("service_bookings")
    Call<Void> updateBookingStatus(@Header("Content-Type") String contentType,
                                   @Query("id") String filter,
                                   @Body BookingStatusUpdate update);

    @POST("service_bookings")
    Call<ServiceBooking> createBooking(@Body ServiceBooking booking);

    @GET("service_bookings")
    Call<List<ServiceBooking>> getCustomerBookings(@Query("customer_id") String filter);

    @GET("service_bookings")
    Call<List<ServiceBooking>> getProfessionalBookings(@Query("professional_id") String filter);

    // -------------------- RPC --------------------
    @GET("rpc/get_customer_bookings_with_details")
    Call<List<ServiceBookingWithDetails>> getCustomerBookingsWithDetails(@Query("cust_id") String customerId);

    @GET("rpc/get_professional_bookings_with_details")
    Call<List<ProfessionalBookingWithDetails>> getProfessionalBookingsWithDetails(@Query("prof_id") String professionalId);
}
