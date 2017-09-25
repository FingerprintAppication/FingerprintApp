package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.example.i2ichest_.fingerprintit.adapter.ListViewAdapter;
import com.example.i2ichest_.fingerprintit.adapter.ListViewSubjectForParent;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.EnrollmentModel;
import com.example.i2ichest_.fingerprintit.model.PersonModel;
import com.example.i2ichest_.fingerprintit.model.SubjectModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewListSubjectActivity extends AppCompatActivity implements Serializable {
    private GlobalClass gb;
    WSManager wsManager;
    ListViewAdapter lva;
    ListViewSubjectForParent lsp;
    Toolbar toolBar;
    List<SubjectModel> listSubject;
    List<EnrollmentModel.Enrollment> listSubjectName;
    long subjectID;
    String subjectNumber;
    String subjectName;
    boolean a = true;

    //sq lv 2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_subject);
        gb = (GlobalClass) this.getApplicationContext();
        toolBar = (Toolbar)findViewById(R.id.profile);
        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);

        Intent getResultAnnounce = getIntent();
        String result = getResultAnnounce.getStringExtra("result");
        if(result != null){
            AlertDialog resultAlert = new AlertDialog.Builder(ViewListSubjectActivity.this).create();
            resultAlert.setIcon(getResources().getDrawable(R.drawable.error));
            resultAlert.setTitle("สถานะการประกาศข่าว");
            if (result.toString().equals("1")) {
                resultAlert.setIcon(getResources().getDrawable(R.drawable.success));
                resultAlert.setMessage("ประกาศข่าวสำเร็จ");
            } else {
                resultAlert.setIcon(getResources().getDrawable(R.drawable.error));
                resultAlert.setMessage("ข้อมูลผิดพลาด กรุณาตรวจสอบข้อมูลอีกครั้ง");
            }
            resultAlert.show();
        }
        showListSubject();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                finish();
                startActivity(new Intent(this,ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    //sq lv 2
    public void showListSubject(){
        final ProgressDialog progress = ProgressDialog.show(ViewListSubjectActivity.this,"Please Wait...","Please wait...",true);
        Intent intent = getIntent();
        final Long pID = intent.getLongExtra("personID",1L);
        //sq lv 2
        PersonModel personModel = new PersonModel();
        personModel.getPerson().setPersonID(pID);
        wsManager = WSManager.getWsManager(this);
        final ListView listView = (ListView) findViewById(R.id.listViewSubject);
        if("parent".equals(gb.getTypeUser())){
            wsManager.getAttendancesForParent(pID+"", new WSManager.WSManagerListener() {
                @Override
                public void onComplete(Object response) {
                    listSubjectName = (List<EnrollmentModel.Enrollment>)response;
                    lsp = new ListViewSubjectForParent(getApplicationContext(),listSubjectName,listSubjectName.size());
                    listView.setAdapter(lsp);
                    progress.dismiss();
                }

                @Override
                public void onError(String error) {
                    Log.i("Errors090090", "onError: "+error.toString());
                }
            });
        }else {
            //sq lv 2
            wsManager.doSearchSubject(personModel, new WSManager.WSManagerListener() {
                @Override
                public void onComplete(Object response) {
                    progress.dismiss();
                        a = false;
                        //sq lv 2
                        listSubject = (List<SubjectModel>) response;
                        List<String> listSubjectName = new ArrayList<>();

                        for (int i = 0 ; i < listSubject.size() ; i++){
                            listSubjectName.add(listSubject.get(i).getSubject().getSubjectID()
                                    + "="+ listSubject.get(i).getSubject().getSubjectNumber()
                                    + " " + listSubject.get(i).getSubject().getSubjectName());
                        }
                        lva = new ListViewAdapter(getApplicationContext(),listSubjectName,ViewListSubjectActivity.this);
                        lva.setCountView(listSubjectName.size());
                        lva.setMode(Attributes.Mode.Single);
                        listView.setAdapter(lva);
                        //sq lv 2
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
                }
            });

        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(a == false){
                    subjectID = listSubject.get(i).getSubject().getSubjectID();
                    subjectNumber = listSubject.get(i).getSubject().getSubjectNumber();
                    subjectName = listSubject.get(i).getSubject().getSubjectName();
                }else {
                    subjectID = listSubjectName.get(i).getSection().getSubject().getSubjectID();
                    subjectNumber = listSubjectName.get(i).getSection().getSubject().getSubjectNumber();
                    subjectName = listSubjectName.get(i).getSection().getSubject().getSubjectName();
                }
                Intent intent = new Intent(ViewListSubjectActivity.this,PeriodActivity.class);
                intent.putExtra("subjectID", subjectID);
                intent.putExtra("subjectNumber", subjectNumber);
                intent.putExtra("subjectName", subjectName);
                intent.putExtra("personID", pID+"");
                startActivity(intent);
            }
        });
    }
}
