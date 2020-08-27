package com.g09;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class Options extends AppCompatActivity {

    Switch showTime;
    Button credits;
    TextView options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(getFlag() ? R.style.AppThemeDark : R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

         showTime = findViewById(R.id.showTime);
         credits = findViewById(R.id.credits);
         options = findViewById(R.id.options);

        Switch darkMode = findViewById(R.id.darkMode);
        darkMode.setChecked(getFlag());

        darkMode.setOnCheckedChangeListener((compoundButton, b) -> {
            if(compoundButton.isChecked()) {
                saveFlag(true);
                Intent intent = new Intent(Options.this, MainActivity.class);
                startActivity(intent);
                Intent intent2 = new Intent(Options.this, Options.class);
                startActivity(intent2);
            }
            else {
                saveFlag(false);
                Intent intent = new Intent(Options.this, MainActivity.class);
                startActivity(intent);
                Intent intent2 = new Intent(Options.this, Options.class);
                startActivity(intent2);
            }
            finish();
        });

        showTime.setChecked(getFlagTime());
        showTime.setOnCheckedChangeListener((compoundButton, b) -> {
            if(compoundButton.isChecked()) {
                saveFlagTime(true);
                Intent intent = new Intent(Options.this, MainActivity.class);
                startActivity(intent);
                Intent intent2 = new Intent(Options.this, Options.class);
                startActivity(intent2);
            }
            else {
                saveFlagTime(false);
                Intent intent = new Intent(Options.this, MainActivity.class);
                startActivity(intent);
                Intent intent2 = new Intent(Options.this, Options.class);
                startActivity(intent2);
            }
            finish();
        });

        credits.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Options.this);
            alertDialog.setMessage("Daniel Łondka" +
                    "\nArtur Madaj" +
                    "\nWojciech Sowa")
                    .setTitle("Autorzy")
                    .setCancelable(false)
                    .setNegativeButton("Ok", (dialog, id) -> {
                    });
            alertDialog.show();
        });
    }

    private void saveFlag(boolean flag) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("dark", flag);
        editor.apply();
    }
    private boolean getFlag() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("dark", false);
    }

    private void saveFlagTime(boolean flag) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("time", flag);
        editor.apply();
    }

    private boolean getFlagTime() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("time", false);
    }
}
