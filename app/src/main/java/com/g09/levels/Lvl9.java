package com.g09.levels;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.g09.MainActivity;
import com.g09.R;

public class Lvl9 extends Level {

    private SensorManager mSensorManager;
    private Sensor mGravitySensor;
    private Sensor mLinearAccelerationSensor;

    private TextView textView6;
    private ImageButton hint6v2;

    private double[] startingValues = new double[3];
    boolean firstTimeChecked = false;
    boolean stepZeroDone = false;
    boolean firstStepDone = false;
    boolean secondStepDone = false;

    double a, b;

    MediaPlayer mediaPlayer;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl9);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        textView6 = findViewById(R.id.textView6);
        hint6v2 = findViewById(R.id.hint6v2);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        hint6v2.setOnClickListener(view -> showHint("Po prostu wczytaj się w kaprysy telefonu."));

        a = startTimer();
        start();
    }

    @Override
    protected void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) == null &&
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) == null) {
            noSensorsAlert(null);
        } else {
            mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            mLinearAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_UI);
        }

    }

    @Override
    protected void stop() {
        mSensorManager.unregisterListener(this, mLinearAccelerationSensor);
        mSensorManager.unregisterListener(this, mGravitySensor);
        if(mediaPlayer != null)
            mediaPlayer.stop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (!firstTimeChecked && sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
            startingValues[0] = sensorEvent.values[0];
            startingValues[1] = sensorEvent.values[1];
            startingValues[2] = sensorEvent.values[2];
            firstTimeChecked = true;
        }

        if(!stepZeroDone) {
            if(stepZero(sensorEvent)) stepZeroDone = true;
        }
        if(!firstStepDone) {
            if (stepZeroDone && firstStep(sensorEvent)) firstStepDone = true;
        }
        if(!secondStepDone) {
            if (firstStepDone && secondStep(sensorEvent)) secondStepDone = true;
        }
        if(secondStepDone) thirdStep(sensorEvent);
    }

    private boolean stepZero(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY && sensorEvent.values[1] > 9 && sensorEvent.values[2] < 1) {
            startingValues[0] = sensorEvent.values[0];
            mSensorManager.unregisterListener(this, mGravitySensor);
            textView6.setText("Jestem zmęczony, daj mi odpocząć.");
            return true;
        }
        return false;
    }

    private boolean firstStep(SensorEvent sensorEvent) {
        mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_UI);
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY && Math.abs(sensorEvent.values[1]) < 1 && sensorEvent.values[2] < -9) {
            mSensorManager.unregisterListener(this, mGravitySensor);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ringtone);
            mediaPlayer.start();
            return true;
        }
        return false;
    }

    private boolean secondStep(SensorEvent sensorEvent) {
        mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_UI);
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY && sensorEvent.values[2] > 9) {
            mediaPlayer.setVolume(10, 10);
            mSensorManager.unregisterListener(this, mGravitySensor);
            textView6.setText("Szybko, odbierz telefon!!");
            return true;
        }
        return false;
    }

    private void thirdStep(SensorEvent sensorEvent) {
        mSensorManager.registerListener(this, mLinearAccelerationSensor, SensorManager.SENSOR_DELAY_UI);
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && sensorEvent.values[2] >= 20) {
            mSensorManager.unregisterListener(this, mLinearAccelerationSensor);
            b = stopTimer();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("stats9", (float)calculateElapsedTime(a, b));
            editor.apply();
            if(getScore("stats9") < getCurrentHighScore("stats9CurrentHS"))
                editor.putFloat("stats9CurrentHS", (float)calculateElapsedTime(a, b));
            editor.apply();

            stop();
            if(!getFlagStart())
                winAlert("Gratulacje", "\nTwój czas: " + (float) calculateElapsedTime(a, b) + " sekund", MainActivity.class);
            else {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.the_end_theme);
                mediaPlayer.start();
                theEnd(mediaPlayer, String.valueOf("Twój czas: " + (float)calculateElapsedTime(a, b) + " sekund\nUdało Ci się ukończyć grę!"));
            }

        } else if(sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && sensorEvent.values[2] < 20 && sensorEvent.values[2] > 10) {
            winAlert("Porażka", "To nie była szybka reakcja...", Lvl9.class);

            stop();
        }
    }
}