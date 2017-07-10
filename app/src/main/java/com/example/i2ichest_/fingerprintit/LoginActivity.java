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
        LoginModel loginModel = new LoginModel();

        final EditText username = (EditText) findViewById(R.id.editTextUsername);
        EditText password = (EditText) findViewById(R.id.editTextPassword);

        loginModel.getLogin().setUsername(username.getText().toString());
        loginModel.getLogin().setPassword(password.getText().toString());

        wsManager.doLogin(loginModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(final Object response) {
                progress.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    if(jsonObject.getString("username").equals("null")){
                        Toast.makeText(LoginActivity.this, "กรุณาตรวจสอบข้อมูลใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
                    } else {
                        /************** set Login ************/
                        long loginID = jsonObject.getLong("loginID");
                        String username = jsonObject.getString("username");
                        String password = jsonObject.getString("password");

                        /************** set Person ************/
                        JSONObject jsonPerson = jsonObject.getJSONObject("person");
                        long personID = jsonPerson.getLong("personID");
                        String title = jsonPerson.getString("title");
                        String firstName = jsonPerson.getString("firstName");
                        String lastName = jsonPerson.getString("lastName");

                        /************** set fingerData ************/
                        JSONObject jsonFingerData = jsonPerson.getJSONObject("fingerprintData");
                        String fingerData = jsonFingerData.getString("fingerprintNumber");

                        PersonModel person = new PersonModel();
                        person.getPerson().setPersonID(personID);
                        person.getPerson().setTitle(title);
                        person.getPerson().setFirstName(firstName);
                        person.getPerson().setLastName(lastName);
                        person.getPerson().setFingerprintData(fingerData);

                        /************** set Major ************/
                        JSONObject jsonMajor = jsonPerson.getJSONObject("major");
                        long majorID = jsonMajor.getLong("majorID");
                        String secondaryMajorID = jsonMajor.getString("secondaryMajorID");
                        String majorName = jsonMajor.getString("majorName");

                        MajorModel major = new MajorModel();
                        major.getMajor().setMajorID(majorID);
                        major.getMajor().setScondaryMajorID(secondaryMajorID);
                        major.getMajor().setMajorName(majorName);

                        /************** set Faculty ************/
                        JSONObject jsonFaculty = jsonMajor.getJSONObject("faculty");
                        long facultyID = jsonFaculty.getLong("facultyID");
                        String facultyName = jsonFaculty.getString("facultyName");

                        FacultyModel faculty = new FacultyModel();
                        faculty.getFaculty().setFacultyID(facultyID);
                        faculty.getFaculty().setFacultyName(facultyName);

                        /************** Finish Add to Person ************/
                        major.getMajor().setFaculty(faculty.getFaculty());
                        person.getPerson().setMajor(major.getMajor());

                        /************** set Global Class or LoginModel ************/
                        gb.getLoginModel().getLogin().setLoginID(loginID);
                        gb.getLoginModel().getLogin().setUsername(username);
                        gb.getLoginModel().getLogin().setPassword(password);
                        gb.getLoginModel().getLogin().setPerson(person.getPerson());

                        Log.d("GB : ",gb.getLoginModel().getLogin().toString());

                        Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                progress.dismiss();
                Toast.makeText(LoginActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(this, " " + username.getText().toString() + " / " + password.getText().toString() , Toast.LENGTH_SHORT).show();
    }

    public void onClickVerifyParent(View view){
        Intent intent = new Intent(LoginActivity.this,VerifyStudentParentActivity.class);
        startActivity(intent);
    }
}
