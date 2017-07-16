package com.example.i2ichest_.fingerprintit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.AttendanceModel;
import com.example.i2ichest_.fingerprintit.model.EnrollmentModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewAttendanceForTeacherActivity extends AppCompatActivity {
    WSManager wsManager;
    String receiveSectionPeriod = "";
    List<EnrollmentModel.Enrollment> listEnrollment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance_for_teacher);
        wsManager = WSManager.getWsManager(this);

        showAllStudentAttendance();
    }

    public void showAllStudentAttendance () {
        Intent intent = getIntent();
        final String subjectName = intent.getExtras().getString("subjectName");
        receiveSectionPeriod = intent.getExtras().getString("sectionPeriod");
        wsManager.studentAttendaceForTeacher(receiveSectionPeriod, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                /****map json data from Ws***/
                listEnrollment = new ArrayList<EnrollmentModel.Enrollment>();
                Log.d("all Attendance ",response.toString());
                try {
                    JSONObject jsonEnrollment = new JSONObject(response.toString());
                    JSONArray sss =   jsonEnrollment.getJSONArray("allEnrollment");
                    for(int u = 0;u<sss.length();u++){
                       EnrollmentModel enm = new EnrollmentModel(sss.get(u).toString());
                        listEnrollment.add(enm.getEnrollment());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /***sort enrollment by studentID***/
                sortEnrollmentByStudentId(listEnrollment);

                ListView student = (ListView)findViewById(R.id.listStudent);
                List<String> addString = new ArrayList<String>();
                TextView subject = (TextView)findViewById(R.id.subject);
                TextView totalStudent = (TextView)findViewById(R.id.totalStudent);
                subject.setText(subjectName);
                totalStudent.setText(listEnrollment.size()+" คน");
                for(EnrollmentModel.Enrollment en :listEnrollment){
                    addString.add(en.getStudent().getStudentID()+" "+Attendance(en.getAttendanceList()));

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewAttendanceForTeacherActivity.this,
                        android.R.layout.simple_list_item_1, addString);
                student.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    public String Attendance (List<AttendanceModel.Attendance> listAttendance) {
        int come= 0;
        int late =0;
        int absence = 0;
        int inform = 0;
        int total;
        for(AttendanceModel.Attendance att:listAttendance) {
            if ("มา".equals(att.getStatus())) {
                come++;
            } else if ("สาย".equals(att.getStatus())) {
                late++;
            } else if ("ขาด".equals(att.getStatus())) {
                absence++;
            } else if ("ลา".equals(att.getStatus())) {
                inform++;
            } else {

            }
        }

        return " มา "+come+" สาย "+late+" ขาด "+absence+" ลา "+inform;

    }

    public void sortEnrollmentByStudentId (List<EnrollmentModel.Enrollment>listEnrollment) {
        Collections.sort(listEnrollment, new Comparator<EnrollmentModel.Enrollment>() {
            @Override
            public int compare(EnrollmentModel.Enrollment c1, EnrollmentModel.Enrollment c2) {
                if (c1.getStudent().getStudentID() > c2.getStudent().getStudentID()) return 1;
                if (c1.getStudent().getStudentID() < c2.getStudent().getStudentID()) return -1;
                return 0;
            }});
    }

}
