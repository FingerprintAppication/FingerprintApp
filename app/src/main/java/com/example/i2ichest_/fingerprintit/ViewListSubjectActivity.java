package com.example.i2ichest_.fingerprintit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.LoginModel;

public class ViewListSubjectActivity extends AppCompatActivity {
    private GlobalClass gb;
    WSManager wsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_subject);
        gb = (GlobalClass) this.getApplicationContext();
        searchSubject();
    }

    public LoginModel.Login getUserData(){
        return gb.getLoginModel().getLogin();
    }

    public void searchSubject(){
        Log.d("UserData", getUserData().toString());

    }
}
