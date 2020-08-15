package com.g09.levels;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.g09.R;

//Akcelerometr			LSM330 3-axis accelerometer			Trzeba telefon ustawić pod odpowiednim kątem

public class Lvl2 extends Level {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    double x,y,z;
    double a;
    //Frame
    private FrameLayout gameLayout;
    private int frameHeight, frameWidth, initialFrameWidth;

    //Image
    private ImageView man;

    //Position
    private float manX, manY;
    private float coronaX, coronaY;

    //Size
    private float manSize;
    private float coronaSize;

    TextView lvl2txt;
    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl2);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //lvl2txt = (TextView)findViewById(R.id.lvl2txt);
        ImageButton hint = findViewById(R.id.hint2);
        gameLayout = findViewById(R.id.frameLayout);
        man = findViewById(R.id.man);
        frameWidth = gameLayout.getWidth();
        frameHeight = gameLayout.getHeight();


        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHint("Na razie dupa z tym poziomem.");
            }
        });

        start();
        a = startTimer();
    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            noSensorsAlert();
        }
        else {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop() {
        mSensorManager.unregisterListener(this, mAccelerometer);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        x = sensorEvent.values[0];
        y = sensorEvent.values[1];
        z = sensorEvent.values[2];

        double pitch = Math.atan(x/Math.sqrt(Math.pow(y,2) + Math.pow(z,2)));
        double roll = Math.atan(y/Math.sqrt(Math.pow(x,2) + Math.pow(z,2)));
        //convert radians into degrees
        int a = (int) (pitch * (180.0/3.14));
        int b = (int) (roll * (180.0/3.14)) ;

        //lvl2txt.setText(String.valueOf(a) + "°  " + String.valueOf(b) + "°");
        manX += a;
        manY += b;

        if(manX != frameWidth && manX != 0)
            man.setX(-manX);
        if(manY != frameHeight && manY != 0)
            man.setY(manY);
    }

    private void move() {

    }

    private boolean checkInfected() {
        return coronaX + coronaSize/2 == manX + manSize/2 ||
                coronaX + coronaSize/2 == manX - manSize/2 ||
                coronaX - coronaSize/2 == manX + manSize/2 ||
                coronaX - coronaSize/2 == manX - manSize/2 ||
                coronaY + coronaSize/2 == manY + manSize/2 ||
                coronaY + coronaSize/2 == manY - manSize/2 ||
                coronaY - coronaSize/2 == manY + manSize/2 ||
                coronaY - coronaSize/2 == manY - manSize/2;
    }

}
