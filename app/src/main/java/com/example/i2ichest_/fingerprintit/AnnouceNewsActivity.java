package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.AnnouceNewsModel;
import com.example.i2ichest_.fingerprintit.model.PeriodModel;
import com.example.i2ichest_.fingerprintit.model.ScheduleModel;
import com.example.i2ichest_.fingerprintit.model.TeacherModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnnouceNewsActivity extends AppCompatActivity {
    WSManager wsManager;
    private GlobalClass gb;
    SimpleDateFormat sdf;
    SimpleDateFormat sdfAdd;
    AlertDialog.Builder showRex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annouce_news);
        gb = (GlobalClass) this.getApplicationContext();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        sdfAdd = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        showAnnouceNews();
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

    public void showAnnouceNews() {
        final Intent intent = getIntent();
        final long periodID = intent.getLongExtra("periodID", 1L);
        final long subjectID = intent.getLongExtra("subjectID", 1L);
        final String subjectName = intent.getStringExtra("subjectName");
        final String subjectNumber = intent.getStringExtra("subjectNumber");
        final String subjectType = intent.getStringExtra("subjectType");
        String subjectDay = intent.getStringExtra("subjectDay");

        TextView txtSubID = (TextView) findViewById(R.id.textViewSubID);
        TextView txtSubName = (TextView) findViewById(R.id.textViewSubName);
        TextView txtSubType = (TextView) findViewById(R.id.textViewSubType);
        TextView txtSubDay = (TextView) findViewById(R.id.textViewSubDay);
        final TextView txtDateNow = (TextView) findViewById(R.id.dateNow);

        txtSubID.setText(subjectNumber);
        txtSubName.setText(subjectName);
        txtSubType.setText(subjectType);
        txtSubDay.setText(subjectDay);

        Date d = new Date();
        txtDateNow.setText("[ " + sdfAdd.format(d) + " ]");

        final Spinner spType = (Spinner) findViewById(R.id.spinnerNewsType);
        String[] type = {"ทั่วไป", "ยกเลิกคาบเรียน"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AnnouceNewsActivity.this, android.R.layout.simple_list_item_1, type);
        spType.setAdapter(adapter);

        final ProgressDialog progress = ProgressDialog.show(AnnouceNewsActivity.this, "Please Wait...", "Please wait...", true);
        wsManager = WSManager.getWsManager(this);

        final PeriodModel periodModel = new PeriodModel();
        periodModel.getPeriod().setPeriodID(periodID);
        final Spinner spDate = (Spinner) findViewById(R.id.spinnerNewsDate);
        wsManager.doSearchScheduleDate(periodModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                progress.dismiss();
                List<String> listDate = (List<String>) response;

                if (listDate.size() == 0){
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AnnouceNewsActivity.this);
                    alertDialog.setMessage("ไม่พบข้อมูลวันที่คาบเรียน");
                    alertDialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            Intent intent = new Intent(AnnouceNewsActivity.this,PeriodActivity.class);
                            intent.putExtra("subjectID",subjectID);
                            intent.putExtra("subjectNumber",subjectNumber);
                            intent.putExtra("subjectName",subjectName);
                            startActivity(intent);
                        }
                    });

                    alertDialog.show();
                } else {
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AnnouceNewsActivity.this, android.R.layout.simple_list_item_1, listDate);
                    spDate.setAdapter(adapter2);
                }
            }

            @Override
            public void onError(String error) {
                progress.dismiss();
            }
        });

        Button btn = (Button) findViewById(R.id.buttonAddNews);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRex = new AlertDialog.Builder(AnnouceNewsActivity.this);
                showRex.setTitle("สถานะการตวจสอบข้อมูล");
                showRex.setIcon(R.drawable.error);
                String patternName = "([ก-์a-zA-Z0-9]){5,150}";
                final EditText editText = (EditText) findViewById(R.id.editTextNewsDetail);
                if (!editText.getText().toString().matches(patternName)){
                    showRex.setMessage("ข้อมูลผิดพลาด : กรุณากรอกรายละเอียดให้ ถูกต้อง");
                    showRex.create().show();
                    //Toast.makeText(AnnouceNewsActivity.this, "กรุณากรอกรายละเอียด", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AnnouceNewsActivity.this);
                    alertDialog.setMessage("ยืนยันการประกาศข่าว");
                    alertDialog.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            AnnouceNewsModel annouceNewsModel = new AnnouceNewsModel();
                            annouceNewsModel.getAnnouceNews().setAnnouceNewsType(spType.getSelectedItem().toString());
                            annouceNewsModel.getAnnouceNews().setDetail(editText.getText().toString());

                            TeacherModel teacherModel = new TeacherModel();
                            teacherModel.getTeacher().setPersonID(gb.getLoginModel().getLogin().getPerson().getPersonID());
                            annouceNewsModel.getAnnouceNews().setTeacher(teacherModel.getTeacher());

                            ScheduleModel scheduleModel = new ScheduleModel();
                            scheduleModel.getSchedule().setPeriod(periodModel.getPeriod());
                            String dateSelected = spDate.getSelectedItem().toString();
                            /*Parsing Date here*/
                            try {
                                Date date = sdfAdd.parse(dateSelected);
                                scheduleModel.getSchedule().setScheduleDate(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            final ProgressDialog progress = ProgressDialog.show(AnnouceNewsActivity.this, "Please Wait...", "Please wait...", true);
                            annouceNewsModel.getAnnouceNews().setSchedule(scheduleModel.getSchedule());
                            wsManager.doAddAnnouceNews(annouceNewsModel, new WSManager.WSManagerListener() {
                                @Override
                                public void onComplete(Object response) {
                                    progress.dismiss();
                                    if (response.toString().equals("1")) {
                                        Toast.makeText(AnnouceNewsActivity.this, "ประกาศข่าวสำเร็จ", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(AnnouceNewsActivity.this, "ข้อมูลผิดพลาด \nกรุณาตรวจสอบข้อมูลอีกครั้ง", Toast.LENGTH_LONG).show();
                                    }
                                    Intent intent = new Intent(AnnouceNewsActivity.this, ViewListSubjectActivity.class);
                                    intent.putExtra("personID", gb.getLoginModel().getLogin().getPerson().getPersonID());
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(String error) {
                                    progress.dismiss();
                                }
                            });
                        }
                    });

                    alertDialog.show();

                }
            }
        });
    }
}

