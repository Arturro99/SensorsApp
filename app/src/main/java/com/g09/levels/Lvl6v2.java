package com.g09.levels;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.g09.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class Lvl6v2 extends Level {

    private SensorManager mSensorManager;
    private Sensor mRotationSensor;
    private Sensor mLinearAccelerationSensor;

    private TextView time6v2;
    private TextView textView6;

    private double[] startingValues = new double[3];
    boolean firstTimeChecked = false;

    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl6v2);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        time6v2 = findViewById(R.id.time6v2);
        textView6 = findViewById(R.id.textView6);
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


    }

    @Override
    protected void stop() {
        mSensorManager.unregisterListener(this, mLinearAccelerationSensor);
        mSensorManager.unregisterListener(this, mRotationSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (!firstTimeChecked && sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            startingValues[0] = sensorEvent.values[0];
            startingValues[1] = sensorEvent.values[1];
            startingValues[2] = sensorEvent.values[2];
            firstTimeChecked = true;
        }

        //FRUSTRATED


        //RINGTONE


        //ANSWER QUICKLY
    }

    private boolean firstStep(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR && Math.abs(startingValues[0] - sensorEvent.values[0]) > 0.9 && Math.abs(startingValues[2] - sensorEvent.values[2]) > 0.6) {
            mSensorManager.unregisterListener(this, mRotationSensor);
            textView6.setText(String.valueOf("1st checkpoint, now turn phone around..."));
            mSensorManager.registerListener(this, mRotationSensor, SensorManager.SENSOR_DELAY_UI);
            startingValues[0] = sensorEvent.values[0];
            textView6.setText(String.valueOf(startingValues[0]));
            return true;
        }
        return false;
    }

    private boolean secondStep(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR &&
                Math.abs(startingValues[0] - sensorEvent.values[0]) > 0.9) {
            mSensorManager.unregisterListener(this, mRotationSensor);
            textView6.setText(String.valueOf("2nd checkpoint, now answer quickly..."));
            mSensorManager.registerListener(this, mLinearAccelerationSensor, SensorManager.SENSOR_DELAY_UI);
            //display someone ringing/answer quickly
            return true;
        }
        return false;
    }

    private boolean thirdStep(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && sensorEvent.values[2] >= 15) {
            textView6.setText(String.valueOf(sensorEvent.values[2]));
            winAlert("X się udał", "J/W", null);
            stop();
            return true;
        } else {
            textView6.setText(String.valueOf(sensorEvent.sensor.getName()));
            mSensorManager.registerListener(this, mRotationSensor, SensorManager.SENSOR_DELAY_UI);
            return false;
        }
    }
}