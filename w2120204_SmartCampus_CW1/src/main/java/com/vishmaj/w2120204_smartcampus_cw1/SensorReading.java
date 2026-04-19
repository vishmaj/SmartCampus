package com.vishmaj.w2120204_smartcampus_cw1;

import java.util.*;

public class SensorReading {
    private String id;
    private long timeStamp;
    private double value;

    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public SensorReading(String id, long timeStamp, double value) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.value = value;
    }
    
    public SensorReading(){
    }
}
