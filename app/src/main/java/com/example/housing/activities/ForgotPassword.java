package com.example.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housing.R;

public class ForgotPassword extends AppCompatActivity
{

    private EditText emailInput;
    private Button confirmButton;
//    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.email_input_field);
        confirmButton = findViewById(R.id.confirm_button);

//        mAuth = FirebaseAuth.getInstance();

        confirmButton.setOnClickListener(v ->
        {
            String email = emailInput.getText().toString().trim();

            if (email.isEmpty())
            {
                emailInput.setError("Enter your email");
                emailInput.requestFocus();
                return;
            }

//            mAuth.sendPasswordResetEmail(email)
//                    .addOnCompleteListener(task ->
//                    {
//                        if (task.isSuccessful())
//                        {
//                            Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
//                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });
            Intent intent = new Intent(this, VerifyOTP.class);
            startActivity(intent);
        });
    }
}
