package com.g09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Stats extends AppCompatActivity {

    TextView stats1;
    TextView stats2;
    TextView stats3;
    TextView stats4;
    TextView stats5;
    TextView stats6;
    TextView stats6v2;
    TextView stats7;
    TextView stats8;
    Button resetStatsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getFlag() ? R.style.AppThemeDark : R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        stats1 = findViewById(R.id.stats1);
        stats2 = findViewById(R.id.stats2);
        stats3 = findViewById(R.id.stats3);
        stats4 = findViewById(R.id.stats4);
        stats5 = findViewById(R.id.stats5);
        stats6 = findViewById(R.id.stats6);
        stats6v2 = findViewById(R.id.stats6v2);
        stats7 = findViewById(R.id.stats7);
        stats8 = findViewById(R.id.stats8);
        resetStatsBtn = findViewById(R.id.resetScores);

        refreshScores();

        resetStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetStats();
                Intent intent = new Intent(Stats.this, MainActivity.class);
                startActivity(intent);
                Intent intent2 = new Intent(Stats.this, Stats.class);
                startActivity(intent2);
            }
        });

    }

    private float getHighScore(String statsNumberCurrentHS) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getFloat(statsNumberCurrentHS, 900);
    }


    public void refreshScores() {
        stats1.setText(String.valueOf(getHighScore("stats1CurrentHS")));
        stats2.setText(String.valueOf(getHighScore("stats2CurrentHS")));
        stats3.setText(String.valueOf(getHighScore("stats3CurrentHS")));
        stats4.setText(String.valueOf(getHighScore("stats4CurrentHS")));
        stats5.setText(String.valueOf(getHighScore("stats5CurrentHS")));
        stats6.setText(String.valueOf(getHighScore("stats6CurrentHS")));
        stats6v2.setText(String.valueOf(getHighScore("stats6v2CurrentHS")));
        stats7.setText(String.valueOf(getHighScore("stats7CurrentHS")));
        stats8.setText(String.valueOf(getHighScore("stats8CurrentHS")));
    }

    private void resetStats() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().remove("stats1CurrentHS").apply();
        preferences.edit().remove("stats2CurrentHS").apply();
        preferences.edit().remove("stats3CurrentHS").apply();
        preferences.edit().remove("stats4CurrentHS").apply();
        preferences.edit().remove("stats5CurrentHS").apply();
        preferences.edit().remove("stats6CurrentHS").apply();
        preferences.edit().remove("stats6v2CurrentHS").apply();
        preferences.edit().remove("stats7CurrentHS").apply();
        preferences.edit().remove("stats8CurrentHS").apply();
    }

    private boolean getFlag() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("dark", false);
    }
}