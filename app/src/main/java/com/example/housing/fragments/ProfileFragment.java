package com.example.housing.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.housing.R;
import com.example.housing.activities.LanguageActivity;
import com.example.housing.activities.PersonalInfo;
import com.example.housing.activities.LoginSignup;
import com.example.housing.utils.PrefManager;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment
{
    // Views
    private View menuPersonalInfo, menuLanguage, menuLogout;
    private ImageButton editProfileButton;
    private TextView userName, userEmail;
    private TextView menuLanguageValue;

    // Map to quickly look up language display name from locale code (must match LanguageActivity)
    private final Map<String, String> languageCodeToNameMap = new HashMap<>();

    private PrefManager prefManager;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (getContext() != null) {
            prefManager = PrefManager.getInstance(getContext());
        }

        setupLanguageMap();
        initViews(view);
        setupMenuContent(view);
        setupMenu();
        loadUserData();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadUserData();
        updateLanguageDisplay(); // Update language display on resume
    }

    /**
     * Sets up the map to convert locale codes back to human-readable names for the menu display.
     * Must match the list in LanguageActivity.java
     */
    private void setupLanguageMap() {
        languageCodeToNameMap.put("en", "English");
        languageCodeToNameMap.put("zh", "Mandarin Chinese (中文)");
        languageCodeToNameMap.put("es", "Spanish (Español)");
        languageCodeToNameMap.put("hi", "Hindi (हिन्दी)");
        languageCodeToNameMap.put("ar", "Arabic (العربية)");
        languageCodeToNameMap.put("bn", "Bengali (বাংলা)");
        languageCodeToNameMap.put("pt", "Portuguese (Português)");
        languageCodeToNameMap.put("ru", "Russian (Русский)");
        languageCodeToNameMap.put("ja", "Japanese (日本語)");
        languageCodeToNameMap.put("pa", "Western Punjabi (ਪੰਜਾਬੀ)");
        languageCodeToNameMap.put("de", "German (Deutsch)");
        languageCodeToNameMap.put("jv", "Javanese (Basa Jawa)");
        languageCodeToNameMap.put("ko", "Korean (한국어)");
        languageCodeToNameMap.put("fr", "French (Français)");
        languageCodeToNameMap.put("te", "Telugu (తెలుగు)");
        languageCodeToNameMap.put("mr", "Marathi (ਮਰਾਠੀ)");
        languageCodeToNameMap.put("tr", "Turkish (Türkçe)");
        languageCodeToNameMap.put("ta", "Tamil (தமிழ்)");
        languageCodeToNameMap.put("vi", "Vietnamese (Tiếng Việt)");
        languageCodeToNameMap.put("ur", "Urdu (اردو)");
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

        // Get the TextView that displays the current language value
        menuLanguageValue = menuLanguage.findViewById(R.id.menu_value);
    }

    /**
     * Sets the titles and icons for the menu items, as these are in included layouts
     * and must be set programmatically.
     */
    private void setupMenuContent(View view) {
        // 1. Personal Info Menu
        TextView personalInfoTitle = menuPersonalInfo.findViewById(R.id.menu_title);
        ImageView personalInfoIcon = menuPersonalInfo.findViewById(R.id.menu_icon);

        if (personalInfoTitle != null) {
            personalInfoTitle.setText(R.string.personal_info_title);
        }
        if (personalInfoIcon != null) {
            // Assuming 'person' drawable is the icon for personal info
            personalInfoIcon.setImageResource(R.drawable.person);
        }

        // 2. Language Menu
        TextView languageTitle = menuLanguage.findViewById(R.id.menu_title);
        ImageView languageIcon = menuLanguage.findViewById(R.id.menu_icon);

        if (languageTitle != null) {
            languageTitle.setText(R.string.language_title);
        }
        if (languageIcon != null) {
            // Assuming 'language' drawable is the icon for language selection
            languageIcon.setImageResource(R.drawable.language);
        }

        // 3. Logout Menu (Title only)
        TextView logoutTitle = menuLogout.findViewById(R.id.menu_title);
        if (logoutTitle != null) {
            logoutTitle.setText(R.string.action_logout);
        }
    }


    private void setupMenu()
    {
        // 1. Personal Info Menu
        menuPersonalInfo.setOnClickListener(v ->
        {
            Intent i = new Intent(requireContext(), PersonalInfo.class);
            startActivity(i);
        });

        // 2. Language Menu
        menuLanguage.setOnClickListener(v ->
        {
            Intent i = new Intent(requireContext(), LanguageActivity.class);
            startActivity(i);
        });

        editProfileButton.setOnClickListener(v ->
        {
            Intent i = new Intent(requireContext(), PersonalInfo.class);
            startActivity(i);
        });

        // 3. Logout Logic (Re-implemented with Confirmation Dialog)
        menuLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    /**
     * Displays a confirmation dialog before logging out the user.
     */
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.logout_confirmation_title)
                .setMessage(R.string.logout_confirmation_message)
                .setPositiveButton(R.string.action_logout, (dialog, which) -> {
                    // User confirmed logout
                    if (prefManager != null) {
                        prefManager.clearLogin();
                        Toast.makeText(getContext(), R.string.logged_out_successfully, Toast.LENGTH_SHORT).show();

                        // Navigate to Login/Signup screen and clear the activity stack
                        Intent i = new Intent(requireContext(), LoginSignup.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        requireActivity().finish(); // Finish the activity hosting this fragment
                    }
                })
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> {
                    // User cancelled logout
                    dialog.dismiss();
                })
                .show();
    }

    private String extractFirstNameFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }
        String localPart = email.substring(0, email.indexOf('@'));
        if (localPart.length() > 0)
        {
            return localPart.substring(0, 1).toUpperCase() + localPart.substring(1);
        }
        return localPart;
    }


    @SuppressLint("SetTextI18n")
    private void loadUserData()
    {
        if (prefManager != null)
        {
            String email = prefManager.getEmail();
            String storedName = prefManager.getUserId(); // Assume UserID stores the name

            // Set Email
            userEmail.setText(email != null ? email : getString(R.string.not_available));

            // Determine Display Name
            String displayName;
            if (storedName != null && !storedName.isEmpty()) {
                displayName = storedName;
            } else if (email != null) {
                String extractedName = extractFirstNameFromEmail(email);
                displayName = extractedName != null ? extractedName : getString(R.string.not_available);
            } else {
                displayName = getString(R.string.not_available);
            }
            userName.setText(displayName);
        }
        else
        {
            userName.setText("Error");
            userEmail.setText("No Auth Manager");
        }
    }

    private void updateLanguageDisplay() {
        if (prefManager != null && menuLanguageValue != null) {
            String currentLocaleCode = prefManager.getLanguage();

            // FIX: Replace getOrDefault (API 24+) with standard null check (API 23 compatible)
            String languageName = languageCodeToNameMap.get(currentLocaleCode);

            if (languageName == null) {
                // Default value if locale code is not found
                languageName = "English";
            }

            menuLanguageValue.setText(languageName);
        }
    }
}