package com.example.main;

public class SimCursor {
    double x, y, minX, minY, maxX, maxY, oriX, oriY;
    float r;
    public SimCursor(double nx, double ny, double minX, double minY, double maxX, double maxY){
        this.x = nx;
        this.y = ny;
        this.oriX = x;
        this.oriY = y;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.r = 5;
    }
    public void move(double dx, double dy, double factor){
        this.x += dx*factor;
        this.y += dy*factor;

        this.x = Util.clamp(this.x, minX, maxX);
        this.y = Util.clamp(this.y, minY, maxY);
    }
    public void resetCursorPosition(){
        this.x = oriX;
        this.y = oriY;
    }
    public void setOriginY(double newVal){
        this.oriY = newVal;
    }
    public double getX(){
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public float getR() {
        return this.r;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
