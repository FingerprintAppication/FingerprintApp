package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScheduleModel {
    private Schedule schedule;
    Gson gson = new GsonBuilder().create();

    public ScheduleModel(){
        schedule = new Schedule();
    }

    public ScheduleModel(String jsonResponse){
        schedule = gson.fromJson(jsonResponse,Schedule.class);
    }

    public Schedule getSchedule(){
        return schedule;
    }

    public String toJSONString(){
        return gson.toJson(this.schedule);
    }

    public class Schedule implements Serializable {
        private long scheduleID;
        private int scheduleNumber;
        private String scheduleDate;
        private Time ddd;
        List<PostponeModel.Postpone> postponeList;
        PeriodModel.Period period;

        public Schedule() {
        }

        public Schedule(long scheduleID, int scheduleNumber, String scheduleDate, Time ddd) {
            this.scheduleID = scheduleID;
            this.scheduleNumber = scheduleNumber;
            this.scheduleDate = scheduleDate;
            this.ddd = ddd;
        }

        public List<PostponeModel.Postpone> getPostponeList() {
            return postponeList;
        }

        public void setPostponeList(List<PostponeModel.Postpone> postponeList) {
            this.postponeList = postponeList;
        }

        public PeriodModel.Period getPeriod() {
            return period;
        }

        public void setPeriod(PeriodModel.Period period) {
            this.period = period;
        }

        public long getScheduleID() {
            return scheduleID;
        }

        public void setScheduleID(long scheduleID) {
            this.scheduleID = scheduleID;
        }

        public int getScheduleNumber() {
            return scheduleNumber;
        }

        public void setScheduleNumber(int scheduleNumber) {
            this.scheduleNumber = scheduleNumber;
        }

        public String getScheduleDate() {
            return scheduleDate;
        }

        public void setScheduleDate(String scheduleDate) {
            this.scheduleDate = scheduleDate;
        }

        public Time getDdd() {
            return ddd;
        }

        public void setDdd(Time ddd) {
            this.ddd = ddd;
        }
    }
}
