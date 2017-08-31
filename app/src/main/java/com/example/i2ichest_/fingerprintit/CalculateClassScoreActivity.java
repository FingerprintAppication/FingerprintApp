package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.AttendanceModel;
import com.example.i2ichest_.fingerprintit.model.EnrollmentModel;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CalculateClassScoreActivity extends AppCompatActivity {
    WSManager wsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_class_score);
        wsManager = WSManager.getWsManager(this);
        showCalculateScore();
    }

    public void showCalculateScore(){
        Intent intent = getIntent();
        final String secID = intent.getStringExtra("sectionID");
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

<<<<<<< HEAD
        Button spinnerScore = (Button)findViewById(R.id.buttonCalculateScore);

        spinnerScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progress = ProgressDialog.show(CalculateClassScoreActivity.this,"Please Wait...","Please wait...",true);
                wsManager.getEnrollmentForCalculateScore(secID, new WSManager.WSManagerListener() {
                    @Override
                    public void onComplete(Object response) {

                        List<EnrollmentModel.Enrollment> listEnrollment = ( List<EnrollmentModel.Enrollment>) response;
                        Log.d("TAG-SIZE", "onComplete: "+listEnrollment.size()+" size");
                        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.listCalculateScore);
                        linearLayout.removeAllViews();
                        Spinner spinnerScore = (Spinner)findViewById(R.id.spinnerScore);
                        Spinner spinnerLate = (Spinner)findViewById(R.id.spinnerLate);
                        Double score = Double.parseDouble(spinnerScore.getSelectedItem().toString());
                        Double lates = Double.parseDouble(spinnerLate.getSelectedItem().toString());
                        sortEnrollmentByStudentId(listEnrollment);
                        for(EnrollmentModel.Enrollment en :listEnrollment){
                            List<Double> status =  Attendance(en.getAttendanceList(),score,lates);
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
                            come.setText(status.get(0).intValue()+"");
                            late.setText(status.get(1).intValue()+"");
                            absence.setText(status.get(2).intValue()+"");
                            inform.setText(status.get(3).intValue()+"");
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
        });

    }

    public List<Double> Attendance (List<AttendanceModel.Attendance> listAttendance,Double score,Double timeLate) {
        Double come= 0.0;
        Double late = 0.0;
        Double absence = 0.0;
        Double inform = 0.0;
        Double cancel = 0.0;
        Double total = 0.0;
        List<Double> list = new ArrayList<Double>();
        DecimalFormat df2 = new DecimalFormat(".##");
        for(AttendanceModel.Attendance att:listAttendance) {
            if ("มา".equals(att.getStatus())) {
                come++;
            } else if ("สาย".equals(att.getStatus())) {
                late++;
            } else if ("ขาด".equals(att.getStatus())) {
                absence++;
            } else if ("ลา".equals(att.getStatus())) {
                inform++;
            } else if("ยกเลิก".equals(att.getStatus())){
                cancel++;
            }else{

            }
        }
        list.add(come);
        list.add(late);
        list.add(absence);
        list.add(inform);
        Double calLate = late/timeLate;
        Log.d("LATE TO ABSENCE ", "Attendance: status "+calLate);
        total = Double.valueOf(((come - (absence+calLate.intValue())) /listAttendance.size()) * score);
        list.add(Double.valueOf(df2.format(total)));
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
=======

>>>>>>> 9fef7efe4bb2fe95701df963232ec18bdec0b72e
    }
}
