package com.example.housing.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.housing.R;
import com.example.housing.utils.PrefManager;

import java.util.LinkedHashMap;
import java.util.Map;

// LanguageActivity now extends BaseActivity
public class LanguageActivity extends BaseActivity {

    private PrefManager prefManager;
    private Spinner languageSpinner;

    // Use LinkedHashMap to maintain insertion order (for consistent spinner display)
    // Key = Display Name, Value = Locale Code (ISO 639-1)
    private final Map<String, String> languageMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // BaseActivity handles setting the correct locale before super.onCreate()
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        prefManager = PrefManager.getInstance(this);

        setupLanguageMap();
        initViews();
        setupSpinner();
    }

    // NOTE: attachBaseContext is handled by BaseActivity

    /**
     * Sets up the map with the world's top 20 most spoken languages (based on available locale codes).
     */
    private void setupLanguageMap() {
        // English is set as default in PrefManager
        languageMap.put("English", "en");
        languageMap.put("Mandarin Chinese (中文)", "zh");
        languageMap.put("Spanish (Español)", "es");
        languageMap.put("Hindi (हिन्दी)", "hi");
        languageMap.put("Arabic (العربية)", "ar");
        languageMap.put("Bengali (বাংলা)", "bn");
        languageMap.put("Portuguese (Português)", "pt");
        languageMap.put("Russian (Русский)", "ru");
        languageMap.put("Japanese (日本語)", "ja");
        languageMap.put("Western Punjabi (ਪੰਜਾਬੀ)", "pa");
        languageMap.put("German (Deutsch)", "de");
        languageMap.put("Javanese (Basa Jawa)", "jv");
        languageMap.put("Korean (한국어)", "ko");
        languageMap.put("French (Français)", "fr");
        languageMap.put("Telugu (తెలుగు)", "te");
        languageMap.put("Marathi (ਮਰਾਠੀ)", "mr");
        languageMap.put("Turkish (Türkçe)", "tr");
        languageMap.put("Tamil (தமிழ்)", "ta");
        languageMap.put("Vietnamese (Tiếng Việt)", "vi");
        languageMap.put("Urdu (اردو)", "ur");
    }

    private void initViews() {
        languageSpinner = findViewById(R.id.language_spinner);

        // If you had a back button in the RelativeLayout, you'd initialize it here,
        // but since you didn't include one, we'll keep it simple.
    }

    private void setupSpinner()
    {
        String[] languages = languageMap.keySet().toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_custom_spinner_text,
                languages
        );

        adapter.setDropDownViewResource(R.layout.item_custom_spinner_dropdown);
        languageSpinner.setAdapter(adapter);

        String currentLocaleCode = prefManager.getLanguage();
        String currentLanguageName = languages[0];

        for (Map.Entry<String, String> entry : languageMap.entrySet())
        {
            if (entry.getValue().equals(currentLocaleCode))
            {
                currentLanguageName = entry.getKey();
                break;
            }
        }

        int selectionPosition = adapter.getPosition(currentLanguageName);
        languageSpinner.setSelection(selectionPosition);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguageName = (String) parent.getItemAtPosition(position);
                String localeCode = languageMap.get(selectedLanguageName);

                // Only proceed if the selection is a new locale code
                if (localeCode != null && !localeCode.equals(prefManager.getLanguage())) {
                    // 1. Save the new language code
                    prefManager.setLanguage(localeCode);

                    // 2. Trigger activity restart. BaseActivity will pick up the new locale.
                    recreate();

                    Toast.makeText(LanguageActivity.this,
                            getString(R.string.language_changed_to) + " " + selectedLanguageName,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
}