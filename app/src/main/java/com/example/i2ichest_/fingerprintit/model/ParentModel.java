package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ParentModel extends PersonModel{
    private Parent parent;
    Gson gson = new GsonBuilder().create();

    public ParentModel(){
        parent = new Parent();
    }

    public ParentModel(String jsonResponse){
        parent = gson.fromJson(jsonResponse,Parent.class);
    }

    public Parent getParent(){
        return parent;
    }

    public String toJSONString(){
        return gson.toJson(this.parent);
    }


    public class Parent extends Person {
        private String phoneNo;
        private String email;

        public Parent() {
        }

        public Parent(String phoneNo, String email,Long personID, String title, String firstName, String lastName,String fingerprintData) {
            super(personID,title, firstName, lastName, fingerprintData);
            this.phoneNo = phoneNo;
            this.email = email;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
