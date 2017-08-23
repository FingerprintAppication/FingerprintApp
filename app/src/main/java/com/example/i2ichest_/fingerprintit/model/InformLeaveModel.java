package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

public class InformLeaveModel implements Serializable {
    private InformLeave informLeave;
    Gson gson = new GsonBuilder().create();

    public InformLeaveModel(){
        this.informLeave = new InformLeave();
    }

    public InformLeaveModel(String jsonResponse){
        this.informLeave = gson.fromJson(jsonResponse,InformLeave.class);
    }

    public InformLeave getInformLeave(){
        return this.informLeave;
    }

    public void setInformLeave(InformLeave informLeave) {
        this.informLeave = informLeave;
    }

    public String toJSONString(){
        return gson.toJson(this.informLeave);
    }

    public class InformLeave implements Serializable{
        private long informLeaveID;
        private String informType;
        private String supportDocument;
        private String status;
        private String caseDetail;
        private String detail;
        ScheduleModel.Schedule schedule;
        StudentModel.Student student;

        public InformLeave() {
        }

        public InformLeave(long informLeaveID, String informType, String supportDocument, String status, String caseDetail) {
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

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
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
