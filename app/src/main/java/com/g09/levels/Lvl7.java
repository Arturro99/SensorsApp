package com.g09.levels;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//Oczyt baterii									Odczytujemy prędkość ładowania baterii i gracz musi
//										        obliczyć za ile minut bateria powinna sie załadować
//										        (będzie miał 4 odpowiedzi do wyboru)

public class Lvl7 extends AppCompatActivity implements SensorEventListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
