package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.R;
import com.example.housing.fragments.DashboardFragment;

public class VerifyOTP extends AppCompatActivity
{
    private EditText otpInput;
    private Button confirmButton;
    private TextView resendTimer;

    private final String CORRECT_OTP = "123456";

    private static final long TIMER_DURATION_MS = 60 * 1000;

    private CountDownTimer countDownTimer;
    private boolean canResend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        otpInput = findViewById(R.id.otp_input_layout);
        confirmButton = findViewById(R.id.confirm_button);
        resendTimer = findViewById(R.id.resend_timer);

        startResendTimer();

        confirmButton.setOnClickListener(v ->
        {
            String enteredOtp = otpInput.getText().toString().trim();

            if (enteredOtp.isEmpty())
            {
                Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            if (enteredOtp.equals(CORRECT_OTP))
            {
                Toast.makeText(this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DashboardFragment.class);
                startActivity(intent);
                finish();
            }
            else
            {
                otpInput.setError("Invalid OTP");
                otpInput.requestFocus();
            }
        });

        resendTimer.setOnClickListener(v ->
        {
            if (canResend)
            {
                Toast.makeText(this, "Resending OTP...", Toast.LENGTH_SHORT).show();
                canResend = false;
                startResendTimer();
                // TODO: Call backend here to resend OTP if needed
            }
            else
            {
                Toast.makeText(this, "Please wait until timer finishes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startResendTimer()
    {
        resendTimer.setText("60s");
        canResend = false;

        countDownTimer = new CountDownTimer(TIMER_DURATION_MS, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                resendTimer.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish()
            {
                resendTimer.setText("Resend Code");
                canResend = true;
            }
        }.start();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (countDownTimer != null)
        {
            countDownTimer.cancel();
        }
    }
}
