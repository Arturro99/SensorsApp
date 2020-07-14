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

//Czujnik zbliżeniowy		Proximity sensor				Trzeba zbliżyć się do telefonu (czujnika)

public class Lvl5 extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mProximitySensor;
    int proximityValue;
    TextView lvl5txt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mSensorManager.unregisterListener(this,mProximitySensor);
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
        proximityValue = (int) event.values[0];
        String f = "";
        if(proximityValue == 0)
            f = "\nudalo sie";
        lvl5txt.setText(String.valueOf(proximityValue) + " cm" + f);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
