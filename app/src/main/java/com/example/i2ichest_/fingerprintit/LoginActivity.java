package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.FacultyModel;
import com.example.i2ichest_.fingerprintit.model.LoginModel;
import com.example.i2ichest_.fingerprintit.model.MajorModel;
import com.example.i2ichest_.fingerprintit.model.PersonModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private GlobalClass gb;
    WSManager wsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gb = (GlobalClass) this.getApplicationContext();
    }

    public void onClickLogin(View view){
        final ProgressDialog progress = ProgressDialog.show(LoginActivity.this,"Please Wait...","Please wait...",true);
        wsManager = WSManager.getWsManager(this);
        final LoginModel loginModel = new LoginModel();

        final EditText username = (EditText) findViewById(R.id.editTextUsername);
        EditText password = (EditText) findViewById(R.id.editTextPassword);

        loginModel.getLogin().setUsername(username.getText().toString());
        loginModel.getLogin().setPassword(password.getText().toString());

        wsManager.doLogin(loginModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                progress.dismiss();

                Map<String,List<String>> map = (Map<String, List<String>>) response;

                if (!map.isEmpty()) {
                    Log.d("Map<LOGIN> @@ : ", map.get("login").toString());
                    List<String> login = map.get("login");

                    gb.getLoginModel().getLogin().setUsername(login.get(0));
                    gb.getLoginModel().getLogin().setPassword(login.get(1));

                    PersonModel personModel = new PersonModel();
                    personModel.getPerson().setTitle(login.get(2));
                    personModel.getPerson().setFirstName(login.get(3));
                    personModel.getPerson().setLastName(login.get(4));
                    personModel.getPerson().setPersonID(Long.valueOf(login.get(5)));

                    if (login.get(8).equals("student") || login.get(8).equals("teacher")) {
                        if (login.get(8).equals("student")) {
                            personModel.getPerson().setFingerprintData(login.get(9));
                        }

                        MajorModel majorModel = new MajorModel();
                        majorModel.getMajor().setMajorName(login.get(6));

                        FacultyModel facultyModel = new FacultyModel();
                        facultyModel.getFaculty().setFacultyName(login.get(7));

                        majorModel.getMajor().setFaculty(facultyModel.getFaculty());
                        personModel.getPerson().setMajor(majorModel.getMajor());
                    }

                    gb.getLoginModel().getLogin().setPerson(personModel.getPerson());
                    gb.setTypeUser(login.get(8));

                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "กรุณาตรวจสอบข้อมูลใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                progress.dismiss();
                Toast.makeText(LoginActivity.this, "ผิดพลาด " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickVerifyParent(View view){
        Intent intent = new Intent(LoginActivity.this,VerifyStudentParentActivity.class);
        startActivity(intent);
    }
}
