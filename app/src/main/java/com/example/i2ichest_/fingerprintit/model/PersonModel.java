package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

public class PersonModel implements Serializable {
    private Person person;
    Gson gson = new GsonBuilder().create();

    public PersonModel(){
        person = new Person();
    }

    public PersonModel(String jsonResponse){
        person = gson.fromJson(jsonResponse,Person.class);
    }

    public Person getPerson(){
        return person;
    }

    public String toJSONString(){
        return gson.toJson(this.person);
    }

    public class Person {

        private Long personID;
        private String title;
        private String firstName;
        private String lastName;
        private String fingerprintData;
        MajorModel.Major major;

        public Person() {
        }

        public Person(Long personID, String title, String firstName, String lastName,String fingerprintData) {
            this.personID = personID;
            this.title = title;
            this.firstName = firstName;
            this.lastName = lastName;
            this.fingerprintData = fingerprintData;
        }

        public MajorModel.Major getMajor() {
            return major;
        }

        public void setMajor(MajorModel.Major major) {
            this.major = major;
        }

        public Long getPersonID() {
            return personID;
        }

        public void setPersonID(Long personID) {
            this.personID = personID;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFingerprintData() {
            return fingerprintData;
        }

        public void setFingerprintData(String fingerprintData) {
            this.fingerprintData = fingerprintData;
        }
    }

}
