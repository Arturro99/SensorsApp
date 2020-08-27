package com.g09.levels;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.SensorEvent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.g09.R;


public class Lvl7 extends Level {

    MediaPlayer mediaPlayer;
    SharedPreferences sharedPreferences;
    TextView lvl7txt;
    Button lvl7btn;
    Long timerA;
    private BatteryReceiver batteryReceiver = new BatteryReceiver();
    private IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

    double a;

    @Override
    public void onCreate() {
        setContentView(R.layout.lvl7);
        ImageButton hint = findViewById(R.id.hint7);
        lvl7txt = findViewById(R.id.lvl7txt);
        lvl7btn = findViewById(R.id.lvl7BTN);
        lvl7btn.setVisibility(View.INVISIBLE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        timerA = SystemClock.elapsedRealtime();
        registerReceiver(batteryReceiver, intentFilter);

        a = startTimer();

        // Thread for playing the end theme
        Runnable r = () -> {


            winAlert("Gratulacje", "Udało Ci się ukończyć wszystkie poziomy", null);
        };

        lvl7btn.setOnClickListener(view -> {
            double b = stopTimer();
            unregisterReceiver(batteryReceiver);
//            winAlert("Gratulacje!", "Udało ci się przejść poziom!\nTwój czas: " + String.format("%.2f", (SystemClock.elapsedRealtime() - timerA)*0.001) + "sekund", Lvl8.class);
            winAlert("Gratulacje!", "Udało ci się przejść poziom!\nTwój czas: " + (float)calculateElapsedTime(a, b) + " sekund", Lvl8.class);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("stats7", (float)calculateElapsedTime(a, b));
            editor.apply();
            if(getScore("stats7") < getCurrentHighScore("stats7CurrentHS"))
                editor.putFloat("stats7CurrentHS", (float)calculateElapsedTime(a, b));
            editor.apply();
        });

        hint.setOnClickListener(view -> {
            showHint("Na początku było Słowo. (Genesis, 1, 1)");
            if (getFlagStart()) {
                Handler h = new Handler();
                h.postDelayed(r, 3000);
            }
        });
    }

    @Override
    protected void start() {

    }

    @Override
    protected void stop() {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

}
