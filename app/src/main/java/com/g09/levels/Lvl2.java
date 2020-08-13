package com.g09.levels;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.g09.R;

//Akcelerometr			LSM330 3-axis accelerometer			Trzeba telefon ustawić pod odpowiednim kątem

public class Lvl2 extends Level {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    double x,y,z;
    double a;
    TextView lvl2txt;
    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl2);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lvl2txt = (TextView)findViewById(R.id.lvl2txt);

        start();
        a = startTimer();
    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            noSensorsAlert();
        }
        else {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop() {
        mSensorManager.unregisterListener(this, mAccelerometer);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        x = sensorEvent.values[0];
        y = sensorEvent.values[1];
        z = sensorEvent.values[2];

        double pitch = Math.atan(x/Math.sqrt(Math.pow(y,2) + Math.pow(z,2)));
        double roll = Math.atan(y/Math.sqrt(Math.pow(x,2) + Math.pow(z,2)));
        //convert radians into degrees
        int a = (int) (pitch * (180.0/3.14));
        int b = (int) (roll * (180.0/3.14)) ;

        lvl2txt.setText(String.valueOf(a) + "°  " + String.valueOf(b) + "°");
    }

}
