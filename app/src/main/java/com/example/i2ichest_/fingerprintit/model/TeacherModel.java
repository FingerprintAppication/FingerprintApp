package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

public class TeacherModel extends PersonModel implements Serializable{
    private Teacher teacher;
    Gson gson = new GsonBuilder().create();

    public TeacherModel(){
        teacher = new Teacher();
    }

    public TeacherModel(String jsonResponse){
        teacher = gson.fromJson(jsonResponse,Teacher.class);
    }

    public Teacher getTeacher(){
        return teacher;
    }

    public String toJSONString(){
        return gson.toJson(this.teacher);
    }

    public class Teacher extends Person implements Serializable {
        private String teacherID;

        public Teacher() {
        }

        public Teacher(String teacherID,Long personID, String title, String firstName, String lastName,String fingerprintData) {
            super(personID,title, firstName, lastName, fingerprintData);
            this.teacherID = teacherID;
        }

        public String getTeacherID() {
            return teacherID;
        }

        public void setTeacherID(String teacherID) {
            this.teacherID = teacherID;
        }
    }

}
