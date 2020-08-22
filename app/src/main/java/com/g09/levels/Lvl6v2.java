package com.g09.levels;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.g09.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

public class Lvl6v2 extends Level {

    private SensorManager mSensorManager;
    private Sensor mRotationSensor;
    private Sensor mLinearAccelerationSensor;

    private TextView time6v2;
    private TextView textView6;
    private TextView currentValue;
    private TextView achievedValue;

    private double[] startingValues = new double[3];
    boolean firstTimeChecked = false;
    boolean stepZeroDone = false;
    boolean firstStepDone = false;
    boolean secondStepDone = false;

    double a;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl6v2);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        time6v2 = findViewById(R.id.time6v2);
        textView6 = findViewById(R.id.textView6);
        currentValue = findViewById(R.id.currentValue);
        achievedValue = findViewById(R.id.achievedValue);
    }

    @Override
    protected void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null &&
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) == null) {
            noSensorsAlert();
        } else {
            mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            mLinearAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            mSensorManager.registerListener(this, mRotationSensor, SensorManager.SENSOR_DELAY_UI);
            //mSensorManager.registerListener(this, mLinearAccelerationSensor, SensorManager.SENSOR_DELAY_UI);
        }

        a = startTimer();

    }

    @Override
    protected void stop() {
        mSensorManager.unregisterListener(this, mLinearAccelerationSensor);
        mSensorManager.unregisterListener(this, mRotationSensor);
        if(mediaPlayer != null)
            mediaPlayer.stop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (!firstTimeChecked && sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            startingValues[0] = sensorEvent.values[0];
            startingValues[1] = sensorEvent.values[1];
            startingValues[2] = sensorEvent.values[2];
            firstTimeChecked = true;
        }

        if(!stepZeroDone) {
            if(stepZero(sensorEvent)) stepZeroDone = true;
        }
        if(!firstStepDone) {
            if (stepZeroDone && firstStep(sensorEvent)) firstStepDone = true;
        }
        if(!secondStepDone) {
            if (firstStepDone && secondStep(sensorEvent)) secondStepDone = true;
        }
        if(secondStepDone) thirdStep(sensorEvent);

        currentValue.setText(String.valueOf(sensorEvent.values[0]));

    }

    private boolean stepZero(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR && Math.abs(startingValues[2] - sensorEvent.values[2]) < 0.9 && Math.abs(startingValues[2] - sensorEvent.values[2]) > 0.8) {
            startingValues[0] = sensorEvent.values[0];
            mSensorManager.unregisterListener(this, mRotationSensor);
            textView6.setText(String.valueOf("Ludzki mózg nie jest przystosowany do tego typu bodźców, żałosne... Nie chce mi się na Ciebie patrzeć"));
            return true;
        }
        return false;
    }

    private boolean firstStep(SensorEvent sensorEvent) {
        mSensorManager.registerListener(this, mRotationSensor, SensorManager.SENSOR_DELAY_UI);
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR && Math.abs(startingValues[0] - sensorEvent.values[0]) > 0.9/*&& Math.abs(startingValues[2] - sensorEvent.values[2]) > 0.4*/) {
            startingValues[0] = sensorEvent.values[0];
            mSensorManager.unregisterListener(this, mRotationSensor);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ringtone);
            mediaPlayer.start();
            achievedValue.setText(String.valueOf(startingValues[0]));
            //textView6.setText(String.valueOf(startingValues[0]));
            return true;
        }
        return false;
    }

    private boolean secondStep(SensorEvent sensorEvent) {
        mSensorManager.registerListener(this, mRotationSensor, SensorManager.SENSOR_DELAY_UI);
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR &&
                Math.abs(startingValues[0] - sensorEvent.values[0]) > 0.9) {
            mediaPlayer.setVolume(10, 10);
            mSensorManager.unregisterListener(this, mRotationSensor);
            textView6.setText(String.valueOf("Szybko, odbierz telefon!!"));
            //display someone ringing/answer quickly
            return true;
        }
        return false;
    }

    private void thirdStep(SensorEvent sensorEvent) {
        mSensorManager.registerListener(this, mLinearAccelerationSensor, SensorManager.SENSOR_DELAY_UI);
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && sensorEvent.values[2] >= 20) {
            textView6.setText(String.valueOf(sensorEvent.values[2]));
            double b = stopTimer();
            stop();
            winAlert("Gratulacje", "\nTwój czas: " + (float)calculateElapsedTime(a, b) + " sekund", Lvl7.class);
        } else if(sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && sensorEvent.values[2] < 20 && sensorEvent.values[2] > 10) {
            stop();
            winAlert("Porażka", "To nie była szybka reakcja...", Lvl6v2.class);
        }
    }
}