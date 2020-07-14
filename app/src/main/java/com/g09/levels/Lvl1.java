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

import org.w3c.dom.Text;

//Żyroskop			LSM330 Gyroscope sensor				Trzeba szybko zakrecić telefonem
//You spin me right round

public class Lvl1 extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mGyroscopeSensor;
    int gyroscopeValue;
    float max;
    TextView lvl1txt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lvl1);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lvl1txt = (TextView)findViewById(R.id.lvl1txt);

        start();
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
                max = 30;
            mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
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

    public void winAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Udalo Ci się przejść poziom!")
                .setCancelable(false)
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void stop() {
        mSensorManager.unregisterListener(this, mGyroscopeSensor);
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

        gyroscopeValue = (int) event.values[2];
        String f = "";
        if(gyroscopeValue < -max) {
            f = "\nudalo sie ";
            lvl1txt.setText(String.valueOf(gyroscopeValue) + f + String.valueOf(max));
            winAlert();
            onPause();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
