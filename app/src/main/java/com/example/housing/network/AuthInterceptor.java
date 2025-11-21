package com.example.housing.network;

import android.content.Context;
import com.example.housing.utils.PrefManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor
{
    private final PrefManager prefManager;
    public AuthInterceptor(Context context)
    {
        this.prefManager = PrefManager.getInstance(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request originalRequest = chain.request();
        String accessToken = prefManager.getAccessToken();

        // Check if a token exists and if the request is not already authenticated (e.g., signup)
        if (accessToken != null)
        {
            // Build a new request with the Authorization header
            Request authenticatedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    // PostgREST also often requires the Authorization header to be duplicated as the 'apikey' header
                    .header("apikey", AuthService.ANON_KEY)
                    .build();

            return chain.proceed(authenticatedRequest);
        }

        // If no token, proceed with the original request (for public endpoints like login/signup)
        return chain.proceed(originalRequest);
    }
}