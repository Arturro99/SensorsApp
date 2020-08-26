package com.g09.levels;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.SensorEvent;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.g09.R;

// Battery receiver							    Celem poziomu jest opodłączenie urządzenia do
//										        dowolnego źródła prądu.

public class Lvl7 extends Level {

    MediaPlayer mediaPlayer;
    SharedPreferences sharedPreferences;
    TextView lvl7txt;
    private BatteryReceiver batteryReceiver = new BatteryReceiver();
    private IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

    @Override
    public void onCreate() {
        setContentView(R.layout.lvl7);
        ImageButton hint = findViewById(R.id.hint7);
        lvl7txt = findViewById(R.id.lvl7txt);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());



        // Thread for playing the end theme
        Runnable r = new Runnable() {
            @Override
            public void run() {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.the_end_theme);
                mediaPlayer.start();


                winAlert("Gratulacje", "Udało Ci się ukończyć wszystkie poziomy", null);
            }
        };

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHint("Na początku było Słowo. (Genesis, 1, 1)");
                if (getFlagStart()) {
                    Handler h = new Handler();
                    h.postDelayed(r, 3000);
                }
            }
        });
    }

    @Override
    protected void start() {


        registerReceiver(batteryReceiver, intentFilter);
    }

    @Override
    protected void stop() {
        unregisterReceiver(batteryReceiver);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

}
