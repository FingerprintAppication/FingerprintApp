package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;

public class MajorModel implements Serializable {
    private Major major;
    Gson gson = new GsonBuilder().create();

    public MajorModel(){
        major = new Major();
    }

    public MajorModel(String jsonResponse){
        major = gson.fromJson(jsonResponse,Major.class);
    }


    public Major getMajor(){
        return major;
    }

    public String toJSONString(){
        return gson.toJson(this.major);
    }

        public class Major implements Serializable {
            private Long majorID;
            private String scondaryMajorID;
            private String majorName;
            FacultyModel.Faculty faculty;

            public Major() {
            }

            public Major(Long majorID, String scondaryMajorID, String majorName) {
                this.majorID = majorID;
                this.scondaryMajorID = scondaryMajorID;
                this.majorName = majorName;
            }

            public FacultyModel.Faculty getFaculty() {
                return faculty;
            }

            public void setFaculty(FacultyModel.Faculty faculty) {
                this.faculty = faculty;
            }

            public Long getMajorID() {
                return majorID;
            }

            public void setMajorID(Long majorID) {
                this.majorID = majorID;
            }

            public String getScondaryMajorID() {
                return scondaryMajorID;
            }

            public void setScondaryMajorID(String scondaryMajorID) {
                this.scondaryMajorID = scondaryMajorID;
            }

            public String getMajorName() {
                return majorName;
            }

            public void setMajorName(String majorName) {
                this.majorName = majorName;
            }
        }
}
