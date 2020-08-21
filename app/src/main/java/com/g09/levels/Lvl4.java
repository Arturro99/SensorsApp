package com.g09.levels;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.g09.R;

import java.util.Timer;
import java.util.TimerTask;


//Czujnik światła			Light sensor					Trzeba zbliżyć/oddalić telefon do/od lampi

public class Lvl4 extends Level {
    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    int lightValue;
    TextView lvl4txt;
    TextView timeTxt;

    Timer timer = new Timer();
    Handler handler = new Handler();

    SharedPreferences sharedPreferences;

    double a;

    @Override
    public void onCreate() {
        setContentView(R.layout.lvl4);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lvl4txt = (TextView)findViewById(R.id.lvl4txt);
        timeTxt = findViewById(R.id.time4);
        ImageButton hint = findViewById(R.id.hint4);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHint("Warto poczytać o jednostkach pochodnych układu SI.");
            }
        });

        start();
        a = startTimer();
    }

    @Override
    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null) {
            noSensorsAlert();
        }
        else {
            mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_UI);
        }

        if(getFlagTime()) {
            final int[] i = {0};
            timeTxt.setVisibility(View.VISIBLE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            timeTxt.setText(String.valueOf(i[0]));
                            i[0]++;
                        }
                    });
                }
            }, 0, 1000);
        }
    }

    @Override
    public void stop() {
        mSensorManager.unregisterListener(this, mLightSensor);
        timer.cancel();
        timer.purge();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        lightValue = (int) sensorEvent.values[0];
        String f = "";
        if(lightValue > 3000) {
            double b = stopTimer();
            f = "\nudalo sie";
            winAlert("Gratulacje!", "Udalo Ci się przejść poziom!\nTwój czas: " + calculateElapsedTime(a, b) + " sekund", Lvl5.class);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("stats4", (float)calculateElapsedTime(a, b));
            editor.apply();
            if(getScore("stats4") < getCurrentHighScore("stats4CurrentHS"))
                editor.putFloat("stats4CurrentHS", (float)calculateElapsedTime(a, b));
            editor.apply();

            stop();
        }
        lvl4txt.setText(String.valueOf(lightValue) + " lux" + f);
    }
}
