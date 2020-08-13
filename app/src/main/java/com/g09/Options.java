package com.g09;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Options extends AppCompatActivity {

    Switch showTime;
    Button credits;
    TextView options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(getFlag() ? R.style.AppThemeDark : R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

         showTime = (Switch)findViewById(R.id.showTime);
         credits = (Button)findViewById(R.id.credits);
         options = findViewById(R.id.options);

        Switch darkMode = (Switch)findViewById(R.id.darkMode);
        darkMode.setChecked(getFlag());

        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                OperateOnXml operateOnXml = new OperateOnXml();
                if(compoundButton.isChecked()) {
                    saveFlag(true);
                    Intent intent = new Intent(Options.this, MainActivity.class);
                    startActivity(intent);
                    Intent intent2 = new Intent(Options.this, Options.class);
                    startActivity(intent2);
                    try {
                        operateOnXml.saveToXmlFile("config.xml", this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    saveFlag(false);
                    Intent intent = new Intent(Options.this, MainActivity.class);
                    startActivity(intent);
                    Intent intent2 = new Intent(Options.this, Options.class);
                    startActivity(intent2);
                    try {
                        operateOnXml.saveToXmlFile("config.xml", this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }
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
}
