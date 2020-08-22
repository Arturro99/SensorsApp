package com.g09.levels;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.g09.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

public class Lvl8 extends Level  {
    private SensorManager mSensorManager;
    private Sensor mLinearAcceleration;
    private Sensor mRotationV;
    TextView lvl8txt;
    TextView lvl8help;
    enum Dir {N, E, S, W};
    Dir direction = Dir.N;
    TextView txt_compass;
    boolean goodDirection = false;
    int mAzimuth;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private ArrayList<Float> mLastLinAcc = new ArrayList<Float>();
    int steps = 0;
    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl8);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        lvl8txt = findViewById(R.id.lvl8txt);
        ImageButton hint = findViewById(R.id.hint8);
        lvl8help = findViewById(R.id.lvl8he);

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHint("Nie dla psa.");
            }
        });
        pickDirection();
        start();
    }

    @Override
    protected void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null || mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) == null) {
            noSensorsAlert(null);
        }
        else {
            mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_UI);
            haveSensor2 = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void stop() {
        if(haveSensor && haveSensor2){
            mSensorManager.unregisterListener(this,mLinearAcceleration);
            mSensorManager.unregisterListener(this,mRotationV);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }
        mAzimuth = Math.round(mAzimuth);

        String where = "BLAD";
        if (direction.name().equals("N") && mAzimuth > 315 || mAzimuth <= 45)
            goodDirection = true;
        else if (direction.name().equals("W") && mAzimuth <= 315 && mAzimuth > 225)
            goodDirection = true;
        else if (direction.name().equals("S") && mAzimuth <= 225 && mAzimuth > 135)
            goodDirection = true;
        else if (direction.name().equals("E") && mAzimuth <= 135 && mAzimuth > 45)
            goodDirection = true;
        else
            goodDirection = false;

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            mLastLinAcc.add(event.values[1]);
            if(mLastLinAcc.size() > 11)
            {
                mLastLinAcc.remove(0);
                if(mLastLinAcc.get(0) < mLastLinAcc.get(5) &&
                        mLastLinAcc.get(1) < mLastLinAcc.get(5) &&
                        mLastLinAcc.get(2) < mLastLinAcc.get(5) &&
                        mLastLinAcc.get(3) < mLastLinAcc.get(5) &&
                        mLastLinAcc.get(4) < mLastLinAcc.get(5) &&
                        mLastLinAcc.get(6) < mLastLinAcc.get(5) &&
                        mLastLinAcc.get(7) < mLastLinAcc.get(5) &&
                        mLastLinAcc.get(8) < mLastLinAcc.get(5) &&
                        mLastLinAcc.get(9) < mLastLinAcc.get(5) &&
                        mLastLinAcc.get(10) < mLastLinAcc.get(5) &&
                        mLastLinAcc.get(5) > 1 && mLastLinAcc.get(5) < 2 && goodDirection) {
                    steps--;
                    //lvl8txt.append(String.valueOf(mLastLinAcc.get(5)) + " ");
                }

            }
        }

        lvl8help.setText(mAzimuth + "° " + where + " "  + steps);
        lvl8txt.setText(direction.name() + " " + steps + " kroków");

        if(steps == 0){
            stop();
            finish();
        }
    }
    @SuppressLint("SetTextI18n")
    void pickDirection() {
        int tmp = (int)(Math.random() * 4);
        if(tmp == 0)
            direction = Dir.N;
        else if(tmp == 1)
            direction = Dir.E;
        else if(tmp == 2)
            direction = Dir.S;
        else if(tmp == 3)
            direction = Dir.W;
        steps = (int)(Math.random() * 5) + 5;
        lvl8txt.setText(direction.name() + " " + steps + " kroków");
    }
}
