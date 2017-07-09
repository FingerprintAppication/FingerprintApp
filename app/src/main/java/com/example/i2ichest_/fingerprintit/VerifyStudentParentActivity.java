package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.ParentModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;
import com.example.i2ichest_.fingerprintit.task.WSTaskPost;

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
                    if(!response.toString().equalsIgnoreCase("student not found")) {
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
                        result.setText("Student not found");
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
