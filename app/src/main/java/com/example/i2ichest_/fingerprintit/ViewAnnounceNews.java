package com.example.i2ichest_.fingerprintit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.i2ichest_.fingerprintit.model.AnnouceNewsModel;

public class ViewAnnounceNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announce_news);
        showAnnounceNewsDetail ();
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
        date.setText(announce.getSchedule().getScheduleDate());
        type.setText(announce.getAnnouceNewsType());
        teacher.setText(announce.getTeacher().toString());
        detail.setText(announce.getDetail());
    }
}
