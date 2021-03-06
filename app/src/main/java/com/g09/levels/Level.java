package com.g09.levels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.g09.MainActivity;
import com.g09.R;

public abstract class Level extends AppCompatActivity implements SensorEventListener {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(getFlag() ? R.style.AppThemeDark : R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        onCreate();
    }

    protected abstract void onCreate();
    protected abstract void start();
    protected abstract void stop();

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        start();
    }

    @Override
    public abstract void onSensorChanged(SensorEvent sensorEvent);

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    protected void noSensorsAlert(Class cl) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the sensors used in level.")
                .setCancelable(false)
                .setNegativeButton("Close", (dialog, id) -> {
                    if(cl != null && getFlagStart())
                        startActivity(new Intent(getApplicationContext(), cl));
                });
        alertDialog.show();
    }

    protected void winAlert(String title, String message, Class cl) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setNegativeButton("Ok", (dialog, id) -> {
                    if(cl != null)
                        finish();
                    if(cl != null && getFlagStart())
                        startActivity(new Intent(getApplicationContext(), cl));
                });
        alertDialog.show();
    }

    protected void theEnd(MediaPlayer mediaPlayer, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message)
                .setTitle("Gratulacje")
                .setCancelable(false)
                .setNegativeButton("Ok", (dialog, id) -> {
                    finish();
                    mediaPlayer.stop();
                });
        alertDialog.show();
    }

    protected void showHint(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Ok",null);
        alertDialog.show();
    }

    protected boolean getFlag() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("dark", false);
    }

    protected boolean getFlagTime() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("time", false);
    }

    protected boolean getFlagStart() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("start", false);
    }

    protected float getCurrentHighScore(String statsNumberCurrentHighScore) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getFloat(statsNumberCurrentHighScore, 900f);
    }

    protected float getScore(String statsNumber) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getFloat(statsNumber, 0.0f);
    }

    protected double startTimer() {
        return System.currentTimeMillis();
    }

    protected double stopTimer() {
        return System.currentTimeMillis();
    }

    protected double calculateElapsedTime(double a, double b) {
        return Math.round((b - a)/10.0)/100.0;
//        throw new IllegalStateException (String.valueOf(b-a));
    }
}
