package com.example.i2ichest_.fingerprintit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.AlarmReceiver;
import com.example.i2ichest_.fingerprintit.model.ParentModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;
import com.example.i2ichest_.fingerprintit.task.WSTaskPost;

import java.util.Calendar;

public class VerifyStudentParentActivity extends AppCompatActivity {
    String splitName[];
    StudentModel studentModelResponse;
    Spinner title;
    EditText firstname;
    EditText lastname;
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



    public void getPersonData(View view) throws Exception{
        title = (Spinner)findViewById(R.id.titleSpinner);
        firstname = (EditText)findViewById(R.id.firstNameTxt);
        lastname = (EditText)findViewById(R.id.lastNameTxt);
        studentID = (EditText)findViewById(R.id.studentIdTxt);
        phone = (EditText)findViewById(R.id.phoneTxt);
        email = (EditText)findViewById(R.id.emailTxt);
        manager = WSManager.getWsManager(this);
        final ProgressDialog progress = ProgressDialog.show(VerifyStudentParentActivity.this,"Please Wait...","Please wait...",true);
        StudentModel sm = new StudentModel();
        ParentModel pm = new ParentModel();
        pm.getParent().setTitle(title.getSelectedItem().toString());
        pm.getParent().setFirstName(firstname.getText().toString());
        pm.getParent().setLastName(lastname.getText().toString());
        pm.getParent().setPhoneNo(phone.getText().toString());
        pm.getParent().setEmail(email.getText().toString());
        /*******set data from verifystudentParent*********/
        sm.getStudent().setStudentID(Long.parseLong(studentID.getText().toString()));
        sm.getStudent().setParent(pm.getParent());
        manager.verifyParent(sm, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                progress.dismiss();
                result = (TextView) findViewById(R.id.resultVerify);
                result.setText(response.toString());
            }

            @Override
            public void onError(String error) {
                progress.dismiss();
                Toast.makeText(VerifyStudentParentActivity.this,error,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
