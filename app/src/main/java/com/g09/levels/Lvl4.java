package com.g09.levels;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.g09.R;



//Czujnik światła			Light sensor					Trzeba zbliżyć/oddalić telefon do/od lampi

public class Lvl4 extends Level {
    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    int lightValue;
    TextView lvl4txt;

    @Override
    public void onCreate() {
        setContentView(R.layout.lvl4);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lvl4txt = (TextView)findViewById(R.id.lvl4txt);

        start();
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
    }

    @Override
    public void stop() {
        mSensorManager.unregisterListener(this, mLightSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        lightValue = (int) sensorEvent.values[0];
        String f = "";
        if(lightValue > 6000) {
            f = "\nudalo sie";
            winAlert("Gratulację!", "Udalo Ci się przejść poziom!");
            onPause();
        }
        lvl4txt.setText(String.valueOf(lightValue) + " lux" + f);
    }
}
