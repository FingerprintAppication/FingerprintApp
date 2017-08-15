package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import com.example.i2ichest_.fingerprintit.DBHelper.DatabaseHelper;
import com.example.i2ichest_.fingerprintit.adapter.InformViewAdapter;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewListInformLeaveActivity extends AppCompatActivity {
    WSManager wsManager;
    DatabaseHelper myDb;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_inform_leave);
        showInformLeave ();
    }
    public void onStart (){
        super.onStart();
        ListView view = (ListView) findViewById(R.id.listInform);
        if(view.getCount()!=0){
            View adat = (View)getViewByPosition(index,view);
            TextView tee = (TextView)adat.findViewById(R.id.informTxt);
            tee.setBackgroundColor(Color.WHITE);
        }
    }

    public void showInformLeave () {
        myDb = new DatabaseHelper(this);
        wsManager = WSManager.getWsManager(this);
        Intent intent = getIntent();
        String personId = intent.getStringExtra("personId");
        final ProgressDialog progress = ProgressDialog.show(this,"Please Wait...","Please wait...",true);
        wsManager.searchInformLeaveForTeacher(personId, new WSManager.WSManagerListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(Object response) {
                myDb.deleteStudents("79");
                myDb.deleteStudents("80");
                myDb.deleteStudents("82");
                final List<InformLeaveModel.InformLeave> list =(List<InformLeaveModel.InformLeave>) response;
                ListView view = (ListView) findViewById(R.id.listInform);
                Map<String,String> unRead = new HashMap<String, String>();
                unRead = myDb.getAllInformLeave();
                List<String> setColor = new ArrayList<String>();
                if(!list.isEmpty()) {
                    view.clearAnimation();
                    List<String> string = new ArrayList<String>();
                    for (InformLeaveModel.InformLeave i : list) {
                        Calendar car = Calendar.getInstance();
                        car.clear();
                        Date date = new Date();
                        Long setDate = Long.parseLong(i.getSchedule().getScheduleDate());
                        date.setTime(setDate);
                        car.setTime(date);
                        i.getSchedule().setScheduleDate(car.get(java.util.Calendar.YEAR) + "-"
                                + (car.get(java.util.Calendar.MONTH) + 1)
                                + "-" + car.get(java.util.Calendar.DAY_OF_MONTH));
                        string.add(i.getStudent().getStudentID() + " " +
                                " " + i.getSchedule().getScheduleDate() + " " + i.getSchedule().getPeriod().getSection().getSubject().getSubjectNumber());
                        if(unRead.get(i.getInformLeaveID()+"") == null){
                            setColor.add("set");

                        }else {
                            setColor.add("off");
                        }
                    }
                    InformViewAdapter informViewAdapter = new InformViewAdapter(ViewListInformLeaveActivity.this,string,setColor,string.size());
                    view.setAdapter(informViewAdapter);
                    progress.dismiss();
                    view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(ViewListInformLeaveActivity.this, ApproveLeaveActivity.class);
                            intent.putExtra("informleave", list.get(i));
                            myDb.addInformRead(list.get(i));
                            index =i;
                            startActivity(intent);
                        }
                    });

                }else {
                    String show [] =  {"ไม่พบนักศึกษาที่ลา"};
                    view.setAdapter(new ArrayAdapter<String>(ViewListInformLeaveActivity.this, android.R.layout.simple_list_item_1, show));
                    progress.dismiss();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
    /************get view in listview*********/
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
