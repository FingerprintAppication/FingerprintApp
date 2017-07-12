package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.FacultyModel;
import com.example.i2ichest_.fingerprintit.model.LoginModel;
import com.example.i2ichest_.fingerprintit.model.MajorModel;
import com.example.i2ichest_.fingerprintit.model.PersonModel;
import com.example.i2ichest_.fingerprintit.model.SubjectModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    final List<SubjectModel> listSubject = new ArrayList<SubjectModel>();
                    List<String> listSubjectName = new ArrayList<String>();

                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                        long subjectID = jsonObject.getLong("subjectID");
                        String subjectNumber = jsonObject.getString("subjectNumber");
                        String subjectName = jsonObject.getString("subjectName");
                        int credit = jsonObject.getInt("credit");

                        JSONObject jsonMajor = jsonObject.getJSONObject("major");
                        long majorID = jsonMajor.getLong("majorID");
                        String secondaryMajorID = jsonMajor.getString("secondaryMajorID");
                        String majorName = jsonMajor.getString("majorName");

                        JSONObject jsonFaculty = jsonMajor.getJSONObject("faculty");
                        long facultyID = jsonFaculty.getLong("facultyID");
                        String facultyName = jsonFaculty.getString("facultyName");

                        FacultyModel facultyModel = new FacultyModel();
                        facultyModel.getFaculty().setFacultyID(facultyID);
                        facultyModel.getFaculty().setFacultyName(facultyName);

                        MajorModel majorModel = new MajorModel();
                        majorModel.getMajor().setMajorID(majorID);
                        majorModel.getMajor().setMajorName(majorName);
                        majorModel.getMajor().setScondaryMajorID(secondaryMajorID);
                        majorModel.getMajor().setFaculty(facultyModel.getFaculty());

                        SubjectModel subjectModel = new SubjectModel();
                        subjectModel.getSubject().setSubjectID(subjectID);
                        subjectModel.getSubject().setSubjectNumber(subjectNumber);
                        subjectModel.getSubject().setSubjectName(subjectName);
                        subjectModel.getSubject().setCredit(credit);
                        subjectModel.getSubject().setMajor(majorModel.getMajor());

                        listSubject.add(subjectModel);
                        listSubjectName.add(subjectNumber + " " + subjectName);
                    }
                        Log.d("TEST LIST SUBJECT " , listSubject.toString());

                    ListView listView = (ListView) findViewById(R.id.listViewSubject);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewListSubjectActivity.this,android.R.layout.simple_selectable_list_item,listSubjectName);
                    listView.setAdapter(adapter);

                   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                           Toast.makeText(ViewListSubjectActivity.this, "CLICK " + listSubject.get(i).getSubject().toString(), Toast.LENGTH_SHORT).show();
                       }
                   });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(ViewListSubjectActivity.this, "Search Success ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                progress.dismiss();
                Toast.makeText(ViewListSubjectActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
