package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewListInformLeaveActivity extends AppCompatActivity {
    WSManager wsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_inform_leave);
        showInformLeave ();
    }

    public void showInformLeave () {
        wsManager = WSManager.getWsManager(this);
        Intent intent = getIntent();
        String personId = intent.getStringExtra("personId");
        final ProgressDialog progress = ProgressDialog.show(this,"Please Wait...","Please wait...",true);
        wsManager.searchInformLeaveForTeacher(personId, new WSManager.WSManagerListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(Object response) {
                final List<InformLeaveModel.InformLeave> list =(List<InformLeaveModel.InformLeave>) response;
                ListView view = (ListView)findViewById(R.id.listInform);
                ArrayList<String> string = new ArrayList<String>();
                for(InformLeaveModel.InformLeave i:list){
                    Calendar car = Calendar.getInstance();
                    car.clear();
                    Date date = new Date();
                    Long setDate = Long.parseLong(i.getSchedule().getScheduleDate());
                    date.setTime(setDate);
                    car.setTime(date);
                    i.getSchedule().setScheduleDate(car.get(java.util.Calendar.YEAR)+"-"
                            +(car.get(java.util.Calendar.MONTH)+1)
                            +"-"+car.get(java.util.Calendar.DAY_OF_MONTH));
                    string.add(i.getStudent().getStudentID()+" "+
                            " "+i.getSchedule().getScheduleDate()+" "+i.getSchedule().getPeriod().getSection().getSubject().getSubjectNumber());

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewListInformLeaveActivity.this,android.R.layout.simple_list_item_1, string);
                view.setAdapter(adapter);
                progress.dismiss();
                view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(ViewListInformLeaveActivity.this,ApproveLeaveActivity.class);
                        intent.putExtra("informleave",list.get(i));
                        startActivity(intent);
                    }
                });



            }

            @Override
            public void onError(String error) {

            }
        });
    }


}
