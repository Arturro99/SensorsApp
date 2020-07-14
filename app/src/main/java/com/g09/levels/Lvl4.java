package com.g09.levels;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.g09.R;



//Czujnik światła			Light sensor					Trzeba zbliżyć/oddalić telefon do/od lampi

public class Lvl4 extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    int lightValue;
    TextView lvl4txt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lvl4);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lvl4txt = (TextView)findViewById(R.id.lvl4txt);

        start();
    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null) {
            noSensorsAlert();
        }
        else {
            mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void noSensorsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the sensors used in level.")
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void stop() {
        mSensorManager.unregisterListener(this,mLightSensor);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        lightValue = (int) event.values[0];
        String f = "";
        if(lightValue > 9000)
            f = "\nudalo sie";
        lvl4txt.setText(String.valueOf(lightValue) + " lux" + f);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
