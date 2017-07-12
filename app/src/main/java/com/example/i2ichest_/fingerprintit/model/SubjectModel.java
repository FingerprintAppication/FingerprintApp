package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * Created by MSI on 5/7/2560.
 */

public class SubjectModel implements Serializable{
    private Subject subject;
    Gson gson = new GsonBuilder().create();

    public SubjectModel(){
        subject = new Subject();
    }

    public SubjectModel(String jsonResponse){
        subject = gson.fromJson(jsonResponse,Subject.class);
    }

    public Subject getSubject(){
        return subject;
    }

    public String toJSONString(){
        return gson.toJson(this.subject);
    }

    public class Subject {
        private Long subjectID;
        private int credit;
        private String subjectName;
        private String subjectNumber;
        MajorModel.Major major;

        public Subject(){}

        public Subject(Long subjectID, int credit, String subjectName, String subjectNumber, MajorModel.Major major) {
            this.subjectID = subjectID;
            this.credit = credit;
            this.subjectName = subjectName;
            this.subjectNumber = subjectNumber;
            this.major = major;
        }

        public Long getSubjectID() {
            return subjectID;
        }

        public void setSubjectID(Long subjectID) {
            this.subjectID = subjectID;
        }

        public MajorModel.Major getMajor() {
            return major;
        }

        public void setMajor(MajorModel.Major major) {
            this.major = major;
        }

        public String getSubjectNumber() {
            return subjectNumber;
        }

        public void setSubjectNumber(String subjectNumber) {
            this.subjectNumber = subjectNumber;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public int getCredit() {
            return credit;
        }

        public void setCredit(int credit) {
            this.credit = credit;
        }

        @Override
        public String toString() {
            return "Subject{" +
                    "subjectID=" + subjectID +
                    ", credit=" + credit +
                    ", subjectName='" + subjectName + '\'' +
                    ", subjectNumber='" + subjectNumber + '\'' +
                    ", major=" + major +
                    '}';
        }
    }
}
