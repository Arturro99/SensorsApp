package com.g09;

public class Virus {
    long speedX;
    long speedY;
    String id;
    boolean goingForwardY = true;
    boolean goingForwardX = true;

    public Virus(String id) {
        this.id = id;
        this.speedX = Math.round((Math.random()*10) + 1);
        this.speedY = Math.round((Math.random()*20) + 1 );
    }

    public long getSpeedX() {
        return speedX;
    }

    public long getSpeedY() {
        return speedY;
    }

    public boolean isGoingForwardY() {
        return goingForwardY;
    }

    public boolean isGoingForwardX() {
        return goingForwardX;
    }

    public void setGoingForwardY(boolean goingForwardY) {
        this.goingForwardY = goingForwardY;
    }

    public void setGoingForwardX(boolean goingForwardX) {
        this.goingForwardX = goingForwardX;
    }
}
