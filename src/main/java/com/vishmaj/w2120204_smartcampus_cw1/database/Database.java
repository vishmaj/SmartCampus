package com.vishmaj.w2120204_smartcampus_cw1.database;

import com.vishmaj.w2120204_smartcampus_cw1.SensorReading;
import com.vishmaj.w2120204_smartcampus_cw1.Room;
import com.vishmaj.w2120204_smartcampus_cw1.Sensor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;

public class Database {
    //creating the singleton instance
    private static final Database instance = new Database();

    //using thread sage collections instead of normal hashmaps
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    private final Map<String, List<SensorReading>> sensorReadings = new ConcurrentHashMap<>();

    private Database() {

    }


    public static Database getInstance() {
        return instance;
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public Map<String, Sensor> getSensors() {
        return sensors;
    }

    public Map<String, List<SensorReading>> getSensorReadings() {
        return sensorReadings;
    }
}
