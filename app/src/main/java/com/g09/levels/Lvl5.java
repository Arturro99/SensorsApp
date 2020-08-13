package com.g09.levels;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.g09.R;

//Czujnik zbliżeniowy		Proximity sensor				Trzeba zbliżyć się do telefonu (czujnika)

public class Lvl5 extends Level {
    private SensorManager mSensorManager;
    private Sensor mProximitySensor;
    int proximityValue;
    TextView lvl5txt;
    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl5);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lvl5txt = (TextView)findViewById(R.id.lvl5txt);

        start();
    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null) {
            noSensorsAlert();
        }
        else {
            mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mSensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_UI);
        }
    }


    public void stop() {
        mSensorManager.unregisterListener(this,mProximitySensor);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        proximityValue = (int) event.values[0];
        String f = "";
        if(proximityValue == 0) {
            f = "\nudalo sie";
            winAlert("Gratulację!", "Udalo Ci się przejść poziom!");
            onPause();
        }
        lvl5txt.setText(String.valueOf(proximityValue) + " cm" + f);
    }
}
