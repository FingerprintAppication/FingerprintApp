package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.example.i2ichest_.fingerprintit.adapter.ListViewAdapter;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.FacultyModel;
import com.example.i2ichest_.fingerprintit.model.LoginModel;
import com.example.i2ichest_.fingerprintit.model.MajorModel;
import com.example.i2ichest_.fingerprintit.model.PersonModel;
import com.example.i2ichest_.fingerprintit.model.SubjectModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewListSubjectActivity extends AppCompatActivity implements Serializable {
    private GlobalClass gb;
    WSManager wsManager;
    ListViewAdapter lva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_subject);
        gb = (GlobalClass) this.getApplicationContext();
        showListSubject();
    }

    public void showListSubject(){
        final ProgressDialog progress = ProgressDialog.show(ViewListSubjectActivity.this,"Please Wait...","Please wait...",true);
        wsManager = WSManager.getWsManager(this);

        Intent intent = getIntent();
        Long pID = intent.getLongExtra("personID",1L);

        PersonModel personModel = new PersonModel();
        personModel.getPerson().setPersonID(pID);

        wsManager.doSearchSubject(personModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                progress.dismiss();

                    final List<SubjectModel> listSubject = (List<SubjectModel>) response;
                    List<String> listSubjectName = new ArrayList<>();

                    for (int i = 0 ; i < listSubject.size() ; i++){
                        listSubjectName.add(listSubject.get(i).getSubject().getSubjectID()
                                + "="+ listSubject.get(i).getSubject().getSubjectNumber()
                                + " " + listSubject.get(i).getSubject().getSubjectName());
                    }

                    lva = new ListViewAdapter(getApplicationContext(),listSubjectName,ViewListSubjectActivity.this);
                    lva.setCountView(listSubjectName.size());
                    final ListView listView = (ListView) findViewById(R.id.listViewSubject);
                    lva.setMode(Attributes.Mode.Single);
                    listView.setAdapter(lva);

                   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                           Intent intent = new Intent(ViewListSubjectActivity.this,PeriodActivity.class);
                           intent.putExtra("subjectID", listSubject.get(i).getSubject().getSubjectID());
                           intent.putExtra("subjectNumber", listSubject.get(i).getSubject().getSubjectNumber());
                           intent.putExtra("subjectName", listSubject.get(i).getSubject().getSubjectName());
                           startActivity(intent);
                       }
                   });

                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            ((SwipeLayout) (listView.getChildAt(i - listView.getFirstVisiblePosition()))).open(true);
                            return false;
                        }
                    });

            }

            @Override
            public void onError(String error) {
                progress.dismiss();
                Toast.makeText(ViewListSubjectActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
