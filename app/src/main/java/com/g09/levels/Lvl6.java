package com.g09.levels;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.g09.R;

import java.util.Timer;
import java.util.TimerTask;


public class Lvl6 extends Level {
    private SensorManager mSensorManager;
    private Sensor mPressure;
    private Sensor mLinearAcceleration;
    private float linearAccelerationValue;
    private float pressureValue;
    TextView lvl6txt;
    TextView help;
    TextView maxLinearAcc;
    TextView timeTxt;
    boolean done = false;
    float initialPressureValue = 0;

    Timer timer = new Timer();
    Handler handler = new Handler();

    boolean enoughAcceleration = false;
    boolean enoughPressure = false;

    MediaPlayer mediaPlayer;

    SharedPreferences sharedPreferences;

    double a, b;

    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl6);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        lvl6txt = findViewById(R.id.lvl6txt);
        help = findViewById(R.id.help);
        maxLinearAcc = findViewById(R.id.maxLinearAcc);
        timeTxt = findViewById(R.id.time6);
        ImageButton hint = findViewById(R.id.hint6);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        hint.setOnClickListener(view -> showHint("Czasem życie wymaga poświęceń."));

        start();
        a = startTimer();
    }

    public void start() {
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) == null)
            noSensorsAlert(Lvl7.class);
        else {
            mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_UI);
            mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_UI);
        }

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
    public void stop() {
        mSensorManager.unregisterListener(this, mLinearAcceleration);
        mSensorManager.unregisterListener(this, mPressure);
        timer.cancel();
        timer.purge();
    }


    @Override
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE) {
            if (!done) {
                initialPressureValue = sensorEvent.values[0];
                done = true;
            }
            pressureValue = sensorEvent.values[0];
            pressureValue = Math.round(pressureValue * 100) / 100f;
            if(pressureValue < initialPressureValue)
                initialPressureValue = pressureValue;
        }
        else if(sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            linearAccelerationValue = sensorEvent.values[2];
            linearAccelerationValue = Math.round(linearAccelerationValue * 100) / 100f;
        }
        if(linearAccelerationValue >= 9) {
            mSensorManager.unregisterListener(this, mLinearAcceleration);
            enoughAcceleration = true;
            Runnable r = () -> {
                if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE && sensorEvent.values[0] - initialPressureValue >= 0.08) {
                    enoughPressure = true;
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 500);

            if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE && sensorEvent.values[0] - initialPressureValue >= 0.02) {
                mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_UI);
            }
        }

        if(enoughAcceleration && enoughPressure) {
            onPause();
            b = stopTimer();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ouch);
            mediaPlayer.start();
            winAlert("Gratulacje!", "...and belief is all what's left.\nTwój czas: " + (float)calculateElapsedTime(a, b) + " sekund\");", Lvl7.class);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("stats6", (float)calculateElapsedTime(a, b));
            editor.apply();
            if(getScore("stats6") < getCurrentHighScore("stats6CurrentHS"))
                editor.putFloat("stats6CurrentHS", (float)calculateElapsedTime(a, b));
            editor.apply();

            stop();
        }
    }

}
