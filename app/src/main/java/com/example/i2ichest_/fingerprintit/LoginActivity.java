package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.FacultyModel;
import com.example.i2ichest_.fingerprintit.model.LoginModel;
import com.example.i2ichest_.fingerprintit.model.MajorModel;
import com.example.i2ichest_.fingerprintit.model.PersonModel;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private GlobalClass gb;
    WSManager wsManager;
    SharedPreferences sp = null;
    final String USER_DETAIL = "USERDETAIL";
    String saveCheck = "saveCheck";
    SharedPreferences.Editor editor;
    AlertDialog.Builder showRex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gb = (GlobalClass) this.getApplicationContext();
        sp = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        editor = sp.edit();
        CheckBox cbRemember = (CheckBox)findViewById(R.id.saveUserDetail);
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(saveCheck, isChecked);
                editor.commit();
            }
        });
        /**This is saving username and password**/
        boolean isRemember = sp.getBoolean(saveCheck, false);
        cbRemember.setChecked(isRemember);
        if(!cbRemember.isChecked()){
            sp.edit().clear().commit();
        }
        /**This is adding user detail to fields**/
        EditText username = (EditText) findViewById(R.id.editTextUsername);
        EditText password = (EditText) findViewById(R.id.editTextPassword);
        username.setText(sp.getString("username",""));
        password.setText(sp.getString("password",""));

        /**This is checking user logout**/
        if(sp.getBoolean("autoLogin",false)){
            onClickLogin(null);

        }

        Intent intent = getIntent();
        String responseFormVerify = intent.getStringExtra("resultVerify");
        if(responseFormVerify!=null) {
            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setIcon(R.drawable.success);
            alertDialog.setTitle("สถานะการลงทะเบียน");
            alertDialog.setMessage(responseFormVerify.toString());
            alertDialog.show();
        }
    }

    public void onClickLogin(View view){
        final LoginModel LOGINMODEL = new LoginModel();
        final EditText USERNAME = (EditText) findViewById(R.id.editTextUsername);
        EditText password = (EditText) findViewById(R.id.editTextPassword);
        /*Regular Expression here*/
        showRex = new AlertDialog.Builder(this);
        showRex.setTitle("สถานะการตวจสอบข้อมูล");
        showRex.setIcon(R.drawable.error);
        String usernamePattern = "([a-zA-Z0-9]){2,20}";
        String passwordPattern = "([a-zA-Z0-9]){2,20}";
        if(!USERNAME.getText().toString().matches(usernamePattern)){
            showRex.setMessage("ข้อมูลผิดพลาด : กรุณาระบุชื่อผู้ใช้ให้ถูกต้อง");
            showRex.create().show();
        }else if (!password.getText().toString().matches(passwordPattern)){
            showRex.setMessage("ข้อมูลผิดพลาด : กรุณาระบุรหัสผ่านให้ถูกต้อง");
            showRex.create().show();
        }else{
            LOGINMODEL.getLogin().setUsername(USERNAME.getText().toString());
            LOGINMODEL.getLogin().setPassword(password.getText().toString());

            final ProgressDialog PROGRESS = ProgressDialog.show(LoginActivity.this,"Please Wait...","Please wait...",true);
            wsManager = WSManager.getWsManager(this);
            wsManager.doLogin(LOGINMODEL, new WSManager.WSManagerListener() {
                @Override
                public void onComplete(Object response) {
                    PROGRESS.dismiss();

                    Map<String, List<String>> map = (Map<String, List<String>>) response;

                    if (!map.isEmpty()) {
                        Log.d("Map<LOGIN> @@ : ", map.get("login").toString());
                        List<String> login = map.get("login");

                        gb.getLoginModel().getLogin().setUsername(login.get(0));
                        gb.getLoginModel().getLogin().setPassword(login.get(1));

                        PersonModel personModel = new PersonModel();
                        personModel.getPerson().setTitle(login.get(2));
                        personModel.getPerson().setFirstName(login.get(3));
                        personModel.getPerson().setLastName(login.get(4));
                        personModel.getPerson().setPersonID(Long.valueOf(login.get(5)));
                        editor.putString("personId", login.get(5));

                        if (login.get(8).equals("student") || login.get(8).equals("teacher")) {
                            if (login.get(8).equals("student")) {
                                personModel.getPerson().setFingerprintData(login.get(9));
                            }

                            MajorModel majorModel = new MajorModel();
                            majorModel.getMajor().setMajorName(login.get(6));

                            FacultyModel facultyModel = new FacultyModel();
                            facultyModel.getFaculty().setFacultyName(login.get(7));

                            majorModel.getMajor().setFaculty(facultyModel.getFaculty());
                            personModel.getPerson().setMajor(majorModel.getMajor());


                        }

                        gb.getLoginModel().getLogin().setPerson(personModel.getPerson());
                        gb.setTypeUser(login.get(8));
                        List<String> allSub = map.get("subject");
                        gb.setAllSubject(allSub);
                        /**เพิ่มตรงนี้เซฟ autologin**/
                        /*save preferences*/
                        CheckBox saveDetail = (CheckBox) findViewById(R.id.saveUserDetail);
                        if (saveDetail.isChecked()) {
                            editor.putString("username", gb.getLoginModel().getLogin().getUsername());
                            editor.putString("password", gb.getLoginModel().getLogin().getPassword());
                            boolean save = editor.commit();
                            Log.d("SAVE USER!", "save user detail: " + save);
                        }

                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    } else {
                        showRex.setTitle("สถานะการค้นหาข้อมูล");
                        showRex.setMessage("กรุณาตรวจสอบข้อมูลใหม่อีกครั้ง");
                        showRex.create().show();
                    }
                }

                @Override
                public void onError(String error) {
                    PROGRESS.dismiss();
                }
            });
        }
    }

    public void onClickVerifyParent(View view){
        Intent intent = new Intent(LoginActivity.this,VerifyStudentParentActivity.class);
        startActivity(intent);
    }
}
