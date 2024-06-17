package com.example.main;

public class Util {
    public static double clamp(double value, double min, double max) {
        if (value >= max) {
            return max;
        } else return Math.max(value, min);
    }
}
