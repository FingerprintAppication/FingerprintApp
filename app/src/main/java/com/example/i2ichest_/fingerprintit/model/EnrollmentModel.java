package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.List;

public class EnrollmentModel implements Serializable{
    private Enrollment enrollment;
    Gson gson = new GsonBuilder().create();

    public EnrollmentModel(){
        enrollment = new Enrollment();
    }

    public EnrollmentModel(String jsonResponse){
        enrollment = gson.fromJson(jsonResponse,Enrollment.class);
    }

    public Enrollment getEnrollment(){
        return enrollment;
    }

    public String toJSONString(){
        return gson.toJson(this.enrollment);
    }

    public class Enrollment implements Serializable {
        private Long enrollmentID;
        private String status;
        StudentModel.Student student;
        List<AttendanceModel.Attendance> attendanceList;
        SectionModel.Section section;

        public Enrollment() {
        }

        public Enrollment(Long enrollmentID, String status) {
            this.enrollmentID = enrollmentID;
            this.status = status;
        }

        public List<AttendanceModel.Attendance> getAttendanceList() {
            return attendanceList;
        }

        public void setAttendanceList(List<AttendanceModel.Attendance> attendanceList) {
            this.attendanceList = attendanceList;
        }

        public StudentModel.Student getStudent() {
            return student;
        }

        public void setStudent(StudentModel.Student student) {
            this.student = student;
        }

        public Long getEnrollmentID() {
            return enrollmentID;
        }

        public void setEnrollmentID(Long enrollmentID) {
            this.enrollmentID = enrollmentID;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public SectionModel.Section getSection() {
            return section;
        }

        public void setSection(SectionModel.Section section) {
            this.section = section;
        }
    }




}
