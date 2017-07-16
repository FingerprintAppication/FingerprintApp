package com.example.i2ichest_.fingerprintit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.i2ichest_.fingerprintit.manager.WSManager;

public class ProfileActivity extends AppCompatActivity {
    private GlobalClass gb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        gb = (GlobalClass) this.getApplicationContext();
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
        }

        if (typeUser.equals("student")){
            stuID.setText(gb.getLoginModel().getLogin().getUsername());
            fingerID.setText(gb.getLoginModel().getLogin().getPerson().getFingerprintData());

        } else if (typeUser.equals("teacher") ||typeUser.equals("parent")) {
            if (gb.getTypeUser().equals("parent")){
                major.setText("");
                TextView m = (TextView) findViewById(R.id.textMajor);
                m.setText("");
            }
            stuID.setText("");
            fingerID.setText("");

            TextView sid = (TextView) findViewById(R.id.textStudentID);
            TextView finger = (TextView) findViewById(R.id.textFingerID);
            sid.setText("");
            finger.setText("");
        }


    }

    public void onClickBtnViewListSubject(View view){
        Intent intent = new Intent(ProfileActivity.this,ViewListSubjectActivity.class);
        startActivity(intent);
    }
}
