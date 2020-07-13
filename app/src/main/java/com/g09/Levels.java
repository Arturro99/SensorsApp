package com.g09;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.g09.levels.Lvl3;

import javax.xml.datatype.Duration;

public class Levels extends AppCompatActivity {
    private static Boolean[] levelComplete = new Boolean[7];
    Duration[] levelTime = new Duration[7];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levels);
        Button lvl3BTN = (Button)findViewById(R.id.lvl3BTN);
//        LinearLayout lin = (LinearLayout)findViewById(R.id.linear);
//        Button lvl1 = lin.findViewWithTag("lvl1");
//        lvl1.setText("aaa");
        lvl3BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Lvl3.class));
            }
        });
    }
    static public void completeLevel(int level) {
        levelComplete[level-1] = true;
    }

}
