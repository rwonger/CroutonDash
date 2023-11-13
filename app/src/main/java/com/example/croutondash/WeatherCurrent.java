package com.example.croutondash;

public class WeatherCurrent {
    private String summary;
    private String icon;
    private double temperature;
    private double precipProbability;

    public String getIcon() {
        return icon;
    }
    public double getTemperature() {
        return temperature;
    }

    public double getPrecipProbability() {
        return precipProbability;
    }

    public String getSummary() {
        return summary;
    }
}
