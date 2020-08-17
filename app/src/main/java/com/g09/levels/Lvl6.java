package com.g09.levels;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.g09.R;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

//Przyspieszenie liniowe		iNemo Linear Acceleration sensor		Telefon musi się znaleźc w spadku swobodnym
//+wektor rotacji
//I Believe I Can Fly

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

    final Uri URI = Uri.fromFile(new File("android.resource://com.g09/raw/ouch.mp3"));
    MediaPlayer mediaPlayer;

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

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHint("Czasem życie wymaga poświęceń.");
            }
        });

        start();
        a = startTimer();
    }

    public void start() {
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) == null)
            noSensorsAlert();
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
        mSensorManager.unregisterListener(this, mLinearAcceleration);
        mSensorManager.unregisterListener(this, mPressure);
        timer.cancel();
        timer.purge();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE) {
            if (!done) {
                initialPressureValue = (float) sensorEvent.values[0];
                help.setText(String.valueOf(initialPressureValue));
                done = true;
            }
            pressureValue = (float) sensorEvent.values[0];
            pressureValue = Math.round(pressureValue * 100) / 100f;
            if(pressureValue < initialPressureValue)
                initialPressureValue = pressureValue;
        }
        else if(sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            linearAccelerationValue = (float) sensorEvent.values[2];
            linearAccelerationValue = Math.round(linearAccelerationValue * 100) / 100f;
        }
        if(linearAccelerationValue >= 9) {
            mSensorManager.unregisterListener(this, mLinearAcceleration);
            maxLinearAcc.setText(String.valueOf(linearAccelerationValue) + "m/s^2");
            enoughAcceleration = true;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    help.setText(String.valueOf(sensorEvent.values[0]) + sensorEvent.sensor.getName());
                    if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE && sensorEvent.values[0] - initialPressureValue >= 0.08) {
                        enoughPressure = true;
                    }
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 500);

            if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE && sensorEvent.values[0] - initialPressureValue >= 0.02) {
                mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_UI);
            }
        }
        lvl6txt.setText(String.valueOf(pressureValue) + "hPa ");

        if(enoughAcceleration && enoughPressure) {
            onPause();
            try {
                b = stopTimer();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ouch);
                mediaPlayer.start();
            } finally {

            }
            winAlert("Gratulacje!", "...and belief is all what's left.\nTwój czas: " + calculateElapsedTime(a, b) + " sekund\");");
            stop();
        }
    }

}
