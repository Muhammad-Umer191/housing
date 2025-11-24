package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.security.SecureRandom;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.BuildConfig;
import com.example.housing.R;
import com.example.housing.network.AuthApi;
import com.example.housing.network.ResendApi;
import com.example.housing.network.ResendClient;
import com.example.housing.network.RetrofitClient;
import com.example.housing.network.UserApi;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity
{
    private EditText emailInput;
    private Button confirmButton;

//
//    private static final String DIGITS = "0123456789";
//    private static final int OTP_LENGTH = 6;
//    private static SecureRandom random = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.email_input_field);
        confirmButton = findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(v -> handleForgotPassword());
    }

    private void handleForgotPassword() {




        String email = emailInput.getText().toString().trim();

        if (email.isEmpty()) {
            emailInput.setError("Enter your email");
            emailInput.requestFocus();
            return;
        }

        AuthApi authApi = RetrofitClient.getClient().create(AuthApi.class);

        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("redirect_to", BuildConfig.PASSWORD_RESET_REDIRECT_URI);

        authApi.resetPassword(BuildConfig.SUPABASE_ANON_KEY, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this,
                            "Password reset email sent to " + email, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(ForgotPassword.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ForgotPassword.this,
                            "Failed to send reset email: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ForgotPassword.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });



        // SELF EMAIL HANDLING



//        String email = emailInput.getText().toString().trim();
//
//        if (email.isEmpty()) {
//            emailInput.setError("Enter your email");
//            emailInput.requestFocus();
//            return;
//        }
//
//        UserApi userApi = RetrofitClient.getClient().create(UserApi.class);
//        String filter = "eq." + emailInput;
//
//        userApi.getUserByEmail(BuildConfig.SUPABASE_ANON_KEY, "Bearer " + BuildConfig.SUPABASE_ANON_KEY, filter).enqueue(new Callback<List<User>>() {
//            @Override
//            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
//                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
//
//
//                    StringBuilder otp = new StringBuilder();
//                    for (int i = 0; i < OTP_LENGTH; i++) {
//                        int index = random.nextInt(DIGITS.length());
//                        otp.append(DIGITS.charAt(index));
//                    }
//
//                    Map<String, Object> emailBody = new HashMap<>();
//                    emailBody.put("from", "onboarding@resend.dev");
//                    emailBody.put("to", Collections.singletonList(emailInput));
//                    emailBody.put("subject", "Your OTP Code");
//                    emailBody.put("html", "<p>Your OTP is: <b>" + otp + "</b></p>");
//
//                    ResendApi resendApi = ResendClient.getResendApi();
//                    resendApi.sendEmail("Bearer " + BuildConfig.RESEND_API_KEY,emailBody).enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response) {
//                            if (response.isSuccessful()) {
//                                Toast.makeText(ForgotPassword.this, "OTP sent to " + emailInput, Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(ForgotPassword.this, "Failed to send OTP: " + response.message(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            Toast.makeText(ForgotPassword.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                    Map<String, Object> body = new HashMap<>();
//                    body.put("otp", otp);
//                    UserApi api = RetrofitClient.getClient().create(UserApi.class);
//                    api.updateUserByEmail(BuildConfig.SUPABASE_ANON_KEY, "Bearer " + BuildConfig.SUPABASE_ANON_KEY, email, body).enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response) {
//                            if (!response.isSuccessful()) {
//                                System.out.println("OTP updated successfully for: " + email);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            t.printStackTrace();
//                        }
//                    });
//
//                    Intent intent = new Intent(ForgotPassword.this, VerifyOTP.class);
//                    intent.putExtra("email", email);
//                    startActivity(intent);
//                }
//                else
//                {
//                    Toast.makeText(ForgotPassword.this, "Email not registered", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<User>> call, Throwable t) {
//                Toast.makeText(ForgotPassword.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }


}
