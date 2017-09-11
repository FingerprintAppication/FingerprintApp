
package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
import java.util.regex.Pattern;

public class InformLeaveActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_PERMISSION = 786;
    int GALLERY_REQUEST = 1234;
    GalleryPhoto galleryPhoto;
    InformLeaveModel informLeaveModel ;
    WSManager wsManager;
    List<String> allDate = null;
    ArrayAdapter<String> adapter = null;
    Spinner dateSpinner = null;
    List<String> futureDate = null;
    Base64Model base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_leave);
        base = new Base64Model();
        setInformLeaveData ();
    }

    public void setInformLeaveData () {
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        final ImageButton but = (ImageButton)findViewById(R.id.imageButton);
        final Button informButton = (Button)findViewById(R.id.informSubmit);
        /********get intent data from periodActivity**********/
        final Intent intent = getIntent();
        Log.d("TAG", "##############################################3: "+intent.getLongExtra("subjectID",1L));
        final String studentID = intent.getStringExtra("studentID");
        final String studentName = intent.getStringExtra("studentName");
        final String subject = intent.getStringExtra("subject");
        final String periodId = intent.getStringExtra("periodId");
        final TextView imageTitle = (TextView)findViewById(R.id.imageTitle) ;
        final TextView imageName = (TextView)findViewById(R.id.imageName) ;
        final ImageButton imageButton = (ImageButton)findViewById(R.id.imageButton);
        
        final PeriodModel periodModel = new PeriodModel();
        periodModel.getPeriod().setPeriodID(Long.parseLong(periodId));
        Log.d("TAPO", "setInformLeaveData: periodddddd "+periodModel.getPeriod().getPeriodID());
        final ProgressDialog progress = ProgressDialog.show(InformLeaveActivity.this,"Please Wait...","Please wait...",true);
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
                stuId.setText(studentID);
                stuName.setText(studentName);
                subj.setText(subject);
                stuId.setEnabled(false);
                stuName.setEnabled(false);
                subj.setEnabled(false);
                stuId.setBackgroundResource(R.color.grey);
                stuName.setBackgroundResource(R.color.grey);
                subj.setBackgroundResource(R.color.grey);

                but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestPermission();
                        startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);
                    }
                });
                futureDate();
                imageTitle.setVisibility(View.INVISIBLE);
                imageName.setVisibility(View.INVISIBLE);
                imageButton.setVisibility(View.INVISIBLE);
                adapter = new ArrayAdapter<String>(InformLeaveActivity.this,android.R.layout.simple_list_item_1, futureDate);
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
                            adapter = new ArrayAdapter<String>(InformLeaveActivity.this,android.R.layout.simple_list_item_1, allDate);
                            dateSpinner.setAdapter(adapter);
                        }else {
                            Log.d("onselected", "onItemSelected: future");
                            informLeaveModel.getInformLeave().setSupportDocument("");
                            imageTitle.setVisibility(View.INVISIBLE);
                            imageName.setVisibility(View.INVISIBLE);
                            imageButton.setVisibility(View.INVISIBLE);
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
                        Spinner typeInform = (Spinner)findViewById(R.id.typeOfInform);
                        TextView StudentId = (TextView) findViewById(R.id.studentIdTxt);
                        Spinner date = (Spinner)findViewById(R.id.dateOfInform);
                        EditText description = (EditText)findViewById(R.id.description);

                        informLeaveModel.getInformLeave().setInformType(typeInform.getSelectedItem().toString());
                        StudentModel studentModel = new StudentModel();
                        studentModel.getStudent().setStudentID(Long.parseLong(StudentId.getText().toString()));
                        ScheduleModel scheduleModel = new ScheduleModel();
                        scheduleModel.getSchedule().setScheduleDate(date.getSelectedItem().toString());
                        scheduleModel.getSchedule().setPeriod(periodModel.getPeriod());
                        informLeaveModel.getInformLeave().setStudent(studentModel.getStudent());
                        informLeaveModel.getInformLeave().setDetail(description.getText().toString());
                        informLeaveModel.getInformLeave().setSchedule(scheduleModel.getSchedule());
                        informLeaveModel.getInformLeave().setStatus("รอ");
                        Pattern pattern = Pattern.compile("^([ก-์a-zA-Z]){1}([ก-์a-zA-Z\\s]){4,200}$");
                        boolean check = pattern.matcher(informLeaveModel.getInformLeave().getDetail()).matches();
                        if(!check){
                            Toast.makeText(InformLeaveActivity.this,"โปรดกรอกคำอธิบาย 5 ตัวอักษรขึ้นไป",Toast.LENGTH_SHORT).show();
                        }else{
                            final ProgressDialog progress = ProgressDialog.show(InformLeaveActivity.this,"Please Wait...","Please wait...",true);
                            wsManager.doInsertInformLeave(informLeaveModel, new WSManager.WSManagerListener() {
                                @Override
                                public void onComplete(Object response) {
                                    progress.dismiss();
                                    Intent intentStart = new Intent(InformLeaveActivity.this,PeriodActivity.class);
                                    intentStart.putExtra("subjectID", intent.getLongExtra("subjectID",1L));
                                    intentStart.putExtra("subjectNumber", subject);
                                    intentStart.putExtra("subjectName",intent.getStringExtra("subjectName") );
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
        if(request == GALLERY_REQUEST){
             if(resultC == RESULT_OK) {
                 Uri uri = data.getData();
                 galleryPhoto.setPhotoUri(uri);
                 String photoPath = galleryPhoto.getPath();
                 informLeaveModel.getInformLeave().setSupportDocument(base.encodeBase64(photoPath));
                 final TextView imageName = (TextView)findViewById(R.id.imageName) ;
                 File finalFile = new File(photoPath);
                 imageName.setText(finalFile.getName());
             }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            //openFilePicker();
        }

    }

    public void futureDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        futureDate = new ArrayList<String>();
        for (String s : allDate) {
            try {
                Date date = sdf.parse(s);
                if (date.after(d)) {
                    futureDate.add(s);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}

