package com.g09;

import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class Stats extends AppCompatActivity {

    TextView stats1;
    TextView stats2;
    TextView stats3;
    TextView stats4;
    TextView stats5;
    TextView stats6;
    TextView stats7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        stats1 = findViewById(R.id.stats1);
        stats2 = findViewById(R.id.stats2);
        stats3 = findViewById(R.id.stats3);
        stats4 = findViewById(R.id.stats4);
        stats5 = findViewById(R.id.stats5);
        stats6 = findViewById(R.id.stats6);
        stats7 = findViewById(R.id.stats7);

        stats1.setText(String.valueOf(getHighScore("stats1")));
        stats2.setText(String.valueOf(getHighScore("stats2")));
        stats3.setText(String.valueOf(getHighScore("stats3")));
        stats4.setText(String.valueOf(getHighScore("stats4")));
        stats5.setText(String.valueOf(getHighScore("stats5")));
        stats6.setText(String.valueOf(getHighScore("stats6")));
        stats7.setText(String.valueOf(getHighScore("stats7")));

    }

    private float getHighScore(String statsNumber) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getFloat(statsNumber, 0);
    }

    public void updateScores(float score, String statsNumber) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();

        switch(statsNumber) {
            case "stats1": {
                if(score < Float.parseFloat(stats1.getText().toString()) || Float.parseFloat(stats1.getText().toString()) == 0.0 ) {
                    editor.putFloat(statsNumber, score);
                    editor.apply();
                }
                break;
            }
            case "stats2": {
                if(score < Float.parseFloat(stats2.getText().toString()) || Float.parseFloat(stats2.getText().toString()) == 0.0 ) {
                    editor.putFloat(statsNumber, score);
                    editor.apply();
                }
                break;
            }
            case "stats3": {
                if(score < Float.parseFloat(stats3.getText().toString()) || Float.parseFloat(stats3.getText().toString()) == 0.0 ) {
                    editor.putFloat(statsNumber, score);
                    editor.apply();
                }
                break;
            }
            case "stats4": {
                if(score < Float.parseFloat(stats4.getText().toString()) || Float.parseFloat(stats4.getText().toString()) == 0.0 ) {
                    editor.putFloat(statsNumber, score);
                    editor.apply();
                }
                break;
            }
            case "stats5": {
                if(score < Float.parseFloat(stats5.getText().toString()) || Float.parseFloat(stats5.getText().toString()) == 0.0 ) {
                    editor.putFloat(statsNumber, score);
                    editor.apply();
                }
                break;
            }
            case "stats6": {
                if(score < Float.parseFloat(stats6.getText().toString()) || Float.parseFloat(stats6.getText().toString()) == 0.0 ) {
                    editor.putFloat(statsNumber, score);
                    editor.apply();
                }
                break;
            }
            case "stats7": {
                if(score < Float.parseFloat(stats7.getText().toString()) || Float.parseFloat(stats7.getText().toString()) == 0.0 ) {
                    editor.putFloat(statsNumber, score);
                    editor.apply();
                }
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + statsNumber);
        }
    }
}