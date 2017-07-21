package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by I2ichest_ on 7/6/2017.
 */

public class InformLeaveModel {
    private InformLeave informLeave;
    Gson gson = new GsonBuilder().create();

    public InformLeaveModel(){
        informLeave = new InformLeave();
    }

    public InformLeaveModel(String jsonResponse){
        informLeave = gson.fromJson(jsonResponse,InformLeave.class);
    }

    public InformLeave getInformLeave(){
        return informLeave;
    }

    public String toJSONString(){
        return gson.toJson(this.informLeave);
    }

    public class InformLeave {
        private long informLeaveID;
        private String informType;
        private String supportDocument;
        private String status;
        private String caseDetail;
        ScheduleModel.Schedule schedule;
        StudentModel.Student student;

        public InformLeave() {
        }

        public InformLeave(int informLeaveID, String informType, String supportDocument, String status, String caseDetail) {
            this.informLeaveID = informLeaveID;
            this.informType = informType;
            this.supportDocument = supportDocument;
            this.status = status;
            this.caseDetail = caseDetail;
        }

        public long getInformLeaveID() {
            return informLeaveID;
        }

        public void setInformLeaveID(long informLeaveID) {
            this.informLeaveID = informLeaveID;
        }

        public String getInformType() {
            return informType;
        }

        public void setInformType(String informType) {
            this.informType = informType;
        }

        public String getSupportDocument() {
            return supportDocument;
        }

        public void setSupportDocument(String supportDocument) {
            this.supportDocument = supportDocument;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCaseDetail() {
            return caseDetail;
        }

        public void setCaseDetail(String caseDetail) {
            this.caseDetail = caseDetail;
        }

        public ScheduleModel.Schedule getSchedule() {
            return schedule;
        }

        public void setSchedule(ScheduleModel.Schedule schedule) {
            this.schedule = schedule;
        }

        public StudentModel.Student getStudent() {
            return student;
        }

        public void setStudent(StudentModel.Student student) {
            this.student = student;
        }
    }
}
