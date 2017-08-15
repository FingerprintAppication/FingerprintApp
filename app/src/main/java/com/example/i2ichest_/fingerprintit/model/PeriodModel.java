package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.List;

public class PeriodModel {
    private Period period;
    Gson gson = new GsonBuilder().create();

    public PeriodModel(){
        period = new Period();
    }

    public PeriodModel(String jsonResponse){
        period = gson.fromJson(jsonResponse,Period.class);
    }

    public Period getPeriod(){
        return period;
    }

    public String toJSONString(){
        return gson.toJson(this.period);
    }

    public class Period implements Serializable {
        private long periodID;
        private String dayOfWeek;
        private String studyType;
        private String comingTime;
        private String lateTime;
        private String absentTime;
        private String periodStartTime;
        private String periodEndTime;
        RoomModel.Room room;
        List<ScheduleModel.Schedule> scheduleList;
        SectionModel.Section section;

        public Period() {
        }

        public Period(long periodID, String dayOfWeek, String studyType, String comingTime, String lateTime, String absentTime, String periodStartTime, String periodEndTime) {
            this.periodID = periodID;
            this.dayOfWeek = dayOfWeek;
            this.studyType = studyType;
            this.comingTime = comingTime;
            this.lateTime = lateTime;
            this.absentTime = absentTime;
            this.periodStartTime = periodStartTime;
            this.periodEndTime = periodEndTime;
        }

        public RoomModel.Room getRoom() {
            return room;
        }

        public void setRoom(RoomModel.Room room) {
            this.room = room;
        }

        public List<ScheduleModel.Schedule> getScheduleList() {
            return scheduleList;
        }

        public SectionModel.Section getSection() {
            return section;
        }

        public void setSection(SectionModel.Section section) {
            this.section = section;
        }

        public void setScheduleList(List<ScheduleModel.Schedule> scheduleList) {
            this.scheduleList = scheduleList;
        }

        public long getPeriodID() {
            return periodID;
        }

        public void setPeriodID(long periodID) {
            this.periodID = periodID;
        }

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getStudyType() {
            return studyType;
        }

        public void setStudyType(String studyType) {
            this.studyType = studyType;
        }

        public String getComingTime() {
            return comingTime;
        }

        public void setComingTime(String comingTime) {
            this.comingTime = comingTime;
        }

        public String getLateTime() {
            return lateTime;
        }

        public void setLateTime(String lateTime) {
            this.lateTime = lateTime;
        }

        public String getAbsentTime() {
            return absentTime;
        }

        public void setAbsentTime(String absentTime) {
            this.absentTime = absentTime;
        }

        public String getPeriodStartTime() {
            return periodStartTime;
        }

        public void setPeriodStartTime(String periodStartTime) {
            this.periodStartTime = periodStartTime;
        }

        public String getPeriodEndTime() {
            return periodEndTime;
        }

        public void setPeriodEndTime(String periodEndTime) {
            this.periodEndTime = periodEndTime;
        }
    }
}
