package com.g09.levels;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.g09.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

//Przyspieszenie liniowe		iNemo Linear Acceleration sensor		Telefon musi się znaleźc w spadku swobodnym
//+wektor rotacji
//I Believe I Can Fly

public class Lvl6 extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mPressure;
    private Sensor mLinearAcceleration;
    private float linearAccelerationValue;
    private float pressureValue;
    TextView lvl6txt;
    TextView help;
    TextView maxLinearAcc;
    TextView linearAcc;
    boolean done = false;
    float initialPressureValue = 0;

    boolean enoughAcceleration = false;
    boolean enoughPressure = false;

    final Uri URI = Uri.fromFile(new File("android.resource://com.g09/raw/ouch.mp3"));
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lvl6);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        lvl6txt = findViewById(R.id.lvl6txt);
        help = findViewById(R.id.help);
        maxLinearAcc = findViewById(R.id.maxLinearAcc);

        start();
    }

    public void start() {
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) == null ||
                mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) == null)
            noSensorsAlert();
        else {
            mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_UI);
            mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_UI);
        }

    }
    public void stop() {
        mSensorManager.unregisterListener(this, mLinearAcceleration);
        mSensorManager.unregisterListener(this, mPressure);
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

    public void noSensorsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the sensors used in this level.")
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE) {
            if (!done) {
                initialPressureValue = (float) sensorEvent.values[0];
                help.setText(String.valueOf(initialPressureValue));
                done = true;
            }
            pressureValue = (float) sensorEvent.values[0];
            pressureValue = Math.round(pressureValue * 100) / 100f;
        }
        else if(sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            linearAccelerationValue = (float) sensorEvent.values[2];
            linearAccelerationValue = Math.round(linearAccelerationValue * 100) / 100f;
        }
        if(linearAccelerationValue >= 5)
            maxLinearAcc.setText(String.valueOf(linearAccelerationValue));
        if(linearAccelerationValue >= 10) {
            maxLinearAcc.setText(String.valueOf(linearAccelerationValue) + "m/s^2");
            enoughAcceleration = true;
            if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE && sensorEvent.values[0] >= 0.08) {
                help.setText(String.valueOf(pressureValue - initialPressureValue));
                enoughPressure = true;
            }
        }
       lvl6txt.setText(String.valueOf(pressureValue) + "hPa ");

        if(enoughAcceleration && enoughPressure) {
            onPause();
            try {
                winAlert();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void winAlert() throws IOException {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ouch);
        mediaPlayer.start();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("...and belief is all what's left.")
                .setCancelable(false)
                .setTitle("Gratulacje")
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

}
