package com.vishmaj.w2120204_smartcampus_cw1.resources;


import com.vishmaj.w2120204_smartcampus_cw1.database.Database;
import com.vishmaj.w2120204_smartcampus_cw1.*;
import com.vishmaj.w2120204_smartcampus_cw1.exceptions.SensorUnavailableException;

import java.util.ArrayList;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

public class SensorReadingResource
  {
    private final Database database = Database.getInstance();
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response recordReading(@PathParam("sensorId") String sensorId ,SensorReading reading){
        //validaiton
        if(!database.getSensors().containsKey(sensorId)){
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Sensor not found\"}").build();
        }
        
        //creating the id if the user forgets to send the id
        if(reading.getId()== null || reading.getId().trim().isEmpty()){
            reading.setId(UUID.randomUUID().toString());
        }
        
        //inserting the current time if the time is not supplied
        if(reading.getTimeStamp()==0){
            reading.setTimeStamp(System.currentTimeMillis());
        }
        
        //adding the sensor reading to the databse 
        //adding a new empty list if this is the first reading for the sensor
        database.getSensorReadings().putIfAbsent(sensorId, new ArrayList<>());
        
        //adding the current sensor reading to the target sensor
        database.getSensorReadings().get(sensorId).add(reading);
        
        //Fetch the parent sensor and update its currentValue to match this new reading
        Sensor parentSensor = database.getSensors().get(sensorId);
        if (parentSensor != null && "MAINTENANCE".equalsIgnoreCase(parentSensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is currently in MAINTENANCE mode and cannot accept readings.");
        }
        //updateing the sensor
        parentSensor.setCurrentValue(reading.getValue());
        
        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorReadingsForSensor(@PathParam("sensorId") String sensorId){
        //verifing if the sensor exists
        if(!database.getSensors().containsKey(sensorId)){
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Sensor not Found\"}").build();
        }
        
        //get all the reading for the sensor
        List<SensorReading> sensorReadings = database.getSensorReadings().getOrDefault(sensorId, new ArrayList<>());
        
        return Response.ok(sensorReadings).build();
    }
        
  }
