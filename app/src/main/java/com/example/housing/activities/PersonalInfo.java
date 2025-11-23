package com.example.housing.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.housing.R;
import com.example.housing.models.Category;
import com.example.housing.models.Service;
import com.example.housing.viewmodel.PersonalInfoViewModel;
import com.example.housing.utils.PrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalInfo extends AppCompatActivity {

    private PersonalInfoViewModel viewModel;
    private PrefManager pref;

    private EditText fullName, phone, experience, skills, description, serviceName;
    private Spinner roleSpinner, categorySpinner;

    private View btnEdit, btnSave;

    private boolean isEditMode = false;
    private String selectedCategoryId = null;
    private List<Category> categoryList = new ArrayList<>();
    private List<Service> serviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        viewModel = new ViewModelProvider(this).get(PersonalInfoViewModel.class);
        pref = PrefManager.getInstance(PersonalInfo.this);

        initViews();
        setObservers();
        loadUser();

        btnEdit.setOnClickListener(v -> enableEditMode());
        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void initViews() {
        fullName = findViewById(R.id.info_user_full_name);
        phone = findViewById(R.id.info_user_phone_number);
        experience = findViewById(R.id.info_user_experience_years);
        skills = findViewById(R.id.info_user_skills);
        description = findViewById(R.id.info_user_description);
        serviceName = findViewById(R.id.info_service_name);
        roleSpinner = findViewById(R.id.info_user_role_spinner);
        categorySpinner = findViewById(R.id.info_service_category_spinner);

        btnEdit = findViewById(R.id.btn_edit_profile);
        btnSave = findViewById(R.id.btn_save_profile);

        disableFields();
    }

    private void setObservers() {

        viewModel.getUserDetailsLiveData().observe(this, user -> {

            if (user == null) return;

            fullName.setText((String) user.get("name"));
            phone.setText((String) user.get("phone"));

            String role = (String) user.get("role");
            setRole(role);

            if (role.equals("customer")) {
                hideProfessionalFields();
            }

            experience.setText(String.valueOf(user.get("experience_years")));
            skills.setText((String) user.get("skills"));
            description.setText((String) user.get("description"));

            selectedCategoryId = (String) user.get("category_id");

            viewModel.fetchCategories();

            if (selectedCategoryId != null) {
                viewModel.fetchUserServices(pref.getUserId(), selectedCategoryId);
            }
        });

        viewModel.getCategoriesLiveData().observe(this, list -> {
            if (list == null) return;

            categoryList = list;

            ArrayList<String> names = new ArrayList<>();
            for (Category c : list) names.add(c.getName());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, names);

            categorySpinner.setAdapter(adapter);

            if (selectedCategoryId != null) {
                int index = getCategoryIndex(selectedCategoryId);
                if (index >= 0) categorySpinner.setSelection(index);
            }
        });

        viewModel.getUserServicesLiveData().observe(this, services -> {
            serviceList = services;
            if (!services.isEmpty()) {
                serviceName.setText(services.get(0).getName());
            }
        });

        viewModel.getUpdateSuccessLiveData().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                disableFields();
                isEditMode = false;
            }
        });

        viewModel.getErrorMessageLiveData().observe(this, msg -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUser() {
        viewModel.fetchUserDetails(pref.getUserId());
    }

    private void enableEditMode() {
        isEditMode = true;

        setFieldColors(R.color.black);
        enableFields();
        showAddServiceDialog();
    }

    private void disableFields() {
        setFieldsEnabled(false);
        setFieldColors(R.color.dark_gray);
    }

    private void enableFields() {
        setFieldsEnabled(true);
    }

    private void setFieldsEnabled(boolean enabled) {
        fullName.setEnabled(enabled);
        phone.setEnabled(enabled);
        experience.setEnabled(enabled);
        skills.setEnabled(enabled);
        description.setEnabled(enabled);
        categorySpinner.setEnabled(enabled);
        serviceName.setEnabled(enabled);
    }

    private void hideProfessionalFields() {
        experience.setVisibility(View.GONE);
        skills.setVisibility(View.GONE);
        description.setVisibility(View.GONE);
        categorySpinner.setVisibility(View.GONE);
        serviceName.setVisibility(View.GONE);
    }

    private void setFieldColors(int color) {
        fullName.setTextColor(getResources().getColor(color));
        phone.setTextColor(getResources().getColor(color));
        experience.setTextColor(getResources().getColor(color));
        skills.setTextColor(getResources().getColor(color));
        description.setTextColor(getResources().getColor(color));
        serviceName.setTextColor(getResources().getColor(color));
    }

    private void setRole(String role) {
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"customer", "professional"});

        roleSpinner.setAdapter(roleAdapter);

        if (role.equals("customer"))
            roleSpinner.setSelection(0);
        else
            roleSpinner.setSelection(1);

        roleSpinner.setEnabled(false);
    }

    private int getCategoryIndex(String id) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId().equals(id)) return i;
        }
        return -1;
    }

    private void showAddServiceDialog() {
        if (!isEditMode) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Service");

        final EditText input = new EditText(this);
        input.setHint("Enter new service name");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = input.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Service name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedCategoryId == null) {
                Toast.makeText(this, "Select a category first", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.addService(pref.getUserId(), selectedCategoryId, name);
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void saveProfile() {
        if (!isEditMode) return;

        int index = categorySpinner.getSelectedItemPosition();
        selectedCategoryId = categoryList.get(index).getId();

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", fullName.getText().toString());
        updates.put("phone", phone.getText().toString());
        updates.put("experience_years", experience.getText().toString());
        updates.put("skills", skills.getText().toString());
        updates.put("description", description.getText().toString());
        updates.put("category_id", selectedCategoryId);

        List<String> selectedServiceIds = new ArrayList<>();
        for (Service s : serviceList) selectedServiceIds.add(s.getId());
        updates.put("service_ids", selectedServiceIds);

        viewModel.updateUser(pref.getUserId(), updates);
    }
}
