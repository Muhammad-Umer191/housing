package com.example.housing.utils;

import android.app.Activity;

// import androidx.credentials.CredentialManager;
// import com.google.firebase.auth.FirebaseAuth;
// import com.google.firebase.auth.FirebaseUser;
// import com.google.firebase.auth.AuthCredential;
// import com.google.firebase.auth.GoogleAuthProvider;

public class FireBaseAuthManager
{

    private final Activity activity;
    private final PrefManager prefManager;
    // private final FirebaseAuth mAuth;
    // private final CredentialManager credentialManager;

    public FireBaseAuthManager(Activity activity, PrefManager prefManager) {
        this.activity = activity;
        this.prefManager = prefManager;
        // this.mAuth = FirebaseAuth.getInstance();
        // this.credentialManager = CredentialManager.create(activity);
    }

    public void startGoogleSignIn(AuthCallback callback) {
        // Logic will be added later
        callback.onFailure("Google Sign-In not implemented yet");
    }

    // private void firebaseAuthWithGoogle(String idToken, AuthCallback callback) {
    //     // Logic will be implemented later
    // }

    public interface AuthCallback {
        void onSuccess(Object user);
        void onFailure(String message);
    }
}
