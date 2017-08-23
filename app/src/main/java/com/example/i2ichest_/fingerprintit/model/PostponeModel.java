package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * Created by I2ichest_ on 7/6/2017.
 */

public class PostponeModel implements Serializable {
    private Postpone postpone;
    Gson gson = new GsonBuilder().create();

    public PostponeModel(){
        postpone = new Postpone();
    }

    public PostponeModel(String jsonResponse){
        postpone = gson.fromJson(jsonResponse,Postpone.class);
    }

    public Postpone getPostpone(){
        return postpone;
    }

    public String toJSONString(){
        return gson.toJson(this.postpone);
    }

    public class Postpone implements Serializable{
        private long postponeID;
        private String oldPostponeDate;
        private String postponeDate;
        private String postponeDetail;
        private String postponeStartTime;
        private String postponeEndTime;
        RoomModel.Room room;
        ///////////scheduleList

        public Postpone() {
        }

        public Postpone(long postponeID, String oldPostponeDate, String postponeDate, String postponeDetail, String postponeStartTime, String postponeEndTime) {
            this.postponeID = postponeID;
            this.oldPostponeDate = oldPostponeDate;
            this.postponeDate = postponeDate;
            this.postponeDetail = postponeDetail;
            this.postponeStartTime = postponeStartTime;
            this.postponeEndTime = postponeEndTime;
        }

        public RoomModel.Room getRoom() {
            return room;
        }

        public void setRoom(RoomModel.Room room) {
            this.room = room;
        }

        public long getPostponeID() {
            return postponeID;
        }

        public void setPostponeID(long postponeID) {
            this.postponeID = postponeID;
        }

        public String getOldPostponeDate() {
            return oldPostponeDate;
        }

        public void setOldPostponeDate(String oldPostponeDate) {
            this.oldPostponeDate = oldPostponeDate;
        }

        public String getPostponeDate() {
            return postponeDate;
        }

        public void setPostponeDate(String postponeDate) {
            this.postponeDate = postponeDate;
        }

        public String getPostponeDetail() {
            return postponeDetail;
        }

        public void setPostponeDetail(String postponeDetail) {
            this.postponeDetail = postponeDetail;
        }

        public String getPostponeStartTime() {
            return postponeStartTime;
        }

        public void setPostponeStartTime(String postponeStartTime) {
            this.postponeStartTime = postponeStartTime;
        }

        public String getPostponeEndTime() {
            return postponeEndTime;
        }

        public void setPostponeEndTime(String postponeEndTime) {
            this.postponeEndTime = postponeEndTime;
        }
    }
}
