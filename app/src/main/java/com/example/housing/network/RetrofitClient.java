package com.example.housing.network;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient
{
    private static final String BASE_URL = "https://pxuboqabrgabizqrxdmb.supabase.co/";

    private static Retrofit retrofit = null;
    private static Context applicationContext;

    // Must be called once, ideally in your Application class
    public static void initialize(Context context)
    {
        applicationContext = context.getApplicationContext();
    }

    public static AuthService getAuthService()
    {
        // AuthService doesn't need the interceptor as its calls (login/signup) are unauthenticated.
        return getRetrofitInstance().create(AuthService.class);
    }

    // New method for services that require authentication (e.g., getting user data)
    public static <T> T createAuthenticatedService(Class<T> serviceClass)
    {
        // You'll need an OkHttpClient with the interceptor for authenticated calls
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(applicationContext))
                .build();

        // Create a new Retrofit instance specifically for authenticated calls
        Retrofit authenticatedRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return authenticatedRetrofit.create(serviceClass);
    }

    private static Retrofit getRetrofitInstance()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}