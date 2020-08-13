package com.g09.levels;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.widget.TextView;

import com.g09.R;

import java.io.File;

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
    TextView linearAcc;
    boolean done = false;
    float initialPressureValue = 0;

    boolean enoughAcceleration = false;
    boolean enoughPressure = false;

    final Uri URI = Uri.fromFile(new File("android.resource://com.g09/raw/ouch.mp3"));
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl6);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        lvl6txt = findViewById(R.id.lvl6txt);
        help = findViewById(R.id.help);
        maxLinearAcc = findViewById(R.id.maxLinearAcc);

        start();
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

    }
    public void stop() {
        mSensorManager.unregisterListener(this, mLinearAcceleration);
        mSensorManager.unregisterListener(this, mPressure);
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
        }
        else if(sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            linearAccelerationValue = (float) sensorEvent.values[2];
            linearAccelerationValue = Math.round(linearAccelerationValue * 100) / 100f;
        }
        if(linearAccelerationValue >= 9) {
            maxLinearAcc.setText(String.valueOf(linearAccelerationValue) + "m/s^2");
            enoughAcceleration = true;
            mSensorManager.unregisterListener(this, mLinearAcceleration);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE && sensorEvent.values[0] - initialPressureValue >= 0.08) {
                        help.setText(String.valueOf(pressureValue - initialPressureValue));
                        enoughPressure = true;
                    }
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 500);
        }
        lvl6txt.setText(String.valueOf(pressureValue) + "hPa ");
        mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_UI);

        if(enoughAcceleration && enoughPressure) {
            onPause();
            try {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ouch);
                mediaPlayer.start();
            } finally {

            }
            winAlert("Gratulację!", "...and belief is all what's left.");
        }
    }

}
