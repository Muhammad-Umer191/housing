package com.example.housing.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.housing.models.User;
import com.example.housing.network.RetrofitClient;
import com.example.housing.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends AndroidViewModel
{
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final PrefManager prefManager;

    public ProfileViewModel(@NonNull Application application)
    {
        super(application);
        prefManager = PrefManager.getInstance(application);
        loadUserFromDb();
    }

    public LiveData<User> getCurrentUser() { return currentUser; }

    private void loadUserFromDb()
    {
        String userId = prefManager.getUserId();
        if (userId == null)
        {
            currentUser.setValue(null);
            return;
        }

//        RetrofitClient.getSupabaseApi().getUserProfile(userId)
//                .enqueue(new Callback<User>()
//                {
//                    @Override
//                    public void onResponse(Call<User> call, Response<User> response)
//                    {
//                        if (response.isSuccessful() && response.body() != null)
//                            currentUser.setValue(response.body());
//                        else
//                            currentUser.setValue(null);
//                    }
//
//                    @Override
//                    public void onFailure(Call<User> call, Throwable t)
//                    {
//                        currentUser.setValue(null);
//                    }
//                });
    }
}
