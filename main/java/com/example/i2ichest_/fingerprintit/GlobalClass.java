package com.example.i2ichest_.fingerprintit;

import android.app.Application;

import com.example.i2ichest_.fingerprintit.model.LoginModel;

/**
 * Created by MSI on 9/7/2560.
 */

public class GlobalClass extends Application {
    LoginModel loginModel = new LoginModel();

    public GlobalClass(){}

    public LoginModel getLoginModel(){
        return loginModel;
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }


}
