package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;

public class FacultyModel implements Serializable {
    private Faculty faculty;
    Gson gson = new GsonBuilder().create();

    public FacultyModel(){
        faculty = new Faculty();
    }

    public FacultyModel(String jsonResponse){
        faculty = gson.fromJson(jsonResponse,Faculty.class);
    }

    public Faculty getFaculty(){
        return faculty;
    }

    public String toJSONString(){
        return gson.toJson(this.faculty);
    }

        public class Faculty implements Serializable{
            private Long facultyID;
            private String facultyName;

            public Faculty() {
            }

            public Faculty(Long facultyID, String facultyName) {
                this.facultyID = facultyID;
                this.facultyName = facultyName;
            }

            public Long getFacultyID() {
                return facultyID;
            }

            public void setFacultyID(Long facultyID) {
                this.facultyID = facultyID;
            }

            public String getFacultyName() {
                return facultyName;
            }

            public void setFacultyName(String facultyName) {
                this.facultyName = facultyName;
            }
        }

}
