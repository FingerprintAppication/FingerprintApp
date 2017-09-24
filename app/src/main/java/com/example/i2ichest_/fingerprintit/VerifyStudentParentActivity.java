package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.ParentModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;
import java.util.regex.Pattern;

public class VerifyStudentParentActivity extends AppCompatActivity {
    Spinner title;
    EditText firstName;
    EditText lastName;
    EditText studentID;
    EditText phone;
    EditText email;
    WSManager manager;
    AlertDialog.Builder showRex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_student_parent);
        getPersonData();

    }

    public void getPersonData(){
        Button onVerifyParent = (Button)findViewById(R.id.onVerifyParent);
        onVerifyParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRex = new AlertDialog.Builder(VerifyStudentParentActivity.this);
                showRex.setTitle("สถานะการตวจสอบข้อมูล");
                showRex.setIcon(R.drawable.error);
                title = (Spinner)findViewById(R.id.titleSpinner);
                firstName = (EditText)findViewById(R.id.firstNameTxt);
                lastName = (EditText)findViewById(R.id.lastNameTxt);
                studentID = (EditText)findViewById(R.id.studentIdTxt);
                phone = (EditText)findViewById(R.id.phoneTxt);
                email = (EditText)findViewById(R.id.emailTxt);
                boolean space = true;
                String patternName = "([ก-์]){2,30}";
                String patternStudentId = "([0-9]){10}";
                String patternPhone = "([0]){1}([6,8,9]{1}([0-9]{8}))";
                Pattern patternEmail = Patterns.EMAIL_ADDRESS;

                if(!firstName.getText().toString().matches(patternName)){
                    firstName.setBackground(getDrawable(R.drawable.verify_border));
                    showRex.setMessage("ข้อมูลผิดพลาด :  กรณุากรอกชื่อให้ถูกต้อง");
                    space = false;
                }else if(!lastName.getText().toString().matches(patternName)){
                    lastName.setBackground(getDrawable(R.drawable.verify_border));
                    firstName.setBackground(getDrawable(R.color.white));
                    showRex.setMessage("ข้อมูลผิดพลาด :  กรณุากรอกนามสกุลให้ถูกต้อง");
                    space = false;
                }else if(!studentID.getText().toString().matches(patternStudentId)){
                    studentID.setBackground(getDrawable(R.drawable.verify_border));
                    firstName.setBackground(getDrawable(R.color.white));
                    lastName.setBackground(getDrawable(R.color.white));
                    showRex.setMessage("ข้อมูลผิดพลาด :  กรณุากรอกรหัสนักศึกษาให้ถูกต้อง");
                    space = false;
                }else if(!phone.getText().toString().matches(patternPhone)){
                    phone.setBackground(getDrawable(R.drawable.verify_border));
                    firstName.setBackground(getDrawable(R.color.white));
                    lastName.setBackground(getDrawable(R.color.white));
                    studentID.setBackground(getDrawable(R.color.white));
                    showRex.setMessage("ข้อมูลผิดพลาด :  กรณุากรอกเบอร์โทรศัพท์ให้ถูกต้อง");
                    space = false;
                }else if(!email.getText().toString().matches(patternEmail.toString())){
                    email.setBackground(getDrawable(R.drawable.verify_border));
                    firstName.setBackground(getDrawable(R.color.white));
                    lastName.setBackground(getDrawable(R.color.white));
                    studentID.setBackground(getDrawable(R.color.white));
                    phone.setBackground(getDrawable(R.color.white));
                    showRex.setMessage("ข้อมูลผิดพลาด :  กรณุากรอกอีเมล์ให้ถูกต้อง");
                    space = false;
                }else{
                    email.setBackground(getDrawable(R.color.white));
                    firstName.setBackground(getDrawable(R.color.white));
                    lastName.setBackground(getDrawable(R.color.white));
                    studentID.setBackground(getDrawable(R.color.white));
                    phone.setBackground(getDrawable(R.color.white));
                    space = true;
                }


                if(space) {
                    manager = WSManager.getWsManager(VerifyStudentParentActivity.this);
                    final ProgressDialog progress = ProgressDialog.show(VerifyStudentParentActivity.this, "Please Wait...", "Please wait...", true);
                    StudentModel sm = new StudentModel();
                    ParentModel pm = new ParentModel();
                    pm.getParent().setTitle(title.getSelectedItem().toString());
                    pm.getParent().setFirstName(firstName.getText().toString());
                    pm.getParent().setLastName(lastName.getText().toString());
                    pm.getParent().setPhoneNo(phone.getText().toString());
                    pm.getParent().setEmail(email.getText().toString());
                    /*******set data from verify student Parent*********/
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
                }else{
                    showRex.create().show();
                }
            }
        });


        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(VerifyStudentParentActivity.this,"CANCELED ACTIVITY!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


}
