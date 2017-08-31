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
    StudentModel.Student parentStudent = new StudentModel().getStudent();
    List<String> allSubject = new ArrayList<>();
    /*อันนี้เอาเก็บโค้ดรูปที่โครตใหญ่*/
    String largeImage = "";

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

    public StudentModel.Student getParentStudent() {
        return parentStudent;
    }

    public void setParentStudent(StudentModel.Student parentStudent) {
        this.parentStudent = parentStudent;
    }

    public List<String> getAllSubject() {
        return allSubject;
    }

    public void setAllSubject(List<String> allSubject) {
        this.allSubject = allSubject;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }
}
