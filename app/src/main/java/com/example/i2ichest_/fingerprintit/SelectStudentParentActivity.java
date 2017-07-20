package com.example.i2ichest_.fingerprintit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.i2ichest_.fingerprintit.model.StudentModel;

import java.util.ArrayList;
import java.util.List;

public class SelectStudentParentActivity extends AppCompatActivity {
    private GlobalClass gb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student_parent);
        gb = (GlobalClass) this.getApplicationContext();
        showStudentParent();
    }

    public void showStudentParent(){
        final List<String> listString = new ArrayList<>();

        for(StudentModel.Student i : gb.getListStudent()){
            long studentID = i.getStudentID();
            String studentName = i.getTitle() + " " + i.getFirstName() + " " + i.getLastName();
            String major = i.getMajor().getMajorName();

            listString.add(studentID + " " + studentName + " \nสาขา : " + major);
        }

        ListView listView = (ListView) findViewById(R.id.listView_studentParent);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectStudentParentActivity.this,android.R.layout.simple_selectable_list_item,listString);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SelectStudentParentActivity.this,ViewListSubjectActivity.class);
                intent.putExtra("personID" , gb.getListStudent().get(i).getPersonID());
                gb.setParentStudent(gb.getListStudent().get(i));
                startActivity(intent);
            }
        });
    }
}
