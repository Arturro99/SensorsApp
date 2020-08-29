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

import java.util.Timer;
import java.util.TimerTask;


public class Lvl7 extends Level {

    MediaPlayer mediaPlayer;
    SharedPreferences sharedPreferences;
    TextView lvl7txt;
    TextView timeTxt;
    Button lvl7btn;
    Long timerA;
    private BatteryReceiver batteryReceiver = new BatteryReceiver();
    private IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

    double a;

    Timer timer = new Timer();
    Handler handler = new Handler();

    @Override
    public void onCreate() {
        setContentView(R.layout.lvl7);
        ImageButton hint = findViewById(R.id.hint7);
        lvl7txt = findViewById(R.id.lvl7txt);
        timeTxt = findViewById(R.id.time7);
        lvl7btn = findViewById(R.id.lvl7BTN);
        lvl7btn.setVisibility(View.INVISIBLE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        timerA = SystemClock.elapsedRealtime();
        registerReceiver(batteryReceiver, intentFilter);

        a = startTimer();
        lvl7btn.setOnClickListener(view -> {
            double b = stopTimer();
            unregisterReceiver(batteryReceiver);
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
        });
    }

    @Override
    protected void start() {
        if(getFlagTime()) {
            final int[] i = {0};
            timeTxt.setVisibility(View.VISIBLE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> {
                        timeTxt.setText(String.valueOf(i[0]));
                        i[0]++;
                    });
                }
            }, 0, 1000);
        }
    }

    @Override
    protected void stop() {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

}
