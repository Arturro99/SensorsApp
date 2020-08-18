package com.g09.levels;

import android.hardware.SensorEvent;
import android.view.View;
import android.widget.ImageButton;

import com.g09.R;
//Oczyt baterii									Odczytujemy prędkość ładowania baterii i gracz musi
//										        obliczyć za ile minut bateria powinna sie załadować
//										        (będzie miał 4 odpowiedzi do wyboru)

public class Lvl7 extends Level {
//    public Lvl7(MyCallBack callBack) {
//        super(callBack);
//    }

    @Override
    public void onCreate() {
        setContentView(R.layout.lvl7);

        ImageButton hint = findViewById(R.id.hint7);

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHint("Na początku było Słowo. (Genesis, 1, 1)");
            }
        });
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }
}
