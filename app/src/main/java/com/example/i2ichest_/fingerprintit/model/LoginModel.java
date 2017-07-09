package com.example.i2ichest_.fingerprintit.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * Created by MSI on 9/7/2560.
 */

public class LoginModel implements Serializable{
    private Login login;
    Gson gson = new GsonBuilder().create();

    public LoginModel(){
        login = new Login();
    }

    public LoginModel(String jsonResponse){
        login = gson.fromJson(jsonResponse,Login.class);
    }

    public Login getLogin(){
        return login;
    }

    public String toJSONString(){
        return gson.toJson(this.login);
    }

    public class Login {
        private Long loginID;
        private String username;
        private String password;
        PersonModel.Person person;

        public Login(){}

        public Login(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public Login(Long loginID, PersonModel.Person person, String password, String username) {
            this.loginID = loginID;
            this.person = person;
            this.password = password;
            this.username = username;
        }

        public Long getLoginID() {
            return loginID;
        }

        public void setLoginID(Long loginID) {
            this.loginID = loginID;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public PersonModel.Person getPerson() {
            return person;
        }

        public void setPerson(PersonModel.Person person) {
            this.person = person;
        }

        @Override
        public String toString() {
            return "Login{" +
                    "loginID=" + loginID +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", person=" + person +
                    '}';
        }
    }
}
