package com.g09.levels;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.g09.R;


//Żyroskop			LSM330 Gyroscope sensor				Trzeba szybko zakrecić telefonem
//You spin me right round

public class Lvl1 extends Level {
    private SensorManager mSensorManager;
    private Sensor mGyroscopeSensor;
    int gyroscopeValue;
    float max;
    TextView lvl1txt;

    double a;

    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl1);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lvl1txt = (TextView)findViewById(R.id.lvl1txt);
        ImageButton hint = findViewById(R.id.hint1);

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHint("Czynność powinna wzorować się na ruchu wskazówek zegara.");
            }
        });

        start();
        a = startTimer();
    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null) {
            noSensorsAlert();
        }
        else {
            mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            max = mGyroscopeSensor.getMaximumRange();
            if(max*0.9 < 30)
                max *= 0.9;
            else
                max = 10;
            mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop() {
        mSensorManager.unregisterListener(this, mGyroscopeSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        gyroscopeValue = (int) event.values[2];
        String f = "";
        if(gyroscopeValue < -max) {
            double b = stopTimer();
            f = "\nudalo sie ";
            lvl1txt.setText(String.valueOf(gyroscopeValue) + f + String.valueOf(max));
            winAlert("Gratulację!", "Udalo Ci się przejść poziom!\nTwój czas: " + calculateElapsedTime(a, b) + " sekund");
            onPause();
        }
    }
}
