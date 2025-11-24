package com.example.housing.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.housing.R;
import com.example.housing.holders.PersonalInfoViewModel;
import com.example.housing.utils.PrefManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalInfo extends AppCompatActivity
{
    private EditText infoFullName, infoEmail, infoPhoneNumber;
    private EditText infoUserExperienceYears, infoUserSkills, infoUserDescription;

    private Spinner infoUserRoleSpinner, infoServiceCategorySpinner, infoUserServicesSpinner;

    private TextView tv_category_label, tv_services_label;
    private TextView tv_exp_label, tv_skill_label, tv_desc_label;

    private Button btnEditProfile, btnSaveProfile, btnAddService;

    private PersonalInfoViewModel viewModel;
    private PrefManager pref;

    private boolean categoriesLoaded = false;
    private boolean servicesLoaded = false;

    private String userRole = "customer"; // default
    private String selectedCategoryId = null;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        pref = PrefManager.getInstance(this);
        viewModel = new ViewModelProvider(this).get(PersonalInfoViewModel.class);

        initViews();
        initDefaultAdapters();
        observeLiveData();

        viewModel.fetchUserDetails(pref.getAccessToken(), pref.getUserId());

        setupRoleVisibility();
        setupCategorySpinnerLazyLoad();
        setupServiceSpinnerLazyLoad();
        setupEditAndSave();
        setupAddServiceButton();

        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if (isEditing)
                {
                    new AlertDialog.Builder(PersonalInfo.this)
                            .setTitle("Unsaved Changes")
                            .setMessage("Your information will not be saved. Do you want to save it?")
                            .setPositiveButton("Save", (dialog, which) -> {
                                btnSaveProfile.performClick();
                                finish();
                            })
                            .setNegativeButton("Discard", (dialog, which) -> finish())
                            .setCancelable(true)
                            .show();
                }
                else
                {
                    setEnabled(false);
                    PersonalInfo.super.onBackPressed();
                }
            }
        });
    }

    // -------------------------------------------------------------
    // Init Views
    // -------------------------------------------------------------
    private void initViews()
    {
        infoFullName = findViewById(R.id.info_user_full_name);
        infoEmail = findViewById(R.id.info_user_email);
        infoPhoneNumber = findViewById(R.id.info_user_phone_number);

        infoUserExperienceYears = findViewById(R.id.info_user_experience_years);
        infoUserSkills = findViewById(R.id.info_user_skills);
        infoUserDescription = findViewById(R.id.info_user_description);

        infoUserRoleSpinner = findViewById(R.id.info_user_role_spinner);
        infoServiceCategorySpinner = findViewById(R.id.info_service_category_spinner);
        infoUserServicesSpinner = findViewById(R.id.info_user_services_spinner);

        tv_category_label = findViewById(R.id.tv_service_category_label);
        tv_services_label = findViewById(R.id.tv_service_label);

        tv_exp_label = findViewById(R.id.user_experience_label);
        tv_skill_label = findViewById(R.id.user_skills_label);
        tv_desc_label = findViewById(R.id.user_description_label);

        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnSaveProfile = findViewById(R.id.btn_save_profile);
        btnAddService = findViewById(R.id.btn_add_service);

        enableEditing(false);
        lockSpinners(true);
    }

    // -------------------------------------------------------------
    // Initialize default adapters for spinners
    // -------------------------------------------------------------
    private void initDefaultAdapters()
    {
        // Role spinner
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"customer", "professional"}
        );
        infoUserRoleSpinner.setAdapter(roleAdapter);

        // Category and service spinners
        ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Loading..."}
        );
        infoServiceCategorySpinner.setAdapter(emptyAdapter);
        infoUserServicesSpinner.setAdapter(emptyAdapter);
    }

    // -------------------------------------------------------------
    // LiveData Observers
    // -------------------------------------------------------------
    private void observeLiveData()
    {
        viewModel.getUserDetailsLiveData().observe(this, user ->
        {
            if (user == null) return;

            infoFullName.setText(getSafeString(user, "full_name", "Please add full name"));
            infoEmail.setText(getSafeString(user, "email", "Please add email"));
            infoPhoneNumber.setText(getSafeString(user, "phone_number", "Please add phone"));

            userRole = getSafeString(user, "role", "customer");
            int pos = "professional".equals(userRole) ? 1 : 0;
            infoUserRoleSpinner.setSelection(pos);

            if ("professional".equals(userRole))
            {
                infoUserExperienceYears.setText(getSafeString(user, "experience_years", "Please add experience"));

                // Convert JSON array skills to comma-separated string
                if (user.has("skills") && user.get("skills").isJsonArray())
                {
                    JsonArray skillsArray = user.get("skills").getAsJsonArray();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < skillsArray.size(); i++)
                    {
                        if (i > 0) sb.append(", ");
                        sb.append(skillsArray.get(i).getAsString());
                    }
                    infoUserSkills.setText(sb.toString());
                }
                else
                {
                    infoUserSkills.setText("Please add skills");
                }

                infoUserDescription.setText(getSafeString(user, "description", "Please add description"));
            }

            setupRoleVisibility();
        });

        viewModel.getCategoriesLiveData().observe(this, list ->
        {
            List<String> names = new ArrayList<>();
            if (list != null && !list.isEmpty())
            {
                for (JsonObject obj : list)
                    names.add(getSafeString(obj, "name", "Unnamed"));
            }
            else names.add("No categories");

            infoServiceCategorySpinner.setAdapter(
                    new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names)
            );
        });

        viewModel.getUserServicesLiveData().observe(this, list ->
        {
            List<String> names = new ArrayList<>();
            if (list != null && !list.isEmpty())
            {
                for (JsonObject obj : list)
                    names.add(getSafeString(obj, "name", "Unnamed"));
            }
            else names.add("No services");

            infoUserServicesSpinner.setAdapter(
                    new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names)
            );
        });

        viewModel.getErrorMessageLiveData().observe(this, msg ->
        {
            if (msg != null)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        viewModel.getUpdateSuccessLiveData().observe(this, success ->
        {
            if (Boolean.TRUE.equals(success))
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
        });
    }

    // -------------------------------------------------------------
    // Null-safe helper
    // -------------------------------------------------------------
    private String getSafeString(JsonObject obj, String key, String fallback)
    {
        try {
            if (obj.has(key) && !obj.get(key).isJsonNull())
            {
                // Handle arrays (like skills)
                if (obj.get(key).isJsonArray())
                {
                    JsonArray arr = obj.get(key).getAsJsonArray();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < arr.size(); i++)
                    {
                        if (i > 0) sb.append(", ");
                        sb.append(arr.get(i).getAsString());
                    }
                    return sb.toString();
                }
                return obj.get(key).getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fallback;
    }

    // -------------------------------------------------------------
    // Role-Based Visibility
    // -------------------------------------------------------------
    private void setupRoleVisibility()
    {
        if ("professional".equals(userRole))
            setProfessionalVisibility(View.VISIBLE);
        else
            setProfessionalVisibility(View.GONE);
    }

    private void setProfessionalVisibility(int v)
    {
        tv_category_label.setVisibility(v);
        tv_services_label.setVisibility(v);

        tv_exp_label.setVisibility(v);
        tv_skill_label.setVisibility(v);
        tv_desc_label.setVisibility(v);

        infoServiceCategorySpinner.setVisibility(v);
        infoUserServicesSpinner.setVisibility(v);

        infoUserExperienceYears.setVisibility(v);
        infoUserSkills.setVisibility(v);
        infoUserDescription.setVisibility(v);

        btnAddService.setVisibility(v);
    }

    // -------------------------------------------------------------
    // Category Lazy Load
    // -------------------------------------------------------------
    private void setupCategorySpinnerLazyLoad()
    {
        infoServiceCategorySpinner.setOnTouchListener((v, e) ->
        {
            if (e.getAction() == MotionEvent.ACTION_DOWN && !categoriesLoaded && isEditing)
            {
                viewModel.fetchCategories(pref.getAccessToken());
                categoriesLoaded = true;
            }
            return false;
        });

        infoServiceCategorySpinner.setOnItemSelectedListener(new SimpleItemSelectedListener()
        {
            @Override
            public void onItemSelected(int pos)
            {
                List<JsonObject> list = viewModel.getCategoriesLiveData().getValue();
                if (list == null || list.isEmpty()) return;

                selectedCategoryId = list.get(pos).get("id").getAsString();

                servicesLoaded = false;
                infoUserServicesSpinner.setAdapter(
                        new ArrayAdapter<>(PersonalInfo.this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Loading..."})
                );
            }
        });
    }

    // -------------------------------------------------------------
    // Services Lazy Load
    // -------------------------------------------------------------
    private void setupServiceSpinnerLazyLoad()
    {
        infoUserServicesSpinner.setOnTouchListener((v, e) ->
        {
            if (e.getAction() == MotionEvent.ACTION_DOWN && isEditing)
            {
                if (selectedCategoryId != null && !servicesLoaded)
                {
                    viewModel.fetchUserServices(pref.getAccessToken(), pref.getUserId(), selectedCategoryId);
                    servicesLoaded = true;
                }
            }
            return false;
        });
    }

    // -------------------------------------------------------------
    // Edit & Save Buttons
    // -------------------------------------------------------------
    private void setupEditAndSave()
    {
        btnEditProfile.setOnClickListener(v -> {
            isEditing = true;
            enableEditing(true);
            lockSpinners(false);
        });

        btnSaveProfile.setOnClickListener(v -> {
            if (!isEditing) return;

            JsonObject updates = new JsonObject();
            updates.addProperty("full_name", infoFullName.getText().toString());
            updates.addProperty("phone_number", infoPhoneNumber.getText().toString());
            updates.addProperty("email", infoEmail.getText().toString());

            if ("professional".equals(userRole))
            {
                updates.addProperty("experience_years", Integer.parseInt(infoUserExperienceYears.getText().toString()));

                // Convert skills string to JSON array
                String skillsStr = infoUserSkills.getText().toString();
                JsonArray skillsArray = new JsonArray();
                for (String skill : skillsStr.split(",")) {
                    skill = skill.trim();
                    if (!skill.isEmpty()) skillsArray.add(skill);
                }
                updates.add("skills", skillsArray);

                updates.addProperty("description", infoUserDescription.getText().toString());
            }

            viewModel.updateUser(pref.getAccessToken(), pref.getUserId(), updates);

            isEditing = false;
            enableEditing(false);
            lockSpinners(true);
        });
    }

    private void enableEditing(boolean enable)
    {
        infoFullName.setEnabled(enable);
        infoEmail.setEnabled(enable);
        infoPhoneNumber.setEnabled(enable);

        if ("professional".equals(userRole))
        {
            infoUserExperienceYears.setEnabled(enable);
            infoUserSkills.setEnabled(enable);
            infoUserDescription.setEnabled(enable);
        }
    }

    private void lockSpinners(boolean lock)
    {
        infoUserRoleSpinner.setEnabled(!lock);
        infoServiceCategorySpinner.setEnabled(!lock);
        infoUserServicesSpinner.setEnabled(!lock);
    }

    // -------------------------------------------------------------
    // Add Service Button
    // -------------------------------------------------------------
    private void setupAddServiceButton()
    {
        btnAddService.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_service, null);

            EditText etName = dialogView.findViewById(R.id.et_service_name);
            EditText etDesc = dialogView.findViewById(R.id.et_service_description);
            EditText etPrice = dialogView.findViewById(R.id.et_service_price);
            EditText etImg = dialogView.findViewById(R.id.et_service_image_url);

            new AlertDialog.Builder(this)
                    .setTitle("Add Service")
                    .setView(dialogView)
                    .setPositiveButton("Save", (d, w) -> {
                        if (selectedCategoryId == null) {
                            Toast.makeText(this, "Select a category first", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String name = etName.getText().toString();
                        String desc = etDesc.getText().toString();
                        String priceStr = etPrice.getText().toString();
                        String img = etImg.getText().toString();

                        if (name.isEmpty() || priceStr.isEmpty()) {
                            Toast.makeText(this, "Name and Price required", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int price = Integer.parseInt(priceStr);

                        viewModel.addService(
                                pref.getAccessToken(),
                                pref.getUserId(),
                                selectedCategoryId,
                                name,
                                desc,
                                price,
                                img
                        );

                        servicesLoaded = false;
                    })
                    .setNegativeButton("Cancel", (d, w) -> d.dismiss())
                    .show();
        });
    }

    // -------------------------------------------------------------
    // Custom Item Selected Listener
    // -------------------------------------------------------------
    public abstract static class SimpleItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener
    {
        @Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) {}

        @Override
        public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id)
        {
            onItemSelected(position);
        }

        public abstract void onItemSelected(int position);
    }
}
