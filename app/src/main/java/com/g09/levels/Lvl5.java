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

//Czujnik zbliżeniowy		Proximity sensor				Trzeba zbliżyć się do telefonu (czujnika)

public class Lvl5 extends Level {
    private SensorManager mSensorManager;
    private Sensor mProximitySensor;
    int proximityValue;
    TextView lvl5txt;
    TextView timeTxt;

    Timer timer = new Timer();
    Handler handler = new Handler();

    SharedPreferences sharedPreferences;

    double a;

    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl5);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lvl5txt = (TextView)findViewById(R.id.lvl5txt);
        timeTxt = findViewById(R.id.time5);
        ImageButton hint = findViewById(R.id.hint5);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHint("Nawet ja potrzebuję trochę czułości.");
            }
        });

        start();
        a = startTimer();
    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null) {
            noSensorsAlert();
        }
        else {
            mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mSensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_UI);
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


    public void stop() {
        mSensorManager.unregisterListener(this,mProximitySensor);
        timer.cancel();
        timer.purge();
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        proximityValue = (int) event.values[0];
        String f = "";
        if(proximityValue == 0) {
            double b = stopTimer();
            f = "\nudalo sie";
            winAlert("Gratulacje!", "Udalo Ci się przejść poziom!\nTwój czas: " + calculateElapsedTime(a, b) + " sekund", Lvl6.class);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("stats5", (float)calculateElapsedTime(a, b));
            editor.apply();
            if(getScore("stats5") < getCurrentHighScore("stats5CurrentHS"))
                editor.putFloat("stats5CurrentHS", (float)calculateElapsedTime(a, b));
            editor.apply();

            stop();
        }
        lvl5txt.setText(String.valueOf(proximityValue) + " cm" + f);
    }
}
