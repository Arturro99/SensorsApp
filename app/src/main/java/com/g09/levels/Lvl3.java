package com.g09.levels;


import android.annotation.SuppressLint;
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

//Magnetometr			AK09911 3-axis Magnetic field sensor		Góra telefonu ma wskazywać północ

public class Lvl3 extends AppCompatActivity implements SensorEventListener {

    TextView txt_compass;
    int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mMagnetometer;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lvl3);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        txt_compass = (TextView) findViewById(R.id.txt_azimuth);

        start();
    }

    public void start() {
        if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
            noSensorsAlert();
        }
        else {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
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
        mSensorManager.unregisterListener(this,mAccelerometer);
        mSensorManager.unregisterListener(this,mMagnetometer);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
        }
        SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
        SensorManager.getOrientation(rMat, orientation);
        mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;


        mAzimuth = Math.round(mAzimuth);


        String where = "NW";

        if (mAzimuth >= 350 || mAzimuth <= 10) {
            where = "N";
            if(mLastAccelerometer[2] < -8) { //wartosc mLastAccelerometer[2] musi być mniejsza niż -8 (przyspieszenie ziemskie)
                where += " \nudalo sie";
                //Tu metoda, ktora konczy level
            }
        }
        if (mAzimuth < 350 && mAzimuth > 280)
            where = "NW";
        if (mAzimuth <= 280 && mAzimuth > 260)
            where = "W";
        if (mAzimuth <= 260 && mAzimuth > 190)
            where = "SW";
        if (mAzimuth <= 190 && mAzimuth > 170)
            where = "S";
        if (mAzimuth <= 170 && mAzimuth > 100)
            where = "SE";
        if (mAzimuth <= 100 && mAzimuth > 80)
            where = "E";
        if (mAzimuth <= 80 && mAzimuth > 10)
            where = "NE";


        txt_compass.setText(mAzimuth + "° " + where);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
