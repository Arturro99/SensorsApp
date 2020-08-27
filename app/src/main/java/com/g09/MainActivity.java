package com.g09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import com.g09.levels.Lvl1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getFlag() ? R.style.AppThemeDark : R.style.AppThemeLight);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startBtn = findViewById(R.id.startBTN);
        startBtn.setOnClickListener(view -> {
            saveFlagStart(true);
            startActivity(new Intent(getApplicationContext(), Lvl1.class));
        });

        Button lvlBTN = findViewById(R.id.levelsBTN);
        lvlBTN.setOnClickListener(view -> {
            saveFlagStart(false);
            startActivity(new Intent(getApplicationContext(), Levels.class));
        });

        Button optionsBtn = findViewById(R.id.optionsBTN);
        optionsBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Options.class)));

        Button statsBtn = findViewById(R.id.statsBtn);
        statsBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Stats.class)));

        Button exitBtn = findViewById(R.id.exitBTN);
        exitBtn.setOnClickListener(view -> {
            Intent exitIntent = new Intent(Intent.ACTION_MAIN);
            exitIntent.addCategory(Intent.CATEGORY_HOME);
            exitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(exitIntent);
        });
    }



    private boolean getFlag() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("dark", false);
    }

    private void saveFlagStart(boolean flag) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("start", flag);
        editor.apply();
    }
}