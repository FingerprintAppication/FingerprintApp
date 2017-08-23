package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * Created by I2ichest_ on 7/6/2017.
 */

public class RoomModel implements Serializable{
    private Room room;
    Gson gson = new GsonBuilder().create();

    public RoomModel(){
        room = new Room();
    }

    public RoomModel(String jsonResponse){
        room = gson.fromJson(jsonResponse,Room.class);
    }

    public Room getRoom(){
        return room;
    }

    public String toJSONString(){
        return gson.toJson(this.room);
    }

    public class Room implements Serializable {
        private Long roomID;
        private String roomName;
        BuildingModel.Building building;

        public Room() {
        }

        public Room(Long roomID, String roomName) {
            this.roomID = roomID;
            this.roomName = roomName;
        }

        public BuildingModel.Building getBuilding() {
            return building;
        }

        public void setBuilding(BuildingModel.Building building) {
            this.building = building;
        }

        public Long getRoomID() {
            return roomID;
        }

        public void setRoomID(Long roomID) {
            this.roomID = roomID;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }
    }
}
