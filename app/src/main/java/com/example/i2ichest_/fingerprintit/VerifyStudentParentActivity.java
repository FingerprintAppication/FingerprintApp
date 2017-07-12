package com.example.i2ichest_.fingerprintit;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.AlarmReceiver;
import com.example.i2ichest_.fingerprintit.model.ParentModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;
import com.example.i2ichest_.fingerprintit.task.WSTaskPost;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class VerifyStudentParentActivity extends AppCompatActivity {
    String splitName[];
    StudentModel studentModelResponse;
    EditText name;
    EditText studentID;
    EditText phone;
    EditText email;
    WSManager manager;
    TextView result;
    String inputPhone;
    String databasePhone;
    String resultCheck;
    ParentModel parentModel;
    WSTaskPost wsPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_student_parent);

    }
    public void onStart(){
        super.onStart();
        Intent alarmIntent = new Intent(VerifyStudentParentActivity.this, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(VerifyStudentParentActivity.this, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        /* Set the alarm to start at 00:00 AM */
        Calendar Time = Calendar.getInstance();
        Time.set(Calendar.SECOND, 0);
        Time.set(Calendar.MINUTE, 12);
        Time.set(Calendar.HOUR_OF_DAY, 0);
        Log.d("current ",Time.getTime()+"");
        /* Repeating on every day minutes interval */
        Intent intent = new Intent(this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1253, intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, Time.getTimeInMillis(),pendingIntent );
        Toast.makeText(this, "Alarm Set.", Toast.LENGTH_LONG).show();
        /*Notification*/

    }



    public void getPersonData(View view){
        name = (EditText)findViewById(R.id.nametxt);
        studentID = (EditText)findViewById(R.id.studentId);
        phone = (EditText)findViewById(R.id.phone);
        email = (EditText)findViewById(R.id.email);
        manager = WSManager.getWsManager(this);
        final StudentModel studentModel = new StudentModel();
        final ProgressDialog progress = ProgressDialog.show(VerifyStudentParentActivity.this,"Please Wait...","Please wait...",true);
        studentModel.getStudent().setStudentID(Long.parseLong(studentID.getText().toString()));
        studentModel.getStudent().setParentPhone(phone.getText().toString());

        manager.doVerifyStudentParent(studentModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                progress.dismiss();

                    result = (TextView) findViewById(R.id.result);
                    if(!response.toString().equalsIgnoreCase("ไม่พบรหัสนักศึกษาในฐานข้อมูล")) {
                        studentModelResponse = (StudentModel)response;
                        inputPhone = phone.getText().toString();
                        databasePhone = studentModelResponse.getStudent().getParentPhone();
                        resultCheck = studentModelResponse.getStudent().checkStudentIdAndPhone(inputPhone, databasePhone);
                        result.setText(resultCheck);
                        if (resultCheck == "") {
                            parentModel = new ParentModel();
                            splitName = name.getText().toString().split(" ");
                            parentModel.getParent().setFirstName(splitName[0]);
                            parentModel.getParent().setLastName(splitName[1]);
                            parentModel.getParent().setPhoneNo(inputPhone);
                            parentModel.getParent().setEmail(email.getText().toString());
                            parentModel.getParent().setTitle(studentID.getText().toString());
                            manager.verifyParent(parentModel, new WSManager.WSManagerListener() {
                                @Override
                                public void onComplete(Object response) {
                                        Log.d("result ",response.toString());
                                    if("duplicate".equals(response.toString())){
                                        result.setText("รหัสนักศึกษานี้ได้มีการลงทะเบียนแล้ว");
                                    } else {
                                        result.setText("ลงทะเบียนสำเหร็จ");
                                    }
                                }

                                @Override
                                public void onError(String error) {

                                }
                            });

                        }
                    }else{
                        result.setText("ไม่พบรหัสนักศึกษาในฐานข้อมูล");
                    }
            }

            @Override
            public void onError(String error) {
                progress.dismiss();
                Toast.makeText(VerifyStudentParentActivity.this,error,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
