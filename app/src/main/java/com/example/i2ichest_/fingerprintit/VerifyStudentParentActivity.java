package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.ParentModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;

public class VerifyStudentParentActivity extends AppCompatActivity {
    Spinner title;
    EditText firstname;
    EditText lastname;
    EditText studentID;
    EditText phone;
    EditText email;
    WSManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_student_parent);

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
                AlertDialog alertDialog = new AlertDialog.Builder(VerifyStudentParentActivity.this).create();
                alertDialog.setTitle("สถานะการลงทะเบียน");
                alertDialog.setMessage(response.toString());
                alertDialog.show();
            }

            @Override
            public void onError(String error) {
                progress.dismiss();
                Toast.makeText(VerifyStudentParentActivity.this,error,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
