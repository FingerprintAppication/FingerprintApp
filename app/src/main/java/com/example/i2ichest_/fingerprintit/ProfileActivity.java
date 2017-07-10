package com.example.i2ichest_.fingerprintit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.i2ichest_.fingerprintit.manager.WSManager;

public class ProfileActivity extends AppCompatActivity {
    private GlobalClass gb;
    WSManager wsManager;

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
        TextView stuID = (TextView) findViewById(R.id.textSID);
        TextView major = (TextView) findViewById(R.id.textMJ);
        TextView fingerID = (TextView) findViewById(R.id.textFD);

        name.setText(sumStr);
        stuID.setText(gb.getLoginModel().getLogin().getUsername());
        major.setText(gb.getLoginModel().getLogin().getPerson().getMajor().getMajorName());
        fingerID.setText(gb.getLoginModel().getLogin().getPerson().getFingerprintData());
    }

    public void onClickBtnViewListSubject(View view){
        Intent intent = new Intent(ProfileActivity.this,ViewListSubjectActivity.class);
        startActivity(intent);
    }
}
