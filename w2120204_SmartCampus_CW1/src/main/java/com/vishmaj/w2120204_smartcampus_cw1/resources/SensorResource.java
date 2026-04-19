package com.vishmaj.w2120204_smartcampus_cw1.resources;

import com.vishmaj.w2120204_smartcampus_cw1.database.Database;
import com.vishmaj.w2120204_smartcampus_cw1.*;

import java.util.ArrayList;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;


@Path("/sensors")
public class SensorResource
  {
    //database instance 
    private final Database database = Database.getInstance();
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor){
        //basic validation
        if(sensor ==null || sensor.getRoomId().isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Sensor Id and Room Id are required to create the sensor\"}").build();
        }
        
        //checking if the room exists
        Room room = database.getRooms().get(sensor.getRoomId());
        if(room== null){
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Room not found to add sensor\"}").build();
        }
        
        //create a list of sesnsor for the target room if not exist
        if(room.getSensorIds()==null){
            room.setSensorIds(new ArrayList<>());
        }
        
        //if the sensor is not there in the room already add it to the list
        if(!room.getSensorIds().contains(sensor.getId())){
            room.getSensorIds().add(sensor.getId());
        }
        
        return Response.status(Response.Status.CREATED).entity("{\"Success\":\"Successfully created Sensor\"\\n\"Sensor\":\""+sensor+"\"}").build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSensors(@QueryParam("type") String type) {
        Collection<Sensor> allSensors = database.getSensors().values();

        // 1. If the user didn't provide a '?type=' parameter, just return all of them
        if (type == null || type.trim().isEmpty()) {
            return Response.ok(allSensors).build();
        }

        // 2. If they DID provide a type, filter the collection
        List<Sensor> filteredSensors = new ArrayList<>();
        for (Sensor sensor : allSensors) {
            if (type.equalsIgnoreCase(sensor.getType())) {
                filteredSensors.add(sensor);
            }
        }

        return Response.ok(filteredSensors).build();
    }
    
    
    @GET
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorById(@PathParam("sensorId") String sensorId){
        
        //checking if the sensnor is nuull
        Sensor sensor = database.getSensors().get(sensorId);
        
        if(sensor==null){
            return Response.status(Response.Status.NOT_FOUND).entity("{\"Error\":\"Sensor not found\"}").build();
        }
        
        return Response.ok(sensor).build();
    }
    
    @GET
    @Path("/room/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSesnsorsByRoom(@PathParam("roomId") String roomId){
        //checking if the room exists
        if(!database.getRooms().containsKey(roomId)){
            return Response.status(Response.Status.NOT_FOUND).entity("{\"Error\":\"Room not found\"}").build();
        }
        
        //looping throuhg all the sensors and getting the sensors in the target room
        List<Sensor> roomSensors =  new ArrayList<>();
        for(Sensor sensor : database.getSensors().values()){
            if(sensor.getRoomId().equalsIgnoreCase(roomId)){
               roomSensors.add(sensor);
            }
        }
        //return the sensors in that room
        return  Response.ok(roomSensors).build();
        
    }
    
    //====SUB-RESOURSE===//
    @Path("/{sensorId}/readings")
    public SensorReadingResource handleReadingResource(){
        return new SensorReadingResource();
    }
  }
