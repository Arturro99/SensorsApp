package com.g09.levels;

import android.hardware.SensorEvent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import com.g09.R;
//Oczyt baterii									Odczytujemy prędkość ładowania baterii i gracz musi
//										        obliczyć za ile minut bateria powinna sie załadować
//										        (będzie miał 4 odpowiedzi do wyboru)

public class Lvl7 extends Level {

    MediaPlayer mediaPlayer;
    @Override
    public void onCreate() {
        setContentView(R.layout.lvl7);

        ImageButton hint = findViewById(R.id.hint7);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.the_end_theme);
                mediaPlayer.start();
                winAlert("Gratulacje", "Udało Ci się ukończyć wszystkie poziomy", null);
            }
        };

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHint("Na początku było Słowo. (Genesis, 1, 1)");
                if (getFlagStart()) {
                    Handler h = new Handler();
                    h.postDelayed(r, 3000);
                }
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
