package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AttendanceModel {
    private Attendance attendance;
    Gson gson = new GsonBuilder().create();

    public AttendanceModel(){
        attendance = new Attendance();
    }

    public AttendanceModel(String jsonResponse){
        attendance = gson.fromJson(jsonResponse,Attendance.class);
    }

    public Attendance getAttendance(){
        return attendance;
    }

    public String toJSONString(){
        return gson.toJson(this.attendance);
    }

    public class Attendance {
        private long attendanceID;
        private String status;
        private String statusDescription;
        ScheduleModel.Schedule schedule;
        EnrollmentModel.Enrollment enrollment;


        public Attendance() {
        }

        public Attendance(long attendanceID, String status, String statusDescription) {
            this.attendanceID = attendanceID;
            this.status = status;
            this.statusDescription = statusDescription;
        }

        public ScheduleModel.Schedule getSchedule() {
            return schedule;
        }

        public void setSchedule(ScheduleModel.Schedule schedule) {
            this.schedule = schedule;
        }

        public EnrollmentModel.Enrollment getEnrollment() {
            return enrollment;
        }

        public void setEnrollment(EnrollmentModel.Enrollment enrollment) {
            this.enrollment = enrollment;
        }

        public long getAttendanceID() {
            return attendanceID;
        }

        public void setAttendanceID(long attendanceID) {
            this.attendanceID = attendanceID;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusDescription() {
            return statusDescription;
        }

        public void setStatusDescription(String statusDescription) {
            this.statusDescription = statusDescription;
        }
    }
}
