package com.g09;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.g09.levels.*;

public class Levels extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(getFlag() ? R.style.AppThemeDark : R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levels);
        Button lvl1BTN = findViewById(R.id.lvl1BTN);
        Button lvl2BTN = findViewById(R.id.lvl2BTN);
        Button lvl3BTN = findViewById(R.id.lvl3BTN);
        Button lvl4BTN = findViewById(R.id.lvl4BTN);
        Button lvl5BTN = findViewById(R.id.lvl5BTN);
        Button lvl6BTN = findViewById(R.id.lvl6BTN);
        Button lvl6v2Btn = findViewById(R.id.lvl6v2BTN);
        Button lvl7BTN = findViewById(R.id.lvl7BTN);
        Button lvl8BTN = findViewById(R.id.lvl8BTN);


        lvl1BTN.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Lvl1.class)));
        lvl2BTN.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Lvl2.class)));
        lvl3BTN.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Lvl3.class)));
        lvl4BTN.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Lvl4.class)));
        lvl5BTN.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Lvl5.class)));
        lvl6BTN.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Lvl6.class)));
        lvl6v2Btn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Lvl6v2.class)));
        lvl7BTN.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Lvl7.class)));
        lvl8BTN.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Lvl8.class)));
    }

    private boolean getFlag() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("dark", false);
    }
}
