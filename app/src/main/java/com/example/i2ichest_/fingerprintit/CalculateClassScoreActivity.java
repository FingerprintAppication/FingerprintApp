package com.example.i2ichest_.fingerprintit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CalculateClassScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_class_score);
        showCalculateScore();
    }

    public void showCalculateScore(){
        Intent intent = getIntent();
        String secID = intent.getStringExtra("sectionID");
        String secNo = intent.getStringExtra("sectionNumber");
        int semester = intent.getIntExtra("semester",1);
        int schoolYear = intent.getIntExtra("schoolYear",1);
        String subNo = intent.getStringExtra("subjectNumber");
        String subName = intent.getStringExtra("subjectName");

        TextView txt = (TextView) findViewById(R.id.textViewCalSubject);
        txt.setText(subNo + " : " + subName + "\nกลุ่มเรียน " + secNo + " ภาคเรียนที่ " + semester + " ปีการศึกษา " + schoolYear);

        ArrayList<String> listScore = new ArrayList<String>();
        int score = 0;
        for (int i = 0 ; i < 5 ; i++ ){
            score += 5;
            listScore.add("" + score);
        }
        Spinner spScore  = (Spinner) findViewById(R.id.spinnerScore);
        ArrayAdapter<String> adapterScore = new ArrayAdapter<String>(CalculateClassScoreActivity.this,android.R.layout.simple_spinner_dropdown_item,listScore);
        spScore.setAdapter(adapterScore);

        ArrayList<String> listLate = new ArrayList<String>();
        for (int i = 1 ; i <= 10 ; i++ ){
            listLate.add("" + i);
        }

        Spinner spLate = (Spinner) findViewById(R.id.spinnerLate);
        ArrayAdapter<String> adapterLate = new ArrayAdapter<String>(CalculateClassScoreActivity.this,android.R.layout.simple_spinner_dropdown_item,listLate);
        spLate.setAdapter(adapterLate);
    }
}
