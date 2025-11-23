package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.BuildConfig;
import com.example.housing.R;
import com.example.housing.models.User;
import com.example.housing.network.ResendApi;
import com.example.housing.network.ResendClient;
import com.example.housing.network.RetrofitClient;
import com.example.housing.network.UserApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTP extends AppCompatActivity {

    private EditText otpInput;
    private Button confirmButton;
    private TextView resendTimer;

    private String email;
    private String currentOtp;

    private static final long TIMER_DURATION_MS = 60 * 1000;
    private CountDownTimer countDownTimer;
    private boolean canResend = false;

    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        otpInput = findViewById(R.id.otp_input_layout);
        confirmButton = findViewById(R.id.confirm_button);
        resendTimer = findViewById(R.id.resend_timer);

        userApi = RetrofitClient.getClient().create(UserApi.class);

        email = getIntent().getStringExtra("email");

        fetchOtpFromDb();
        startResendTimer();

        confirmButton.setOnClickListener(v -> verifyOtp());

        resendTimer.setOnClickListener(v -> {
            if (canResend) {
                canResend = false;
                startResendTimer();
                resendOtp();
            } else {
                Toast.makeText(this, "Please wait until timer finishes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchOtpFromDb() {
        userApi.getUserByEmail(BuildConfig.SUPABASE_ANON_KEY, "Bearer " + BuildConfig.SUPABASE_ANON_KEY, email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    User user = response.body().get(0);
                    currentOtp = user.getOtp();
                }
                else
                {
                    Toast.makeText(VerifyOTP.this, "System down try again later and if persists contact admin", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(VerifyOTP.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyOtp() {
        String enteredOtp = otpInput.getText().toString().trim();

        if (enteredOtp.isEmpty()) {
            Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        if (enteredOtp.equals(currentOtp)) {
            Toast.makeText(this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ResetPassword.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        } else {
            otpInput.setError("Invalid OTP");
            otpInput.requestFocus();
        }
    }

    private void resendOtp() {

        // Generate new OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        currentOtp = otp;

        Map<String, Object> emailBody = new HashMap<>();
        emailBody.put("from", "onboarding@resend.dev");
        emailBody.put("to", email);
        emailBody.put("subject", "Your OTP Code");
        emailBody.put("html", "<p>Your OTP is: <b>" + otp + "</b></p>");

        ResendApi resendApi = ResendClient.getResendApi();
        resendApi.sendEmail("Bearer " + BuildConfig.RESEND_API_KEY,emailBody).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VerifyOTP.this, "OTP sent to " + email, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerifyOTP.this, "Failed to send OTP: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(VerifyOTP.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startResendTimer() {
        resendTimer.setText("60s");
        canResend = false;

        countDownTimer = new CountDownTimer(TIMER_DURATION_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendTimer.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                resendTimer.setText("Resend Code");
                canResend = true;
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
