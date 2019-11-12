package com.example.onlyimportantnews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SettingsActivity";

    private static final String SETTINGS_DATA = "settingsData";
    private static final String IMPORTANCE_LEVEL = "importanceLevel";

    private static final String WORLD_NEWS_BOOLEAN = "worldNewsBoolean";
    private static final String US_POLITICS_BOOLEAN = "usPoliticsBoolean";
    private static final String UK_POLITICS_BOOLEAN = "ukPoliticsBoolean";
    private static final String TECHNOLOGY_BOOLEAN = "technologyBoolean";
    private static final String SCIENCE_BOOLEAN = "scienceBoolean";
    private static final String SPACE_BOOLEAN = "spaceBoolean";
    private static final String MOVIES_AND_TV_BOOLEAN = "moviesAndTvBoolean";
    private static final String ECONOMY_BOOLEAN = "economyBoolean";
    private static final String SPORTS_BOOLEAN = "sportsBoolean";
    private static final String HEALTH_BOOLEAN = "healthBoolean";

    private SharedPreferences.Editor settingsDataEditor;
    private SharedPreferences settingsData;

    private CheckBox worldNewsCheckBox;
    private CheckBox usPoliticsCheckBox;
    private CheckBox ukPoliticsCheckBox;
    private CheckBox technologyCheckBox;
    private CheckBox scienceCheckBox;
    private CheckBox spaceCheckBox;
    private CheckBox moviesAndTvCheckBox;
    private CheckBox economyCheckBox;
    private CheckBox healthCheckBox;
    private CheckBox sportsCheckBox;

    private RadioGroup importanceRadioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        getSupportActionBar().setTitle("Only Important News");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingsData = getSharedPreferences(SETTINGS_DATA, MODE_PRIVATE);
        settingsDataEditor = settingsData.edit();

        importanceRadioGroup = findViewById(R.id.importanceRadioGroup);
        RadioButton notVeryImportantNewsButton = findViewById(R.id.notVeryImportantNewsButton);
        RadioButton importantNewsButton = findViewById(R.id.importantNewsButton);
        RadioButton veryImportantNewsButton = findViewById(R.id.veryImportantNewsButton);

        switch(settingsData.getInt(IMPORTANCE_LEVEL, 1)) {
            case 0:
                importanceRadioGroup.check(notVeryImportantNewsButton.getId());
                break;
            case 1:
                importanceRadioGroup.check(importantNewsButton.getId());
                break;
            case 2:
                importanceRadioGroup.check(veryImportantNewsButton.getId());
                break;
        }


        worldNewsCheckBox = findViewById(R.id.worldNewsCheckBox);
        worldNewsCheckBox.setOnClickListener(this);
        worldNewsCheckBox.setChecked(settingsData.getBoolean(WORLD_NEWS_BOOLEAN, true));

        usPoliticsCheckBox = findViewById(R.id.usPoliticsCheckBox);
        usPoliticsCheckBox.setOnClickListener(this);
        usPoliticsCheckBox.setChecked(settingsData.getBoolean(US_POLITICS_BOOLEAN, true));

        ukPoliticsCheckBox = findViewById(R.id.ukPoliticsCheckBox);
        ukPoliticsCheckBox.setOnClickListener(this);
        ukPoliticsCheckBox.setChecked(settingsData.getBoolean(UK_POLITICS_BOOLEAN, true));

        technologyCheckBox = findViewById(R.id.technologyCheckBox);
        technologyCheckBox.setOnClickListener(this);
        technologyCheckBox.setChecked(settingsData.getBoolean(TECHNOLOGY_BOOLEAN, true));

        scienceCheckBox = findViewById(R.id.scienceCheckBox);
        scienceCheckBox.setOnClickListener(this);
        scienceCheckBox.setChecked(settingsData.getBoolean(SCIENCE_BOOLEAN, true));

        spaceCheckBox = findViewById(R.id.spaceCheckBox);
        spaceCheckBox.setOnClickListener(this);
        spaceCheckBox.setChecked(settingsData.getBoolean(SPACE_BOOLEAN, true));

        moviesAndTvCheckBox = findViewById(R.id.moviesAndTvCheckBox);
        moviesAndTvCheckBox.setOnClickListener(this);
        moviesAndTvCheckBox.setChecked(settingsData.getBoolean(MOVIES_AND_TV_BOOLEAN, true));

        economyCheckBox = findViewById(R.id.economyCheckBox);
        economyCheckBox.setOnClickListener(this);
        economyCheckBox.setChecked(settingsData.getBoolean(ECONOMY_BOOLEAN, true));

        healthCheckBox = findViewById(R.id.healthCheckBox);
        healthCheckBox.setOnClickListener(this);
        healthCheckBox.setChecked(settingsData.getBoolean(HEALTH_BOOLEAN, true));

        sportsCheckBox = findViewById(R.id.sportsCheckBox);
        sportsCheckBox.setOnClickListener(this);
        sportsCheckBox.setChecked(settingsData.getBoolean(SPORTS_BOOLEAN, true));

        setImportanceRadioGroupListener();
    }

    private void setImportanceRadioGroupListener() {
        importanceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedButton = findViewById(checkedId);

                int index = importanceRadioGroup.indexOfChild(selectedButton);

                switch (index) { // completely backwards
                    case 0:
                        settingsDataEditor.putInt(IMPORTANCE_LEVEL, 0);
                        break;
                    case 1:
                        settingsDataEditor.putInt(IMPORTANCE_LEVEL, 1);
                        break;
                    case 2:
                        settingsDataEditor.putInt(IMPORTANCE_LEVEL, 2);
                        break;
                }
                settingsDataEditor.apply();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.worldNewsCheckBox:
                settingsDataEditor.putBoolean(WORLD_NEWS_BOOLEAN, !settingsData.getBoolean(WORLD_NEWS_BOOLEAN, false));
                break;
            case R.id.usPoliticsCheckBox:
                settingsDataEditor.putBoolean(US_POLITICS_BOOLEAN, !settingsData.getBoolean(US_POLITICS_BOOLEAN, false));
                break;
            case R.id.ukPoliticsCheckBox:
                settingsDataEditor.putBoolean(UK_POLITICS_BOOLEAN, !settingsData.getBoolean(UK_POLITICS_BOOLEAN, false));
                break;
            case R.id.technologyCheckBox:
                settingsDataEditor.putBoolean(TECHNOLOGY_BOOLEAN, !settingsData.getBoolean(TECHNOLOGY_BOOLEAN, false));
                break;
            case R.id.scienceCheckBox:
                settingsDataEditor.putBoolean(SCIENCE_BOOLEAN, !settingsData.getBoolean(SCIENCE_BOOLEAN, false));
                break;
            case R.id.spaceCheckBox:
                settingsDataEditor.putBoolean(SPACE_BOOLEAN, !settingsData.getBoolean(SPACE_BOOLEAN, false));
                break;
            case R.id.moviesAndTvCheckBox:
                settingsDataEditor.putBoolean(MOVIES_AND_TV_BOOLEAN, !settingsData.getBoolean(MOVIES_AND_TV_BOOLEAN, false));
                break;
            case R.id.economyCheckBox:
                settingsDataEditor.putBoolean(ECONOMY_BOOLEAN, !settingsData.getBoolean(ECONOMY_BOOLEAN, false));
                break;
            case R.id.healthCheckBox:
                settingsDataEditor.putBoolean(HEALTH_BOOLEAN, !settingsData.getBoolean(HEALTH_BOOLEAN, false));
                break;
            case R.id.sportsCheckBox:
                settingsDataEditor.putBoolean(SPORTS_BOOLEAN, !settingsData.getBoolean(SPORTS_BOOLEAN, false));
                break;
            default:
                Log.e(TAG, "onClick: ERROR, SWITCH DEFAULTED");
        }
        settingsDataEditor.apply();
    }
}
