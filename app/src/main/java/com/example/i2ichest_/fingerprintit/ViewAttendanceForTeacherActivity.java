package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance_for_teacher);
        wsManager = WSManager.getWsManager(this);
        toolBar = (Toolbar)findViewById(R.id.profile);
        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        showAllStudentAttendance();
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

    public void showAllStudentAttendance () {
        Intent intent = getIntent();
        final String subjectName = intent.getExtras().getString("subjectName");
        receiveSectionPeriod = intent.getExtras().getString("sectionPeriod");
        final ProgressDialog progress = ProgressDialog.show(this,"Please Wait...","Please wait...",true);
        wsManager.studentAttendaceForTeacher(receiveSectionPeriod, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {

                /****map json data from Ws***/
                listEnrollment = new ArrayList<EnrollmentModel.Enrollment>();
                Log.d("all Attendance ",response.toString());
                try {
                    JSONObject jsonEnrollment = new JSONObject(response.toString());
                    JSONArray jsonEnroll =   jsonEnrollment.getJSONArray("allEnrollment");
                    for(int u = 0;u<jsonEnroll.length();u++){
                       EnrollmentModel enm = new EnrollmentModel(jsonEnroll.get(u).toString());
                        listEnrollment.add(enm.getEnrollment());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /***sort enrollment by studentID***/
                sortEnrollmentByStudentId(listEnrollment);
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.listAttendance);
                TextView subject = (TextView)findViewById(R.id.subject);
                TextView totalStudent = (TextView)findViewById(R.id.totalStudent);
                subject.setText(subjectName);
                totalStudent.setText(listEnrollment.size()+" คน");
                for(EnrollmentModel.Enrollment en :listEnrollment){
                        List<Integer> status =  Attendance(en.getAttendanceList());
                        View view = (View) getLayoutInflater().inflate(R.layout.show_attendance_teacher, null);
                        TableLayout table = (TableLayout) view.findViewById(R.id.attendaceTable);
                        TableRow row = (TableRow) table.getChildAt(1);
                        TextView come = (TextView) row.findViewById(R.id.came);
                        TextView late = (TextView) row.findViewById(R.id.late);
                        TextView absence = (TextView) row.findViewById(R.id.absence);
                        TextView inform = (TextView) row.findViewById(R.id.inform);
                        TextView total = (TextView) row.findViewById(R.id.score);
                        TextView student = (TextView)view.findViewById(R.id.studentNameTxt);
                        /***set data into TableLayout***/
                        come.setText(status.get(0).toString());
                        late.setText(status.get(1).toString());
                        absence.setText(status.get(2).toString());
                        inform.setText(status.get(3).toString());
                        total.setText(status.get(4).toString());
                        student.setText(en.getStudent().getStudentID()+" "+en.getStudent().getTitle()+en.getStudent().getFirstName()+" "+en.getStudent().getLastName());
                        linearLayout.addView(view);
                }
                progress.dismiss();
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    public List<Integer> Attendance (List<AttendanceModel.Attendance> listAttendance) {
        int come= 0;
        int late =0;
        int absence = 0;
        int inform = 0;
        int weeks = listAttendance.size();
        List<Integer> list = new ArrayList<Integer>();
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
        list.add(come);
        list.add(late);
        list.add(absence);
        list.add(inform);
        list.add(weeks);
        return list;

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
