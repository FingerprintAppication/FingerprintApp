package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.PersonModel;
import com.example.i2ichest_.fingerprintit.model.StudentModel;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private GlobalClass gb;
    SharedPreferences sp = null;
    final String USER_DETAIL = "USERDETAIL";
    SharedPreferences.Editor editor;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        gb = (GlobalClass) this.getApplicationContext();
        sp = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        editor = sp.edit();
        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton) ;
        editor.putBoolean("autoLogin", true).commit();
        if(gb.getTypeUser().equals("student")){
            for (String s : gb.getAllSubject()) {
                FirebaseMessaging.getInstance().subscribeToTopic(s);
                Log.d("TAG", "subscribeTopic "+s);
            }
        }
        setTextProfile();
    }

    public void setTextProfile(){
        String title = gb.getLoginModel().getLogin().getPerson().getTitle();
        String firstName = gb.getLoginModel().getLogin().getPerson().getFirstName();
        String lastName = gb.getLoginModel().getLogin().getPerson().getLastName();
        String sumStr = title + " " + firstName + " " + lastName;

        TextView name = (TextView) findViewById(R.id.textPT);
        TextView major = (TextView) findViewById(R.id.textMJ);
        TextView stuID = (TextView) findViewById(R.id.textSID);
        TextView fingerID = (TextView) findViewById(R.id.textFD);

        name.setText(sumStr);

        String typeUser = gb.getTypeUser();
        if (!typeUser.equals("parent")){
            major.setText(gb.getLoginModel().getLogin().getPerson().getMajor().getMajorName());
            fab.setVisibility(View.INVISIBLE);
        }

        if (typeUser.equals("student")){
            stuID.setText(gb.getLoginModel().getLogin().getUsername());
            fingerID.setText(gb.getLoginModel().getLogin().getPerson().getFingerprintData());
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProfileActivity.this,ViewListAnnounceNews.class));
                }
            });

        } else if (typeUser.equals("teacher") ||typeUser.equals("parent")) {
            if (gb.getTypeUser().equals("parent")){
                major.setText("");
                TextView m = (TextView) findViewById(R.id.textMajor);
                m.setText("");
                Button btn = (Button) findViewById(R.id.buttonViewInform);
                btn.setVisibility(View.GONE);
                fab.setVisibility(View.INVISIBLE);
            }
            stuID.setText("");
            fingerID.setText("");
            Button btnViewInform = (Button) findViewById(R.id.buttonViewInform);
            btnViewInform.setText("รายชื่อนักศึกษาที่ลา");

            TextView sid = (TextView) findViewById(R.id.textStudentID);
            TextView finger = (TextView) findViewById(R.id.textFingerID);
            sid.setText("");
            finger.setText("");
        }


    }

    public void onClickBtnViewListSubject(View view){
        WSManager wsManager;
        final ProgressDialog progress = ProgressDialog.show(ProfileActivity.this,"Please Wait...","Please wait...",true);
        wsManager = WSManager.getWsManager(this);

        PersonModel personModel = new PersonModel();
        personModel.getPerson().setPersonID(gb.getLoginModel().getLogin().getPerson().getPersonID());
        wsManager.doSearchStudentParent(personModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                progress.dismiss();

                gb.getListStudent().clear();
                List<StudentModel.Student> listStudent = (List<StudentModel.Student>) response;

                for(StudentModel.Student i : listStudent) {
                    gb.getListStudent().add(i);
                }
                Log.d("LIST STUDENT ADD ", gb.getListStudent().toString());

                Intent intent = null;
                //Log.d("Size ", listStudent.size() + "");
                if ( gb.getListStudent().size() > 1) {
                    intent = new Intent(ProfileActivity.this, SelectStudentParentActivity.class);
                } else if (gb.getTypeUser().equals("parent")) {
                    intent = new Intent(ProfileActivity.this, ViewListSubjectActivity.class);
                    intent.putExtra("personID", gb.getListStudent().get(0).getPersonID());
                    gb.setParentStudent(gb.getListStudent().get(0));
                } else {
                    intent = new Intent(ProfileActivity.this, ViewListSubjectActivity.class);
                    intent.putExtra("personID", gb.getLoginModel().getLogin().getPerson().getPersonID());
                }
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                progress.dismiss();
            }
        });

    }

    public void onClickListInformLeave (View view) {
        if("teacher".equals(gb.getTypeUser().toString())) {
            Intent intent = new Intent(this, ViewListInformLeaveActivity.class);
            intent.putExtra("personId", gb.getLoginModel().getLogin().getPerson().getPersonID().toString());
            startActivity(intent);
        } else  if ("student".equals(gb.getTypeUser().toString())){
            Intent intent = new Intent(this,ViewListLeaveHistoryActivity.class);
            intent.putExtra("personId", gb.getLoginModel().getLogin().getPerson().getPersonID());
            startActivity(intent);
        }

        Log.d("PERSON IDD " , gb.getLoginModel().getLogin().getPerson().getPersonID().toString());
    }

    public void logOut (View view) {
        for (String s : gb.getAllSubject()) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(s);
            Log.d("TAG", "UnsubscribeTopic "+s);
        }
        gb = new GlobalClass();
        sp = getSharedPreferences(USER_DETAIL, Context.MODE_PRIVATE);
        editor.putBoolean("autoLogin", false).commit();
        Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
        finish();
        startActivity(intent);
    }

}
