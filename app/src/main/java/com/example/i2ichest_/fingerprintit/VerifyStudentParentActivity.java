package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.ParentModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        boolean space = true;
        String patterns = "([ก-์a-zA-Z]){2,}";
        String patternStudent = "([0-9]){10}";
        String patternPhone = "([0]){1}([6-9]{1}([0-9]{8}))";

        if(!firstname.getText().toString().matches(patterns)){
            firstname.setBackground(getDrawable(R.drawable.verify_border));
            space = false;
        }else if(!lastname.getText().toString().matches(patterns)){
            lastname.setBackground(getDrawable(R.drawable.verify_border));
            firstname.setBackground(getDrawable(R.color.white));
            space = false;
        }else if(!studentID.getText().toString().matches(patternStudent)){
            studentID.setBackground(getDrawable(R.drawable.verify_border));
            lastname.setBackground(getDrawable(R.color.white));
            space = false;
        }else if(!phone.getText().toString().matches(patternPhone)){
            phone.setBackground(getDrawable(R.drawable.verify_border));
            studentID.setBackground(getDrawable(R.color.white));
            space = false;
        }else if(!email.getText().toString().matches(patterns)){
            email.setBackground(getDrawable(R.drawable.verify_border));
            phone.setBackground(getDrawable(R.color.white));
            space = false;
        }else{
            email.setBackground(getDrawable(R.color.white));
            firstname.setBackground(getDrawable(R.color.white));
            lastname.setBackground(getDrawable(R.color.white));
            studentID.setBackground(getDrawable(R.color.white));
            phone.setBackground(getDrawable(R.color.white));
            space = true;
        }

        if(space) {
            manager = WSManager.getWsManager(this);
            final ProgressDialog progress = ProgressDialog.show(VerifyStudentParentActivity.this, "Please Wait...", "Please wait...", true);
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

                    if(!"ลงทะเบียนเสร็จสมบูรณ์  ชื่อผู้ใช้งานและรหัสผ่านคือ หมายเลขโทรศัพท์ของคุณ".equals(response.toString())){
                        AlertDialog alertDialog = new AlertDialog.Builder(VerifyStudentParentActivity.this).create();
                        alertDialog.setIcon(R.drawable.error);
                        alertDialog.setTitle("สถานะการลงทะเบียน");
                        alertDialog.setMessage(response.toString());
                        alertDialog.show();
                    }else{
                        Intent intent = new Intent(VerifyStudentParentActivity.this,LoginActivity.class);
                        intent.putExtra("resultVerify",response.toString());
                        finish();
                        startActivity(intent);
                    }
                }

                @Override
                public void onError(String error) {
                    progress.dismiss();
                    Toast.makeText(VerifyStudentParentActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
