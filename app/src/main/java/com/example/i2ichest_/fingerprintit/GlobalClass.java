package com.example.i2ichest_.fingerprintit;

import android.app.Application;

import com.example.i2ichest_.fingerprintit.model.LoginModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MSI on 9/7/2560.
 */

public class GlobalClass extends Application {
    String typeUser;
    LoginModel loginModel = new LoginModel();
    List<StudentModel.Student> listStudent = new ArrayList<>();

    public GlobalClass(){}

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public LoginModel getLoginModel(){
        return loginModel;
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

    public List<StudentModel.Student> getListStudent() {
        return listStudent;
    }

    public void setListStudent(List<StudentModel.Student> listStudent) {
        this.listStudent = listStudent;
    }
}
