package com.example.i2ichest_.fingerprintit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.AttendanceModel;
import com.example.i2ichest_.fingerprintit.model.EnrollmentModel;
import com.example.i2ichest_.fingerprintit.model.PeriodModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VIewAttendanceActivity extends AppCompatActivity {
    WSManager wsManager;
    private GlobalClass gb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        wsManager = WSManager.getWsManager(this);
        final Intent intent = getIntent();
        gb = (GlobalClass) this.getApplicationContext();

        TextView studentName = (TextView)findViewById(R.id.studentName);
        String title = null;
        String name = null;
        if(gb.getTypeUser().equals("parent")){
            title = gb.getParentStudent().getTitle();
            name = gb.getParentStudent().getFirstName() + " " + gb.getParentStudent().getLastName();
        } else {
            title = gb.getLoginModel().getLogin().getPerson().getTitle();
            name = gb.getLoginModel().getLogin().getPerson().getFirstName() +" " +gb.getLoginModel().getLogin().getPerson().getLastName();
        }
        studentName.setText(title+" "+name);
        String forAttendance =  intent.getExtras().getString("forAttendance");
        String forAttendances[] = forAttendance.split("-");
        PeriodModel period = new PeriodModel();
        period.getPeriod().setPeriodID(Long.parseLong(forAttendances[1]));
        Log.d("sectionID ",forAttendances[0]+" id");
        /*this line below this is setting studentID*/
        period.getPeriod().setStudyType(Long.toString(gb.getLoginModel().getLogin().getPerson().getPersonID()));
        /*this line below this is setting subjectNumber*/
        period.getPeriod().setComingTime(forAttendances[2]);
        wsManager.getEnrollment(period, new WSManager.WSManagerListener() {

            @Override
            public void onComplete(Object response) {
                List<AttendanceModel.Attendance> listAttendance = (List<AttendanceModel.Attendance>) response;
                Log.d("##SIZE ",listAttendance.size()+" $$$");
                int come= 0;
                int late =0;
                int absence = 0;
                int inform = 0;
                int total;
                for(AttendanceModel.Attendance att:listAttendance){
                    if("มา".equals(att.getStatus())){
                        come++;
                    }else if("สาย".equals(att.getStatus())) {
                        late++;
                    }else if("ขาด".equals(att.getStatus())) {
                        absence++;
                    }else if("ลา".equals(att.getStatus())) {
                        inform++;
                    } else {

                    }
                }

                TableLayout mTable = (TableLayout) findViewById(R.id.attendaceTable);
                TableRow mRow = (TableRow) mTable.getChildAt(1);
                TextView comeTxt = (TextView)mRow.getChildAt(0);
                TextView lateTxt = (TextView)mRow.getChildAt(1);
                TextView absenceTxt = (TextView)mRow.getChildAt(2);
                TextView informTxt = (TextView)mRow.getChildAt(3);
                /*set attendance time*/
                comeTxt.setText(come+"");
                lateTxt.setText(late+"");
                absenceTxt.setText(absence+"");
                informTxt.setText(inform+"");

                Bundle b = getIntent().getExtras();
                String[] array=b.getStringArray("sub");
                TableLayout subjectDetailTable = (TableLayout) findViewById(R.id.subjectDetailData);
                for(int i=0;i<subjectDetailTable.getChildCount();i++){
                    TableRow sdtRow = (TableRow) subjectDetailTable.getChildAt(i);
                    TextView subjectTxt = (TextView)sdtRow.getChildAt(1);
                    subjectTxt.setText(array[i]);
                }

            }

            @Override
            public void onError(String error) {

            }
        });
    }

}
