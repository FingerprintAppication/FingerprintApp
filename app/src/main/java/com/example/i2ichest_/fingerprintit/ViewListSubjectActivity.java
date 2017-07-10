package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.LoginModel;
import com.example.i2ichest_.fingerprintit.model.PersonModel;

public class ViewListSubjectActivity extends AppCompatActivity {
    private GlobalClass gb;
    WSManager wsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_subject);
        gb = (GlobalClass) this.getApplicationContext();
        showListSubject();
    }

    public LoginModel.Login getUserData(){
        return gb.getLoginModel().getLogin();
    }

    public void showListSubject(){
        Log.d("UserData", getUserData().toString());

        final ProgressDialog progress = ProgressDialog.show(ViewListSubjectActivity.this,"Please Wait...","Please wait...",true);
        wsManager = WSManager.getWsManager(this);

        PersonModel personModel = new PersonModel();
        personModel.getPerson().setPersonID(gb.getLoginModel().getLogin().getPerson().getPersonID());
        wsManager.doSearchSubject(personModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                progress.dismiss();
                Toast.makeText(ViewListSubjectActivity.this, "Response " + response.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                progress.dismiss();
                Toast.makeText(ViewListSubjectActivity.this, "ERror " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
