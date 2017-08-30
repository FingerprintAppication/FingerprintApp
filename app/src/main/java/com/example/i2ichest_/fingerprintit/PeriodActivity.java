package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.AnnouceNewsModel;
import com.example.i2ichest_.fingerprintit.model.BuildingModel;
import com.example.i2ichest_.fingerprintit.model.PeriodModel;
import com.example.i2ichest_.fingerprintit.model.RoomModel;
import com.example.i2ichest_.fingerprintit.model.SectionModel;
import com.example.i2ichest_.fingerprintit.model.SubjectModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PeriodActivity extends AppCompatActivity {
    WSManager wsManager;
    final String[] subjectDetail = new String[7];
    private GlobalClass gb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);
        gb = (GlobalClass) this.getApplicationContext();
        showPeriod();
    }

    public void showPeriod(){
        Intent intent = getIntent();
        final long subjectID = intent.getLongExtra("subjectID",1L);
        final String subjectNumber = intent.getStringExtra("subjectNumber");
        final String subjectName = intent.getStringExtra("subjectName");

        String resultInform = intent.getStringExtra("resultInform");
        Log.d("TAG", "##############################################: "+subjectID);
        if(resultInform!= null) {
            AlertDialog alertDialog = new AlertDialog.Builder(PeriodActivity.this).create();
            alertDialog.setTitle("ลาเรียน");
            Log.d("TAG", "ลาเข้าล้ะ: ");
            if ("ลาเรียนสำเร็จ".equals(resultInform)) {
                alertDialog.setIcon(getResources().getDrawable(R.drawable.success));
                alertDialog.setMessage(resultInform);
            } else {
                alertDialog.setIcon(getResources().getDrawable(R.drawable.duplicated));
                alertDialog.setMessage(resultInform);
            }
            alertDialog.show();
        }

        subjectDetail[0] = subjectNumber;
        subjectDetail[1] = subjectName;

        TextView textViewSubjectName = (TextView) findViewById(R.id.textViewSubjectName);
        textViewSubjectName.setText(subjectName);

        final TextView textViewSectionTitle = (TextView) findViewById(R.id.textViewSectionTitle);
        final ProgressDialog progress = ProgressDialog.show(PeriodActivity.this,"Please Wait...","Please wait...",true);
        wsManager = WSManager.getWsManager(this);

        SubjectModel subjectModel = new SubjectModel();
        subjectModel.getSubject().setSubjectID(subjectID);
        wsManager.doSearchSection(subjectModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(final Object response) {

                final SectionModel.Section section = (SectionModel.Section) response;

                    /*section ID here*/
                    final String sectionID = section.getSectionID().toString();
                    final String sectionNumber = String.valueOf(section.getSectionNumber());
                    final int semester = section.getSemester();
                    final int schoolYear = section.getSchoolYear();

                    textViewSectionTitle.setText("กลุ่มเรียน " + sectionNumber + " : ภาคเรียนที่ " + semester + " : ปีการศึกษา " + schoolYear);
                    subjectDetail[5] = semester+"/"+schoolYear;

                    subjectDetail[6] = "";
                    for (int k = 0; k < section.getTeacherList().size(); k++) {
                        subjectDetail[6] += section.getTeacherList().get(k).getTitle()
                                + " " + section.getTeacherList().get(k).getFirstName()
                                + " " + section.getTeacherList().get(k).getLastName() + "\n";

                    }

                    GridLayout gridLayout = (GridLayout) findViewById(R.id.period_gridlayout);
                    for ( int g = 0 ; g < section.getPeriodList().size() ; g++){
                        View view = getLayoutInflater().inflate(R.layout.period_layout,null);

                        TextView txtDay = (TextView) view.findViewById(R.id.textViewPeriodDay);
                        TextView txtTime = (TextView) view.findViewById(R.id.textViewPeriodTime);
                        TextView txtType = (TextView) view.findViewById(R.id.textViewPeriodType);
                        TextView txtRoom = (TextView)  view.findViewById(R.id.textViewPeriodRoom);
                        TextView txtBuild = (TextView) view.findViewById(R.id.textViewPeriodBuild);
                        Button viewAttendance = (Button) view.findViewById(R.id.buttonViewAttendance);

                        txtDay.setText("วันที่เรียน : " + section.getPeriodList().get(g).getDayOfWeek());
                        txtTime.setText("เวลา : " +  section.getPeriodList().get(g).getPeriodStartTime() + " - " + section.getPeriodList().get(g).getPeriodEndTime());
                        txtType.setText("ประเภท : " + section.getPeriodList().get(g).getStudyType());
                        txtRoom.setText("ห้อง : " +  section.getPeriodList().get(g).getRoom().getRoomName());
                        txtBuild.setText("ตึก : " + section.getPeriodList().get(g).getRoom().getBuilding().getBuildingName());

                        final Long periodForAttendance = section.getPeriodList().get(g).getPeriodID();
                        final String time = section.getPeriodList().get(g).getPeriodStartTime() + " - " + section.getPeriodList().get(g).getPeriodEndTime();
                        final String type = section.getPeriodList().get(g).getStudyType();
                        final String room = section.getPeriodList().get(g).getRoom().getRoomName();

                        /****When we click inform leave button*****/
                        if(gb.getTypeUser().equals("teacher")){
                            /*get teahcer to attendance*/
                            Button btn = (Button) view.findViewById(R.id.buttonInformLeave);
                            btn.setText("ประกาศข่าว");
                            final int finalG = g;
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(PeriodActivity.this, AnnouceNewsActivity.class);
                                    intent.putExtra("periodID",section.getPeriodList().get(finalG).getPeriodID());
                                    intent.putExtra("subjectNumber",subjectNumber);
                                    intent.putExtra("subjectName",subjectName);
                                    intent.putExtra("subjectType",section.getPeriodList().get(finalG).getStudyType());
                                    intent.putExtra("subjectDay",section.getPeriodList().get(finalG).getDayOfWeek());
                                    startActivity(intent);
                                }
                            });

                            Button btnCal = (Button) findViewById(R.id.buttonCalculateScore);
                            btnCal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(PeriodActivity.this, CalculateClassScoreActivity.class);
                                    intent.putExtra("sectionID",sectionID);
                                    intent.putExtra("sectionNumber",sectionNumber);
                                    intent.putExtra("semester",semester);
                                    intent.putExtra("schoolYear",schoolYear);
                                    intent.putExtra("subjectNumber",subjectNumber);
                                    intent.putExtra("subjectName",subjectName);
                                    startActivity(intent);
                                }
                            });
                        } else if(gb.getTypeUser().equals("student")) {
<<<<<<< HEAD
                            Button btnCal = (Button) findViewById(R.id.buttonCalculateScore);
                            btnCal.setVisibility(View.GONE);
=======
                            /*มันโหลดหน้า PERIOD บ่อได้ถ้าบ่ปิด*/
                            //Button btnCal = (Button) findViewById(R.id.buttonCalculateScore);
                            //btnCal.setVisibility(View.INVISIBLE);
>>>>>>> 0830ab9f723a8918141226e369560e8acea3a54d
                            Button btn = (Button) view.findViewById(R.id.buttonInformLeave);
                            btn.setText("ลาเรียน");
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intents = new Intent(PeriodActivity.this, InformLeaveActivity.class);
                                    intents.putExtra("subject", subjectNumber);
                                    intents.putExtra("periodId", periodForAttendance.toString());
                                    intents.putExtra("studentID",gb.getLoginModel().getLogin().getUsername());
                                    intents.putExtra("studentName",gb.getLoginModel().getLogin().getPerson().getTitle()+
                                            gb.getLoginModel().getLogin().getPerson().getFirstName()+" "+
                                            gb.getLoginModel().getLogin().getPerson().getLastName());
                                    intents.putExtra("subjectID", subjectID);
                                    Log.d("TAG", "##############################################2: "+subjectID);
                                    intents.putExtra("subjectName", subjectName);
                                    startActivity(intents);
                                }
                            });
                        } else if (gb.getTypeUser().equals("parent")) {
                            Button btn = (Button) view.findViewById(R.id.buttonInformLeave);
                            btn.setVisibility(View.GONE);
                            Button btnCal = (Button) findViewById(R.id.buttonCalculateScore);
                            btnCal.setVisibility(View.GONE);
                        }

                        /*receive data and send to ws*/
                        viewAttendance.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            wsManager.findTeacher(gb.getLoginModel().getLogin().getPerson().getPersonID(), new WSManager.WSManagerListener() {
                                @Override
                                public void onComplete(Object response) {
                                    if("teacher".equals(response.toString())){
                                        Intent intent = new Intent(PeriodActivity.this,ViewAttendanceForTeacherActivity.class);
                                        intent.putExtra("sectionPeriod",sectionID+"-"+periodForAttendance);
                                        intent.putExtra("subjectName", subjectDetail[0]+" "+ subjectDetail[1].toString());
                                        Log.d("Teacher login $$ ",response+"------------------");

                                        startActivity(intent);
                                    }else{
                                        subjectDetail[2] = time;
                                        subjectDetail[3] = type;
                                        subjectDetail[4] = room;
                                        Intent intent = new Intent(PeriodActivity.this,VIewAttendanceActivity.class);
                                        intent.putExtra("forAttendance",sectionID+"-"+periodForAttendance+"-"+subjectNumber);
                                        intent.putExtra("allSubjectData",response.toString());
                                        Bundle b = new Bundle();
                                        b.putStringArray("sub", subjectDetail);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                    }

                                }

                                @Override
                                public void onError(String error) {

                                }
                            });
                            }
                        });

                        gridLayout.addView(view);
                    }
                progress.dismiss();
            }

            @Override
            public void onError(String error) {
                progress.dismiss();
            }
        });


    }


}
