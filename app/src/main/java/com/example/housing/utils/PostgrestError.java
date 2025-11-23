package com.example.housing.utils;

public class PostgrestError extends Throwable
{
    public PostgrestError(String message)
    {
        super(message);
    }
    public interface PostgrestCallback {
        void onSuccess();
        void onError(PostgrestError error);
    }
}