package com.g09.levels;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.g09.R;
import com.g09.Virus;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Lvl2 extends Level {

    //Stuff for sensors
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    double x,y,z;

    //Frame
    private FrameLayout gameLayout;

    //Image
    private ImageView man;
    private ImageView coronaImg1;
    private ImageView coronaImg2;
    private ImageView coronaImg3;
    private ArrayList<ImageView>listOfVirusesImg = new ArrayList<ImageView>();

    //Position
    private float manX, manY;
    private float[] coronaX = new float[3];
    private float[] coronaY = new float[3];

    //Centres
    private double manCenterX;
    private double manCenterY;
    private double[] coronaCenterX = new double[3];
    private double[] coronaCenterY = new double[3];

    TextView timeTxt;
    Timer timer = new Timer();

    private Virus corona1 = new Virus("corona1");
    private Virus corona2 = new Virus("corona2");
    private Virus corona3 = new Virus("corona3");
    private ArrayList<Virus>listOfViruses = new ArrayList<>();


    @Override
    protected void onCreate() {
        setContentView(R.layout.lvl2);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        timeTxt = findViewById(R.id.time2);
        ImageButton hint = findViewById(R.id.hint2);
        gameLayout = findViewById(R.id.frameLayout);

        man = findViewById(R.id.man);
        coronaImg1 = findViewById(R.id.corona1);
        coronaImg2 = findViewById(R.id.corona2);
        coronaImg3 = findViewById(R.id.corona3);

        listOfVirusesImg.add(coronaImg1);
        listOfVirusesImg.add(coronaImg2);
        listOfVirusesImg.add(coronaImg3);

        listOfViruses.add(corona1);
        listOfViruses.add(corona2);
        listOfViruses.add(corona3);

        hint.setOnClickListener(view -> showHint("Przetrwaj 10 sekund w morderczej walce z koronawirusem."));

        start();
    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) {
            noSensorsAlert(Lvl3.class);
        } else {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }

        //Wątek obsługujący ruch wirusów
        final android.os.Handler handler = new android.os.Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    moveVirus();
                    for(int i = 0; i < listOfVirusesImg.size(); i++) {
                        coronaCenterY[i] = coronaY[i] + listOfVirusesImg.get(i).getHeight() / 2.0;
                        coronaCenterX[i] = coronaX[i] + listOfVirusesImg.get(i).getWidth() / 2.0;
                    }
                    manCenterX = manX + man.getWidth() / 2.0;
                    manCenterY = manY + man.getHeight() / 2.0;

                    if (checkInfected() && timer != null) {
                        stop();
                        winAlert("Ooops", "Zostałeś zakażony", Lvl2.class);
                    }
                });
            }
        }, 0, 10);

        final int[] i = {0};
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    timeTxt.setText(String.valueOf(i[0]));
                    i[0]++;

                    if(i[0] == 11 && timer != null) {
                        stop();
                        winAlert("Gratulacje", "Udało Ci się przetrwać", Lvl3.class);
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

        if(man.getX() - a + man.getWidth() <= gameLayout.getWidth() && man.getX() - a >= 0) {
            manX -= a;
            man.setX(manX);
        }
        if(man.getY() + b + man.getHeight() <= gameLayout.getHeight() && man.getY() + b >= 0) {
            manY += b;
            man.setY(manY);
        }
    }

    private void moveVirus() {
        for(int i = 0; i < listOfVirusesImg.size(); i++) {
            coronaY[i] = listOfVirusesImg.get(i).getY();
            coronaX[i] = listOfVirusesImg.get(i).getX();
        }

        for(int i = 0; i < listOfViruses.size(); i++) {
            if(listOfVirusesImg.get(i).getY() + listOfViruses.get(i).getSpeedY() + listOfVirusesImg.get(i).getHeight() >= gameLayout.getHeight())
                listOfViruses.get(i).setGoingForwardY(false);
            else if(listOfVirusesImg.get(i).getY() + listOfViruses.get(i).getSpeedY() <= 0)
                listOfViruses.get(i).setGoingForwardY(true);

            if(listOfVirusesImg.get(i).getX() + listOfViruses.get(i).getSpeedX() + listOfVirusesImg.get(i).getWidth() >= gameLayout.getWidth())
                listOfViruses.get(i).setGoingForwardX(false);
            else if(listOfVirusesImg.get(i).getX() + listOfViruses.get(i).getSpeedX() <= 0)
                listOfViruses.get(i).setGoingForwardX(true);
        }

        for(int i = 0; i < listOfViruses.size(); i++) {
            if (listOfViruses.get(i).isGoingForwardY())
                coronaY[i] += listOfViruses.get(i).getSpeedY();
            else
                coronaY[i] -= listOfViruses.get(i).getSpeedY();

            if (listOfViruses.get(i).isGoingForwardX())
                coronaX[i] += listOfViruses.get(i).getSpeedX();
            else
                coronaX[i] -= listOfViruses.get(i).getSpeedX();
        }

        for(int i = 0; i < listOfVirusesImg.size(); i++) {
            listOfVirusesImg.get(i).setY(coronaY[i]);
            listOfVirusesImg.get(i).setX(coronaX[i]);
        }
    }

    private boolean checkInfected() {
        for(int i = 0; i < coronaCenterY.length; i++) {
            if (Math.abs(manCenterX - coronaCenterX[i]) <= 50 &&
                    Math.abs(manCenterY - coronaCenterY[i]) <= 50)
                return true;
        }
        return false;
    }
}
