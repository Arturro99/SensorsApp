package com.g09;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        Switch darkMode = (Switch)findViewById(R.id.darkMode);
        Switch showTime = (Switch)findViewById(R.id.showTime);
        Button credits = (Button)findViewById(R.id.credits);
        TextView options = findViewById(R.id.options);

        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                    options.setText("Czorny");
                else
                    options.setText("Biały");
//                    setTheme(R.style.LightTheme);
            }
        });

    }


}
