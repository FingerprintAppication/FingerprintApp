package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;
import com.example.i2ichest_.fingerprintit.model.PersonModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ViewListLeaveHistoryActivity extends AppCompatActivity {
    WSManager wsManager;
    private GlobalClass gb;
    /*for keeping big images code*/
    List<String> images;
    Intent intent;
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_leave_history);
        gb = (GlobalClass) this.getApplicationContext();
        intent = getIntent();
        toolBar = (Toolbar)findViewById(R.id.profile);
        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        showLeaveHistory();
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

    public void showLeaveHistory(){
        String resultInform = intent.getStringExtra("resultInform");
        if(resultInform!= null) {
            AlertDialog alertDialog = new AlertDialog.Builder(ViewListLeaveHistoryActivity.this).create();
            alertDialog.setTitle("ลาเรียน");
            Log.d("TAG", "ลาเข้าล้ะ: ");
            if ("ลาเรียนสำเร็จ".equals(resultInform)) {
                alertDialog.setIcon(getResources().getDrawable(R.drawable.success));
                alertDialog.setMessage(resultInform);
            } else if ("ไม่สามารถลาเรียนได้เนื่องจากท่านได้ลาเรียนวันนี้เเล้ว".equals(resultInform)){
                alertDialog.setIcon(getResources().getDrawable(R.drawable.duplicated));
                alertDialog.setMessage(resultInform);
            }else{
                alertDialog.setIcon(getResources().getDrawable(R.drawable.duplicated));
                alertDialog.setMessage("ไม่สามารถลาเรียนได้");
            }
            alertDialog.show();
        }
        /*show response when we already updated image*/
        String result = intent.getStringExtra("result");
        if(result!=null){
            AlertDialog alertDialog = new AlertDialog.Builder(ViewListLeaveHistoryActivity.this).create();
            alertDialog.setTitle("อับโหลดรูปภาพ");
            if ("success".equals(result)) {
                alertDialog.setIcon(getResources().getDrawable(R.drawable.success));
                alertDialog.setMessage("อับโหลดรูปภาพเสร็จสมบูรณ์");
            } else {
                alertDialog.setIcon(getResources().getDrawable(R.drawable.error));
                alertDialog.setMessage("ไม่สามารถอับโหลดรูปภาพได้");
            }
            alertDialog.show();
        }
        final Long PERSONID = intent.getLongExtra("personId",1L);
        Log.d("PERSONID", PERSONID.toString());
        final ProgressDialog progress = ProgressDialog.show(ViewListLeaveHistoryActivity.this,"Please Wait...","Please wait...",true);

        PersonModel personModel = new PersonModel();
        personModel.getPerson().setPersonID(PERSONID);

        wsManager = WSManager.getWsManager(this);
        wsManager.doSearchLeaveHistory(personModel, new WSManager.WSManagerListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(Object response) {
                final List<InformLeaveModel.InformLeave> list = (List<InformLeaveModel.InformLeave>) response;

                ListView view = (ListView) findViewById(R.id.listViewLeaveHistory);

                if (!list.isEmpty()) {
                    view.clearAnimation();
                    List<String> string = new ArrayList<String>();
                    images = new ArrayList<String>();
                    for (InformLeaveModel.InformLeave i : list) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        string.add("วิชา " + i.getSchedule().getPeriod().getSection().getSubject().getSubjectNumber() + " : " + i.getSchedule().getPeriod().getSection().getSubject().getSubjectName()
                                + "\nวันที่ลา " +  sdf.format(i.getSchedule().getScheduleDate()) + " [ " + i.getInformType() + " ] "
                                + "\nสถานะการลา [ " + i.getStatus() + " ]" );
                        images.add(i.getSupportDocument());

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewListLeaveHistoryActivity.this, android.R.layout.simple_selectable_list_item, string);
                    view.setAdapter(adapter);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(ViewListLeaveHistoryActivity.this).create();
                    alertDialog.setIcon(getResources().getDrawable(R.drawable.error));
                    alertDialog.setTitle("สถานะข้อมูลการลา");
                    alertDialog.setMessage("ไม่พบข้อมูลการลา");
                    alertDialog.show();
                }

                view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(ViewListLeaveHistoryActivity.this,ViewLeaveHistoryActivity.class);
                        gb.setLargeImage(images.get(i));
                        list.get(i).setSupportDocument("");
                        intent.putExtra("inform" , list.get(i));
                        intent.putExtra("personId",PERSONID);
                        startActivity(intent);
                    }
                });

                progress.dismiss();
            }

            @Override
            public void onError(String error) {
                progress.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(ViewListLeaveHistoryActivity.this).create();
                alertDialog.setIcon(getResources().getDrawable(R.drawable.error));
                alertDialog.setTitle("สถานะข้อมูลการลา");
                alertDialog.setMessage("ข้อมูลผิดพลาด");
                alertDialog.show();
            }
        });

    }
}
