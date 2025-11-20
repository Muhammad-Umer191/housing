package com.example.housing.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.housing.R;
//import com.example.housing.activities.LanguageActivity;
//import com.example.housing.activities.PersonalInfoActivity;
//import com.example.housing.activities.LoginActivity;

public class ProfileFragment extends Fragment
{
    // Views
    private View menuPersonalInfo, menuLanguage, menuLogout;
    private ImageButton editProfileButton;

    private TextView userName, userEmail;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews(view);
        setupMenu();
        loadUserData();

        return view;
    }

    // -----------------------------
    // Initialize all views
    // -----------------------------
    private void initViews(View view)
    {
        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);

        editProfileButton = view.findViewById(R.id.edit_profile_button);

        menuPersonalInfo = view.findViewById(R.id.menu_personal_info);
        menuLanguage = view.findViewById(R.id.menu_language);
        menuLogout = view.findViewById(R.id.menu_logout);
    }

    private void setupMenu()
    {
        menuPersonalInfo.setOnClickListener(v ->
        {
//            Intent i = new Intent(requireContext(), PersonalInfoActivity.class);
//            startActivity(i);
        });

        menuLanguage.setOnClickListener(v ->
        {
//            Intent i = new Intent(requireContext(), LanguageActivity.class);
//            startActivity(i);
        });

        editProfileButton.setOnClickListener(v ->
        {
//            Intent i = new Intent(requireContext(), PersonalInfoActivity.class);
//            startActivity(i);
        });

        menuLogout.setOnClickListener(v ->
        {
//            Intent i = new Intent(requireContext(), LoginActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadUserData()
    {
        userName.setText("Tom Ryan");
        userEmail.setText("ryanlexx@gmail.com");
    }
}
