package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import com.example.i2ichest_.fingerprintit.DBHelper.DatabaseHelper;
import com.example.i2ichest_.fingerprintit.adapter.InformViewAdapter;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewListInformLeaveActivity extends AppCompatActivity {
    WSManager wsManager;
    DatabaseHelper myDb;
    int index = 0;
    private GlobalClass gb;
    /*for keeping big images code*/
    List<String> images;
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_inform_leave);
        gb = (GlobalClass) this.getApplicationContext();
        toolBar = (Toolbar)findViewById(R.id.profile);
        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);
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

    public void showInformLeave () {
        myDb = new DatabaseHelper(this);
        wsManager = WSManager.getWsManager(this);
        Intent intent = getIntent();
        String personId = intent.getStringExtra("personId");
        String result = intent.getStringExtra("result");
        if(result != null){
            AlertDialog alertDialog = new AlertDialog.Builder(ViewListInformLeaveActivity.this).create();
            alertDialog.setTitle("สถานะการยืนยันการลาเรียน");
            alertDialog.setIcon(getDrawable(R.drawable.success));
            alertDialog.setMessage(result);
            alertDialog.show();
        }
        final ProgressDialog progress = ProgressDialog.show(this,"Please Wait...","Please wait...",true);
        wsManager.searchInformLeaveForTeacher(personId, new WSManager.WSManagerListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(Object response) {
                final List<InformLeaveModel> list =(List<InformLeaveModel>) response;
                ListView view = (ListView) findViewById(R.id.listInform);
                Map<String,String> unRead = new HashMap<String, String>();
                try {
                    unRead = myDb.getAllInformLeave();
                }catch(Exception s){
                    s.getMessage();
                    myDb.createTable();
                }
                List<String> setColor = new ArrayList<String>();
                    if(!list.isEmpty()) {
                        view.clearAnimation();
                        List<String> string = new ArrayList<String>();
                        images = new ArrayList<String>();
                        for (InformLeaveModel i : list) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            string.add(i.getInformLeave().getStudent().getStudentID() + " วิชา: " + i.getInformLeave().getSchedule().getPeriod().getSection().getSubject().getSubjectNumber()
                                    +" "+i.getInformLeave().getSchedule().getPeriod().getSection().getSubject().getSubjectName()+" \nวันที่ลา: " + sdf.format(i.getInformLeave().getSchedule().getScheduleDate())
                                    +" สถานะ: ["+ i.getInformLeave().getStatus()+"]");
                            if(unRead.get(i.getInformLeave().getInformLeaveID()+"") == null){
                                setColor.add("set");
                            }else {
                                setColor.add("off");
                            }
                            images.add(i.getInformLeave().getSupportDocument());
                    }
                    InformViewAdapter informViewAdapter = new InformViewAdapter(ViewListInformLeaveActivity.this,string,setColor,string.size());
                    view.setAdapter(informViewAdapter);

                    view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(ViewListInformLeaveActivity.this, ApproveLeaveActivity.class);
                            gb.setLargeImage(images.get(i));
                            list.get(i).getInformLeave().setSupportDocument("");
                            intent.putExtra("informleave", list.get(i).getInformLeave());
                            try{
                                myDb.addInformRead(list.get(i).getInformLeave());
                                index =i;
                                startActivity(intent);
                            }catch(Exception s) {
                                Log.d("TAG", "onItemClick: "+s.getMessage());
                            }
                        }
                    });
                }else {
                    String show [] =  {"ไม่พบนักศึกษาที่ลา"};
                    view.setAdapter(new ArrayAdapter<String>(ViewListInformLeaveActivity.this, android.R.layout.simple_list_item_1, show));

                }
                progress.dismiss();
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

    public void onDestroy(){
        super.onDestroy();
        gb.setLargeImage("");
    }
}
