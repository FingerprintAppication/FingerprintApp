package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.SectionModel;
import com.example.i2ichest_.fingerprintit.model.SubjectModel;

public class PeriodActivity extends AppCompatActivity {
    WSManager wsManager;
    final String[] subjectDetail = new String[7];
    private GlobalClass gb;

    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);
        gb = (GlobalClass) this.getApplicationContext();
        toolBar = (Toolbar)findViewById(R.id.profile);
        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        showPeriod();
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

    //sq lv 2
    public void showPeriod(){
        Intent intent = getIntent();
        final long SUBJECTID = intent.getLongExtra("subjectID",1L);
        final String SUBJECTNUMBER = intent.getStringExtra("subjectNumber");
        final String SUBJECTNAME = intent.getStringExtra("subjectName");
        final String PERSONID = intent.getStringExtra("personID");
        subjectDetail[0] = SUBJECTNUMBER;
        subjectDetail[1] = SUBJECTNAME;
        TextView textViewSubjectName = (TextView) findViewById(R.id.textViewSubjectName);
        textViewSubjectName.setText(SUBJECTNAME);
        final TextView TEXTVIEW_SECTION_TITLE = (TextView) findViewById(R.id.textViewSectionTitle);
        final ProgressDialog PROGRESS = ProgressDialog.show(PeriodActivity.this,"Please Wait...","Please wait...",true);
        //sq lv 2
        SubjectModel subjectModel = new SubjectModel();
        subjectModel.getSubject().setSubjectID(SUBJECTID);
        //sq lv 2
        wsManager = WSManager.getWsManager(this);
        wsManager.doSearchSection(subjectModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(final Object response) {
                PROGRESS.dismiss();
                //sq lv 2
                final SectionModel.Section SECTION = (SectionModel.Section) response;

                    /*section ID here*/
                    final String sectionID = SECTION.getSectionID().toString();
                    final String sectionNumber = String.valueOf(SECTION.getSectionNumber());
                    final int semester = SECTION.getSemester();
                    final int schoolYear = SECTION.getSchoolYear();

                    TEXTVIEW_SECTION_TITLE.setText("กลุ่มเรียน " + sectionNumber + " : ภาคเรียนที่ " + semester + " : ปีการศึกษา " + schoolYear);
                    subjectDetail[5] = semester+"/"+schoolYear;

                    subjectDetail[6] = "";
                    for (int k = 0; k < SECTION.getTeacherList().size(); k++) {
                        subjectDetail[6] += SECTION.getTeacherList().get(k).getTitle()
                                + " " + SECTION.getTeacherList().get(k).getFirstName()
                                + " " + SECTION.getTeacherList().get(k).getLastName() + "\n";

                    }

                    GridLayout gridLayout = (GridLayout) findViewById(R.id.period_gridlayout);
                    for ( int g = 0 ; g < SECTION.getPeriodList().size() ; g++){
                        View view = getLayoutInflater().inflate(R.layout.period_layout,null);

                        TextView txtDay = (TextView) view.findViewById(R.id.textViewPeriodDay);
                        TextView txtTime = (TextView) view.findViewById(R.id.textViewPeriodTime);
                        TextView txtType = (TextView) view.findViewById(R.id.textViewPeriodType);
                        TextView txtRoom = (TextView)  view.findViewById(R.id.textViewPeriodRoom);
                        TextView txtBuild = (TextView) view.findViewById(R.id.textViewPeriodBuild);
                        Button viewAttendance = (Button) view.findViewById(R.id.buttonViewAttendance);

                        txtDay.setText("วันที่เรียน : " + SECTION.getPeriodList().get(g).getDayOfWeek());
                        txtTime.setText("เวลา : " +  SECTION.getPeriodList().get(g).getPeriodStartTime() + " - " + SECTION.getPeriodList().get(g).getPeriodEndTime());
                        txtType.setText("ประเภท : " + SECTION.getPeriodList().get(g).getStudyType());
                        txtRoom.setText("ห้อง : " +  SECTION.getPeriodList().get(g).getRoom().getRoomName());
                        txtBuild.setText("ตึก : " + SECTION.getPeriodList().get(g).getRoom().getBuilding().getBuildingName());

                        final Long periodForAttendance = SECTION.getPeriodList().get(g).getPeriodID();
                        final String time = SECTION.getPeriodList().get(g).getPeriodStartTime() + " - " + SECTION.getPeriodList().get(g).getPeriodEndTime();
                        final String type = SECTION.getPeriodList().get(g).getStudyType();
                        final String room = SECTION.getPeriodList().get(g).getRoom().getRoomName();

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
                                    intent.putExtra("periodID",SECTION.getPeriodList().get(finalG).getPeriodID());
                                    intent.putExtra("subjectID",SUBJECTID);
                                    intent.putExtra("subjectNumber",SUBJECTNUMBER);
                                    intent.putExtra("subjectName",SUBJECTNAME);
                                    intent.putExtra("subjectType",SECTION.getPeriodList().get(finalG).getStudyType());
                                    intent.putExtra("subjectDay",SECTION.getPeriodList().get(finalG).getDayOfWeek());
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
                                    intent.putExtra("subjectNumber",SUBJECTNUMBER);
                                    intent.putExtra("subjectName",SUBJECTNAME);
                                    startActivity(intent);
                                }
                            });
                        } else if(gb.getTypeUser().equals("student")) {
                            Button btnCal = (Button) findViewById(R.id.buttonCalculateScore);
                            btnCal.setVisibility(View.GONE);
                            Button btn = (Button) view.findViewById(R.id.buttonInformLeave);
                            btn.setText("ลาเรียน");
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intents = new Intent(PeriodActivity.this, InformLeaveActivity.class);
                                    intents.putExtra("subject", SUBJECTNUMBER);
                                    intents.putExtra("periodId", periodForAttendance.toString());
                                    intents.putExtra("studentID",gb.getLoginModel().getLogin().getUsername());
                                    intents.putExtra("studentName",gb.getLoginModel().getLogin().getPerson().getTitle()+
                                            gb.getLoginModel().getLogin().getPerson().getFirstName()+" "+
                                            gb.getLoginModel().getLogin().getPerson().getLastName());
                                    intents.putExtra("subjectID", SUBJECTID);
                                    Log.d("TAG", "##############################################2: "+SUBJECTID);
                                    intents.putExtra("subjectName", SUBJECTNAME);
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
                                        intent.putExtra("forAttendance",sectionID+"-"+periodForAttendance+"-"+SUBJECTNUMBER);
                                        intent.putExtra("allSubjectData",response.toString());
                                        intent.putExtra("personID", PERSONID);
                                        Bundle b = new Bundle();
                                        b.putStringArray("sub", subjectDetail);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                    }

                                }

                                @Override
                                public void onError(String error) {
                                    PROGRESS.dismiss();
                                }
                            });
                            }
                        });

                        gridLayout.addView(view);
                    }

            }

            @Override
            public void onError(String error) {
                PROGRESS.dismiss();
            }
        });


    }


}
