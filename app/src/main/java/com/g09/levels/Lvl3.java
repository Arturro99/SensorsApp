package com.g09.levels;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.g09.R;

import java.util.Timer;
import java.util.TimerTask;

//Magnetometr			AK09911 3-axis Magnetic field sensor		Góra telefonu ma wskazywać północ

public class Lvl3 extends Level {

    TextView txt_compass;
    int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mMagnetometer;
    private TextView timeTxt;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];

    double a;

    Timer timer = new Timer();
    Handler handler = new Handler();

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl3);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        txt_compass = (TextView) findViewById(R.id.txt_azimuth);
        timeTxt = findViewById(R.id.time3);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        if(getFlag()){
            ImageView lvl3img = (ImageView) findViewById(R.id.lvl3img);
            lvl3img.setImageResource(R.drawable.lvl3imgwhite);
        }
        ImageButton hint = findViewById(R.id.hint3);

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHint("Czy twórca poziomu zaliczył sprawdzian z kierunków świata?");
            }
        });
        start();
        a = startTimer();
    }

    public void start() {
        if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
            noSensorsAlert(Lvl4.class);
        }
        else {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
        }

        if(getFlagTime()) {
            final int[] i = {0};
            timeTxt.setVisibility(View.VISIBLE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            timeTxt.setText(String.valueOf(i[0]));
                            i[0]++;
                        }
                    });
                }
            }, 0, 1000);
        }
    }


    public void stop() {
        mSensorManager.unregisterListener(this,mAccelerometer);
        mSensorManager.unregisterListener(this,mMagnetometer);
        timer.cancel();
        timer.purge();
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
                double b = stopTimer();
                where += " \nudalo sie";
                //Tu metoda, ktora konczy level
                winAlert("Gratulacje!", "Udalo Ci się przejść poziom!\nTwój czas: " + calculateElapsedTime(a, b) + " sekund", Lvl4.class);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("stats3", (float)calculateElapsedTime(a, b));
                editor.apply();
                if(getScore("stats3") < getCurrentHighScore("stats3CurrentHS"))
                    editor.putFloat("stats3CurrentHS", (float)calculateElapsedTime(a, b));
                editor.apply();

                stop();
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
}
