package com.vishmaj.w2120204_smartcampus_cw1.resources;

import com.vishmaj.w2120204_smartcampus_cw1.database.Database;
import com.vishmaj.w2120204_smartcampus_cw1.Room;

import java.util.Collection;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 *
 * @author vishma
 */
@Path("/rooms")
public class SensorRoomResource
  {

    //this will fetch the threadsafe instance of the custom databse
    private final Database database = Database.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms()
      {
        System.out.println("==== POST REQUEST RECEIVED ====");
        //This will retreive alll the rooms from the datanase concurent hash map
        Collection<Room> rooms = database.getRooms().values();
        return Response.ok(rooms).build();
      }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room)
      {
        //basic valudation to see if th eroom is null or empty
        if (room.getId() == null || room.getId().trim().isEmpty())
          {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Room ID cannot be null or empty\"}").build();
          }

        //update the thread sage map made for rooms in the datasbe
        database.getRooms().put(room.getId(), room);

        //returns 201 created response , 
        return Response.status(Response.Status.CREATED).entity(room).build();
      }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId)
      {
        Room room = database.getRooms().get(roomId);

        if (room == null)
          {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Room not found\"}").build();
          }
        return Response.ok(room).build();
      }
    
    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId){
        
        Room room = database.getRooms().get(roomId);
        
        if(room==null){
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Room not found\"}").build();
        }
        
        //cannot delete room if it has active sensors
        if(room.getSensorIds() != null && !room.getSensorIds().isEmpty()){
            return Response.status(Response.Status.CONFLICT).entity("{\"error\":\"Cannot delete room, room cantains active sensors\"}").build();
        }
        //deleting the room if conditon is false
        database.getRooms().remove(roomId);
        
        //204 : which means succesfully deleted
        return Response.noContent().build();
    }
    
    
  }
