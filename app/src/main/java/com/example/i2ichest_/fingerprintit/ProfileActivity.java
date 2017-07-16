package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.FacultyModel;
import com.example.i2ichest_.fingerprintit.model.MajorModel;
import com.example.i2ichest_.fingerprintit.model.PersonModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private GlobalClass gb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        gb = (GlobalClass) this.getApplicationContext();
        setTextProfile();
    }

    public void setTextProfile(){
        String title = gb.getLoginModel().getLogin().getPerson().getTitle();
        String firstName = gb.getLoginModel().getLogin().getPerson().getFirstName();
        String lastName = gb.getLoginModel().getLogin().getPerson().getLastName();
        String sumStr = title + " " + firstName + " " + lastName;

        TextView name = (TextView) findViewById(R.id.textPT);
        TextView major = (TextView) findViewById(R.id.textMJ);
        TextView stuID = (TextView) findViewById(R.id.textSID);
        TextView fingerID = (TextView) findViewById(R.id.textFD);

        name.setText(sumStr);

        String typeUser = gb.getTypeUser();
        if (!typeUser.equals("parent")){
            major.setText(gb.getLoginModel().getLogin().getPerson().getMajor().getMajorName());
        }

        if (typeUser.equals("student")){
            stuID.setText(gb.getLoginModel().getLogin().getUsername());
            fingerID.setText(gb.getLoginModel().getLogin().getPerson().getFingerprintData());

        } else if (typeUser.equals("teacher") ||typeUser.equals("parent")) {
            if (gb.getTypeUser().equals("parent")){
                major.setText("");
                TextView m = (TextView) findViewById(R.id.textMajor);
                m.setText("");
            }
            stuID.setText("");
            fingerID.setText("");

            TextView sid = (TextView) findViewById(R.id.textStudentID);
            TextView finger = (TextView) findViewById(R.id.textFingerID);
            sid.setText("");
            finger.setText("");
        }


    }

    public void onClickBtnViewListSubject(View view){
        WSManager wsManager;
        final ProgressDialog progress = ProgressDialog.show(ProfileActivity.this,"Please Wait...","Please wait...",true);
        wsManager = WSManager.getWsManager(this);

        PersonModel personModel = new PersonModel();
        personModel.getPerson().setPersonID(gb.getLoginModel().getLogin().getPerson().getPersonID());
        wsManager.doSearchStudentParent(personModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                progress.dismiss();

                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    gb.getListStudent().clear();

                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                        String title = jsonObject.getString("title");
                        String firstName = jsonObject.getString("firstName");
                        String lastName = jsonObject.getString("lastName");

                        JSONObject jsonMajor = jsonObject.getJSONObject("major");
                        long majorID = jsonMajor.getLong("majorID");
                        String secondaryMajorID = jsonMajor.getString("secondaryMajorID");
                        String majorName = jsonMajor.getString("majorName");

                        JSONObject jsonFaculty = jsonMajor.getJSONObject("faculty");
                        long facultyID = jsonFaculty.getLong("facultyID");
                        String facultyName = jsonFaculty.getString("facultyName");

                        long studentID = jsonObject.getLong("studentID");
                        String parentPhone = jsonObject.getString("parentPhone");
                        long personID = jsonObject.getLong("personID");

                        StudentModel studentModel = new StudentModel();
                        studentModel.getStudent().setTitle(title);
                        studentModel.getStudent().setFirstName(firstName);
                        studentModel.getStudent().setLastName(lastName);

                        FacultyModel facultyModel = new FacultyModel();
                        facultyModel.getFaculty().setFacultyID(facultyID);
                        facultyModel.getFaculty().setFacultyName(facultyName);

                        MajorModel majorModel = new MajorModel();
                        majorModel.getMajor().setMajorID(majorID);
                        majorModel.getMajor().setScondaryMajorID(secondaryMajorID);
                        majorModel.getMajor().setMajorName(majorName);
                        majorModel.getMajor().setFaculty(facultyModel.getFaculty());

                        studentModel.getStudent().setMajor(majorModel.getMajor());
                        studentModel.getStudent().setPersonID(personID);
                        studentModel.getStudent().setStudentID(studentID);
                        studentModel.getStudent().setParentPhone(parentPhone);

                        gb.getListStudent().add(studentModel.getStudent());
                    }
                    Log.d("LIST STUDENT ADD ", gb.getListStudent().toString());

                    Intent intent = null;
                    //Log.d("Size ", listStudent.size() + "");
                    if ( gb.getListStudent().size() > 1) {
                        intent = new Intent(ProfileActivity.this, SelectStudentParentActivity.class);
                    } else {
                        intent = new Intent(ProfileActivity.this, ViewListSubjectActivity.class);
                    }
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                progress.dismiss();
            }
        });



    }
}
