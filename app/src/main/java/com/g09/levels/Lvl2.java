package com.g09.levels;

import android.app.Activity;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.g09.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

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
    private ImageView corona;

    //Position
    private float manX, manY;
    private float coronaX, coronaY;

    //Size
    private double manCenterX;
    private double manCenterY;
    private double coronaCenterX;
    private double coronaCenterY;

    TextView timeTxt;

    boolean goingForwardY = true;
    boolean goingForwardX = true;
    Timer timer = new Timer();
    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl2);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //lvl2txt = (TextView)findViewById(R.id.lvl2txt);
        timeTxt = findViewById(R.id.time2);
        ImageButton hint = findViewById(R.id.hint2);
        gameLayout = findViewById(R.id.frameLayout);
        man = findViewById(R.id.man);
        corona = findViewById(R.id.virus);
        frameWidth = gameLayout.getWidth();
        frameHeight = gameLayout.getHeight();

        //coronaY = 1000;

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHint("Przetrwaj 20 sekund w morderczej walce z koronawirusem.");
            }
        });

        start();
    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            noSensorsAlert();
        } else {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }

        android.os.Handler handler = new android.os.Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        move();
                        coronaCenterY = coronaY + corona.getHeight() / 2.0;
                        coronaCenterX = coronaX + corona.getWidth() / 2.0;
                        manCenterX = manX + man.getWidth() / 2.0;
                        manCenterY = manY + man.getHeight() / 2.0;

                        if (checkInfected() && timer != null) {
                            stop();
                            winAlert("Ooops", "Zostałeś zakażony", Lvl2.class);
                        }
                    }
                });
            }
        }, 0, 10);

        final int[] i = {1};
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        timeTxt.setText(String.valueOf(i[0]));
                        i[0]++;

                        if(i[0] == 21 && timer != null) {
                            stop();
                            winAlert("Gratulacje", "Udało Ci się przetrwać", Lvl3.class);
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    public void stop() {
        mSensorManager.unregisterListener(this, mAccelerometer);
        timer.cancel();
        timer.purge();
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

        if(man.getX() - a + man.getWidth() <= gameLayout.getWidth() && man.getX() - a >= 0) {
            manX += a;
            man.setX(-manX);
        }
        if(man.getY() + b + man.getHeight() <= gameLayout.getHeight() && man.getY() + b >= 0) {
            manY += b;
            man.setY(manY);
        }

    }

    private void move() {
        long speedY = Math.round(Math.random()*15)+1;
        long speedX = Math.round(Math.random()*5)+1;

        if(corona.getY() >= gameLayout.getHeight()) goingForwardY = false;
        else if(corona.getY() <= 0) goingForwardY = true;

        if(corona.getX() >= gameLayout.getWidth()) goingForwardX = false;
        else if(corona.getX() <= 0) goingForwardX = true;

        if(goingForwardY)
            coronaY += speedY;
        else
            coronaY -= speedY;

        if(goingForwardX)
            coronaX += speedX;
        else
            coronaX -= speedX;

        corona.setY(coronaY);
        corona.setX(coronaX);
    }

    private boolean checkInfected() {
//        return (Math.abs(-manCenterX - coronaCenterX) <= 50 &&
//                Math.abs(manCenterY - coronaCenterY) <= 50);
        return false;
    }

    private void infectedAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Niestety, zostałeś zakażony...")
                .setTitle("Oooops")
                .setCancelable(false)
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            finish();
                    }
                });
        alertDialog.show();
    }

}
