package com.example.i2ichest_.fingerprintit;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.i2ichest_.fingerprintit.model.AnnouceNewsModel;

import java.text.SimpleDateFormat;

public class ViewAnnounceNews extends AppCompatActivity {
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announce_news);
        toolBar = (Toolbar)findViewById(R.id.profile);
        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        showAnnounceNewsDetail ();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                finish();
                startActivity(new Intent(this,ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAnnounceNewsDetail (){
        Intent intent = getIntent();
        AnnouceNewsModel.AnnouceNews announce = (AnnouceNewsModel.AnnouceNews)intent.getSerializableExtra("announce");
        TextView subject = (TextView)findViewById(R.id.announceSubject);
        TextView date = (TextView)findViewById(R.id.announceDate);
        TextView type = (TextView)findViewById(R.id.announceType);
        TextView teacher = (TextView)findViewById(R.id.announceTeacher);
        TextView detail = (TextView)findViewById(R.id.announceDetail);
        subject.setText(announce.getSchedule().getPeriod().getSection().getSubject().getSubjectName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        date.setText(sdf.format(announce.getSchedule().getScheduleDate()));
        type.setText(announce.getAnnouceNewsType());
        teacher.setText(announce.getTeacher().toString());
        detail.setText(announce.getDetail());
    }
}
