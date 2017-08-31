package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;
import com.example.i2ichest_.fingerprintit.model.PersonModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewListLeaveHistoryActivity extends AppCompatActivity {
    WSManager wsManager;
    private GlobalClass gb;
    /*for keeping big images code*/
    List<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_leave_history);
        gb = (GlobalClass) this.getApplicationContext();
        showLeaveHistory();
    }

    public void showLeaveHistory(){
        Intent intent = getIntent();
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

        wsManager = WSManager.getWsManager(this);

        PersonModel personModel = new PersonModel();
        personModel.getPerson().setPersonID(PERSONID);

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
                        Calendar car = Calendar.getInstance();
                        car.clear();
                        Date date = new Date();
                        Long setDate = Long.parseLong(i.getSchedule().getScheduleDate());
                        date.setTime(setDate);
                        car.setTime(date);
                        i.getSchedule().setScheduleDate(
                                car.get(java.util.Calendar.DAY_OF_MONTH) + "-"
                                        + (car.get(java.util.Calendar.MONTH) + 1) + "-"
                                        + car.get(java.util.Calendar.YEAR)
                        );

                        string.add("วิชา " + i.getSchedule().getPeriod().getSection().getSubject().getSubjectNumber() + " : " + i.getSchedule().getPeriod().getSection().getSubject().getSubjectName()
                                + "\nวันที่ลา " +  i.getSchedule().getScheduleDate() + " สถานะการลา [ " + i.getStatus() + " ]" );
                        images.add(i.getSupportDocument());

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewListLeaveHistoryActivity.this, android.R.layout.simple_selectable_list_item, string);
                    view.setAdapter(adapter);
                } else {
                    Toast.makeText(ViewListLeaveHistoryActivity.this, "ไม่พบข้อมูลการลา", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ViewListLeaveHistoryActivity.this, "ข้อมูลผิดพลาด", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
