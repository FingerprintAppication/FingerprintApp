
package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;
import com.example.i2ichest_.fingerprintit.model.PeriodModel;
import com.example.i2ichest_.fingerprintit.model.ScheduleModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class InformLeaveActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_PERMISSION = 786;
    int GALLERY_REQUEST = 1234;
    GalleryPhoto galleryPhoto;
    InformLeaveModel informLeaveModel ;
    WSManager wsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_leave);
        setInformLeaveData ();
    }

    public void setInformLeaveData () {
        informLeaveModel = new InformLeaveModel();
        wsManager = WSManager.getWsManager(this);
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        ImageButton but = (ImageButton)findViewById(R.id.imageButton);
        Button informButton = (Button)findViewById(R.id.informSubmit);
        /********get intent data from periodActivity**********/
        final Intent intent = getIntent();
        Log.d("TAG", "##############################################3: "+intent.getLongExtra("subjectID",1L));
        final String studentID = intent.getStringExtra("studentID");
        final String studentName = intent.getStringExtra("studentName");
        final String subject = intent.getStringExtra("subject");
        final String periodId = intent.getStringExtra("periodId");
        final PeriodModel periodModel = new PeriodModel();
        periodModel.getPeriod().setPeriodID(Long.parseLong(periodId));
        final TextView imageTitle = (TextView)findViewById(R.id.imageTitle) ;
        final TextView imageName = (TextView)findViewById(R.id.imageName) ;
        final ImageButton imageButton = (ImageButton)findViewById(R.id.imageButton);
        final ProgressDialog progress = ProgressDialog.show(InformLeaveActivity.this,"Please Wait...","Please wait...",true);
        wsManager.doSearchScheduleDate(periodModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                Log.d("arr ",response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    Gson gson = new Gson();
                    /****use this for instead loop****/
                    Type collectionType = new TypeToken<ArrayList<String>>(){}.getType();
                    ArrayList<String> dateArray = gson.fromJson(jsonArray.toString(), collectionType);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(InformLeaveActivity.this,android.R.layout.simple_list_item_1, dateArray);
                    Spinner date = (Spinner)findViewById(R.id.dateOfInform);
                    date.setAdapter(adapter);
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.d("onError ",error);
            }
        });

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
        Spinner typeInform = (Spinner)findViewById(R.id.typeOfInform);
        typeInform.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(adapterView.getAdapter().getItem(i).toString().equals("ลาป่วย")){
                        imageTitle.setVisibility(View.VISIBLE);
                        imageName.setVisibility(View.VISIBLE);
                        imageButton.setVisibility(View.VISIBLE);
                    }else {
                        informLeaveModel.getInformLeave().setSupportDocument("");
                        imageName.setText("ชื่อรูปภาพ");
                        imageTitle.setVisibility(View.INVISIBLE);
                        imageName.setVisibility(View.INVISIBLE);
                        imageButton.setVisibility(View.INVISIBLE);
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
                Log.d("IDDDDDDDDDDD ",StudentId.getText().toString());
                StudentModel studentModel = new StudentModel();
                ScheduleModel scheduleModel = new ScheduleModel();
                scheduleModel.getSchedule().setScheduleDate(date.getSelectedItem().toString());
                studentModel.getStudent().setStudentID(Long.parseLong(StudentId.getText().toString()));
                scheduleModel.getSchedule().setPeriod(periodModel.getPeriod());
                informLeaveModel.getInformLeave().setStudent(studentModel.getStudent());
                informLeaveModel.getInformLeave().setDetail(description.getText().toString());
                informLeaveModel.getInformLeave().setSchedule(scheduleModel.getSchedule());
                informLeaveModel.getInformLeave().setStatus("รอ");
                Pattern pattern = Pattern.compile("^([ก-์a-zA-Z]){1}([ก-์a-zA-Z\\ ]){4,200}$");
                boolean check = pattern.matcher(informLeaveModel.getInformLeave().getDetail()).matches();
                if(!check){
                    Toast.makeText(InformLeaveActivity.this,"โปรดกรอกคำอธิบาย 5 ตัวอักษรขึ้นไป",Toast.LENGTH_SHORT).show();
                } /*else if(informLeaveModel.getInformLeave().getInformType().equals("ลาป่วย")&& informLeaveModel.getInformLeave().getSupportDocument()=="") {
                    Toast.makeText(InformLeaveActivity.this,"โปรดเลือกรูปภาพ",Toast.LENGTH_SHORT).show();
                }*/else{
                    final ProgressDialog progress = ProgressDialog.show(InformLeaveActivity.this,"Please Wait...","Please wait...",true);
                    wsManager.informLeave(informLeaveModel, new WSManager.WSManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            progress.dismiss();
                            Intent intentStart = new Intent(InformLeaveActivity.this,PeriodActivity.class);
                            intentStart.putExtra("subjectID", intent.getLongExtra("subjectID",1L));
                            intentStart.putExtra("subjectNumber", subject);
                            intentStart.putExtra("subjectName",intent.getStringExtra("subjectName") );
                            intentStart.putExtra("resultInform",response.toString());

                            Toast.makeText(InformLeaveActivity.this, "GETINTENT !"+intent.getStringExtra("subjectNumber")+" "+intent.getStringExtra("subjectName"), Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(intentStart);
                            Toast.makeText(InformLeaveActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
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
    public void onActivityResult (int request,int resultC,Intent data){
        if(request == GALLERY_REQUEST){
             if(resultC == RESULT_OK) {
                 Uri uri = data.getData();
                 galleryPhoto.setPhotoUri(uri);
                 String photoPath = galleryPhoto.getPath();
                 informLeaveModel.getInformLeave().setSupportDocument(encodeBase64(photoPath));
                 final TextView imageName = (TextView)findViewById(R.id.imageName) ;
                 File finalFile = new File(photoPath);
                 imageName.setText(finalFile.getName());
             }
        }
    }

    public String encodeBase64 (String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOut);
        byte[] byteArray = byteArrayOut.toByteArray();

        return Base64.encodeToString(byteArray,Base64.DEFAULT);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            //openFilePicker();
        }

    }
}

