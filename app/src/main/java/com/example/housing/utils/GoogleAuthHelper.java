package com.example.housing.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.example.housing.R;
import com.example.housing.network.AuthService;
import com.example.housing.network.IdTokenRequest;
import com.example.housing.network.LoginResponse;
import com.example.housing.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleAuthHelper {

    public static final int RC_GOOGLE_SIGN_IN = 9001;
    private final GoogleSignInClient mGoogleSignInClient;
    private final Context context;
    private final GoogleAuthCallback callback;

    // Interface to handle the result back in the Activity
    public interface GoogleAuthCallback {
        void onGoogleSignInSuccess(LoginResponse session);
        void onGoogleSignInFailure(String errorMessage);
    }

    public GoogleAuthHelper(Context context, GoogleAuthCallback callback) {
        this.context = context;
        this.callback = callback;

        // Use the context to get the Web Client ID from resources
        String webClientId = context.getString(R.string.default_web_client_id);

        // 1. Configure Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId) // IMPORTANT: Requests token for your Supabase server
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    /** Initiates the Google Sign-In flow */
    public void startSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        ((Activity) context).startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    /** Handles the result from the Google Sign-In Activity */
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String idToken = account.getIdToken();

                if (idToken != null) {
                    // 2. Token received! Start the Supabase exchange
                    supabaseSignInWithGoogle(idToken);
                } else {
                    callback.onGoogleSignInFailure("Google ID Token was null.");
                }
            } catch (ApiException e) {
                String error = "Google Sign In Failed: " + e.getStatusCode();
                Log.w("GoogleAuthHelper", error, e);
                callback.onGoogleSignInFailure(error);
            }
        }
    }

    /** Sends the ID Token to Supabase via Retrofit */
    private void supabaseSignInWithGoogle(String googleIdToken) {
        IdTokenRequest request = new IdTokenRequest(googleIdToken);
        AuthService service = RetrofitClient.getAuthService();

        service.signInWithGoogle(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 3. Exchange Success! Pass the full Supabase session back
                    callback.onGoogleSignInSuccess(response.body());
                } else {
                    String error = "Supabase Exchange Failed. Code: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            error += ", Body: " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e("GoogleAuthHelper", "Error parsing error body", e);
                    }
                    Log.e("GoogleAuthHelper", error);
                    callback.onGoogleSignInFailure(error);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String error = "Network Error during Supabase Exchange: " + t.getMessage();
                Log.e("GoogleAuthHelper", error, t);
                callback.onGoogleSignInFailure(error);
            }
        });
    }
}
