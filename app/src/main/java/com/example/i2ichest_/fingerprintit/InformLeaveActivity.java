
package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.Base64Model;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;
import com.example.i2ichest_.fingerprintit.model.PeriodModel;
import com.example.i2ichest_.fingerprintit.model.ScheduleModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class InformLeaveActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private static final int GALLERY_REQUEST = 1234;
    GalleryPhoto galleryPhoto;
    InformLeaveModel informLeaveModel ;
    WSManager wsManager;
    List<String> allDate = null;
    ArrayAdapter<String> adapter = null;
    Spinner dateSpinner = null;
    List<String> futureDate = null;
    Base64Model base;
    SimpleDateFormat sdf;
    SimpleDateFormat sdfAdd;
    GlobalClass gb;
    Toolbar toolBar;
    List<String> changeFormatDate;
    EditText description;
    ImageButton imageButton;
    Button informButton;
    Button informCancel;
    String studentID;
    String studentName;
    String periodId;
    TextView imageTitle;
    TextView imageName;
    String subject;
    PeriodModel periodModel;
    ProgressDialog progress;
    AlertDialog.Builder showRex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_leave);
        base = new Base64Model();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        sdfAdd = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        gb = (GlobalClass) this.getApplicationContext();
        toolBar = (Toolbar)findViewById(R.id.profile);
        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        setInformLeaveData ();
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

    public void setInformLeaveData () {
        progress = ProgressDialog.show(InformLeaveActivity.this,"Please Wait...","Please wait...",true);
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        informButton = (Button)findViewById(R.id.informSubmit);
        informCancel = (Button)findViewById(R.id.informCancel);
        /********get intent data from periodActivity**********/
        Intent intent = getIntent();
        Log.d("TAG", "##############################################3: "+intent.getLongExtra("subjectID",1L));
        studentID = intent.getStringExtra("studentID");
        studentName = intent.getStringExtra("studentName");
        subject = intent.getStringExtra("subject");
        periodId = intent.getStringExtra("periodId");
        imageTitle = (TextView)findViewById(R.id.imageTitle) ;
        imageName = (TextView)findViewById(R.id.imageName) ;
        periodModel = new PeriodModel();
        periodModel.getPeriod().setPeriodID(Long.parseLong(periodId));

        allDate = new ArrayList<String>();
        wsManager = WSManager.getWsManager(this);
        wsManager.doSearchDateOfInformLeave(periodModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                allDate = (List<String>)response;
                informLeaveModel = new InformLeaveModel();
                Log.d("SIZE ", "onComplete: "+ allDate.size());
                dateSpinner = (Spinner)findViewById(R.id.dateOfInform);
                TextView stuId = (TextView)findViewById(R.id.studentIdTxt);
                TextView stuName = (TextView)findViewById(R.id.studentNameTxt);
                TextView subj = (TextView)findViewById(R.id.subjectTxt);
                description = (EditText)findViewById(R.id.description);
                final TextView DESCRIPT_TEXTVIEW = (TextView)findViewById(R.id.descriptTextView);
                stuId.setText(studentID);
                stuName.setText(studentName);
                subj.setText(subject);
                stuId.setEnabled(false);
                stuName.setEnabled(false);
                subj.setEnabled(false);
                stuId.setBackgroundResource(R.color.grey);
                stuName.setBackgroundResource(R.color.grey);
                subj.setBackgroundResource(R.color.grey);

                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestPermission();
                        startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);
                    }
                });
                changeFormatDate = new ArrayList<String>();
                for (String s : allDate) {
                    try {
                        Date date = sdf.parse(s);
                        changeFormatDate.add(sdfAdd.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new ArrayAdapter<String>(InformLeaveActivity.this,android.R.layout.simple_list_item_1, changeFormatDate);
                dateSpinner.setAdapter(adapter);
                Spinner typeInform = (Spinner)findViewById(R.id.typeOfInform);
                typeInform.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d("TAG TYPES ", ""+adapterView.getAdapter().getItem(i).toString());
                        if(adapterView.getAdapter().getItem(i).toString().equals("ลาป่วย")){
                            imageName.setText("ชื่อรูปภาพ");
                            imageTitle.setVisibility(View.VISIBLE);
                            imageName.setVisibility(View.VISIBLE);
                            imageButton.setVisibility(View.VISIBLE);
                            DESCRIPT_TEXTVIEW.setVisibility(View.VISIBLE);
                            description.setVisibility(View.VISIBLE);
                            informButton.setVisibility(View.VISIBLE);
                            informCancel.setVisibility(View.VISIBLE);
                            dateSpinner.setEnabled(true);
                            adapter = new ArrayAdapter<String>(InformLeaveActivity.this,android.R.layout.simple_list_item_1, changeFormatDate);
                            dateSpinner.setAdapter(adapter);
                        }else {
                            Log.d("onselected", "onItemSelected: future");
                            informLeaveModel.getInformLeave().setSupportDocument("");
                            imageTitle.setVisibility(View.INVISIBLE);
                            imageName.setVisibility(View.INVISIBLE);
                            imageButton.setVisibility(View.INVISIBLE);



                            futureDate();
                            //futureDate.clear();
                            if(futureDate.isEmpty()){
                                futureDate.add("สุดสุดการเรียน");
                                dateSpinner.setEnabled(false);
                                DESCRIPT_TEXTVIEW.setVisibility(View.INVISIBLE);
                                description.setVisibility(View.INVISIBLE);
                                informButton.setVisibility(View.INVISIBLE);
                                informCancel.setVisibility(View.INVISIBLE);
                            }else{
                                dateSpinner.setEnabled(true);
                                DESCRIPT_TEXTVIEW.setVisibility(View.VISIBLE);
                                description.setVisibility(View.VISIBLE);
                                informButton.setVisibility(View.VISIBLE);
                                informCancel.setVisibility(View.VISIBLE);
                            }
                            adapter = new ArrayAdapter<String>(InformLeaveActivity.this,android.R.layout.simple_list_item_1, futureDate);
                            dateSpinner.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                informButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showRex = new AlertDialog.Builder(InformLeaveActivity.this);
                        showRex.setTitle("สถานะการตวจสอบข้อมูล");
                        showRex.setIcon(R.drawable.error);
                        String caseDetailRex = "([ก-์a-zA-Z0-9]){5,150}";
                        boolean check = description.getText().toString().matches(caseDetailRex);
                        if(!check){
                            showRex.setMessage("ข้อมูลผิดพลาด :  กรณุากรอกคำอธิบายให้ถูกต้อง");
                            showRex.create().show();
                        }else{
                            showRex.setTitle("ยืนยันการลาเรียน");
                            showRex.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    progress = ProgressDialog.show(InformLeaveActivity.this,"Please Wait...","Please wait...",true);
                                    Spinner date = (Spinner)findViewById(R.id.dateOfInform);
                                    Spinner typeInform = (Spinner)findViewById(R.id.typeOfInform);
                                    TextView StudentId = (TextView) findViewById(R.id.studentIdTxt);
                                    informLeaveModel.getInformLeave().setInformType(typeInform.getSelectedItem().toString());
                                    StudentModel studentModel = new StudentModel();
                                    studentModel.getStudent().setStudentID(Long.parseLong(StudentId.getText().toString()));
                                    ScheduleModel scheduleModel = new ScheduleModel();
                                    /*Parsing Date here*/
                                    try {
                                        Date parseDate = sdfAdd.parse(date.getSelectedItem().toString());

                                        Log.d("PARSING ", parseDate.getTime()+"");
                                        scheduleModel.getSchedule().setScheduleDate(parseDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    scheduleModel.getSchedule().setPeriod(periodModel.getPeriod());
                                    informLeaveModel.getInformLeave().setStudent(studentModel.getStudent());
                                    informLeaveModel.getInformLeave().setDetail(description.getText().toString());
                                    informLeaveModel.getInformLeave().setSchedule(scheduleModel.getSchedule());
                                    informLeaveModel.getInformLeave().setStatus("รอ");
                                    wsManager.doInsertInformLeave(informLeaveModel, new WSManager.WSManagerListener() {
                                        @Override
                                        public void onComplete(Object response) {
                                            progress.dismiss();
                                            Intent intentStart = new Intent(InformLeaveActivity.this,ViewListLeaveHistoryActivity.class);
                                            intentStart.putExtra("personId",gb.getLoginModel().getLogin().getPerson().getPersonID());
                                            intentStart.putExtra("resultInform",response.toString());
                                            finish();
                                            startActivity(intentStart);
                                        }

                                        @Override
                                        public void onError(String error) {
                                            Log.d("onError ", error.toString());
                                        }
                                    });
                                }
                            });
                            showRex.create().show();
                        }
                    }
                });
            }

            @Override
            public void onError(String error) {
                Log.d("onError ",error);
            }
        });

        progress.dismiss();
    }

    @Override
    public void onActivityResult (int request,int resultC,Intent data){
        try{
            if(request == GALLERY_REQUEST){
                if(resultC == RESULT_OK) {
                    Uri uri = data.getData();
                    galleryPhoto.setPhotoUri(uri);
                    String photoPath = galleryPhoto.getPath();
                    informLeaveModel.getInformLeave().setSupportDocument(base.encodeBase64(photoPath));
                    TextView imageName = (TextView)findViewById(R.id.imageName) ;
                    File finalFile = new File(photoPath);
                    imageName.setText(finalFile.getName());
                }
            }
        }catch(Exception s){
            s.getMessage();
        }

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        }
    }

    public void futureDate() {
        Date d = new Date();
        d.setMonth(7);
        futureDate = new ArrayList<String>();
        for (String s : allDate) {
            Log.d("ListDate ", "futureDate: "+s);
            try {
                Date date = sdf.parse(s);
                Log.d("FUTUREDATE", "futureDate: "+date.getTime());
                if (date.after(d)) {

                    futureDate.add(sdfAdd.format(date));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}

