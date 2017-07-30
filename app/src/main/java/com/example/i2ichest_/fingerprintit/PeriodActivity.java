package com.example.i2ichest_.fingerprintit;

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
        subjectDetail[0] = subjectNumber;
        subjectDetail[1] = subjectName;

        TextView textViewSubjectName = (TextView) findViewById(R.id.textViewSubjectName);
        final TextView textViewSectionTitle = (TextView) findViewById(R.id.textViewSectionTitle);

        textViewSubjectName.setText(subjectNumber + " : " + subjectName);

        final ProgressDialog progress = ProgressDialog.show(PeriodActivity.this,"Please Wait...","Please wait...",true);
        wsManager = WSManager.getWsManager(this);

        SubjectModel subjectModel = new SubjectModel();
        subjectModel.getSubject().setSubjectID(subjectID);
        wsManager.doSearchPeriod(subjectModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(final Object response) {
                progress.dismiss();

                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    Log.d("SECTION @@@@ :",jsonArray.toString());

                    JSONObject jsonSection = new JSONObject(jsonArray.get(0).toString());
                    /*section ID here*/
                    final String sectionID = jsonSection.getString("sectionID");
                    final String sectionNumber = jsonSection.getString("sectionNumber");
                    int semester = jsonSection.getInt("semester");
                    int schoolYear = jsonSection.getInt("schoolYear");
                    textViewSectionTitle.setText("กลุ่มเรียน " + sectionNumber + " : ภาคเรียนที่ " + semester + " : ปีการศึกษา " + schoolYear);
                    subjectDetail[5] = semester+"/"+schoolYear;
                    final List<PeriodModel.Period> listPeriod = new ArrayList<PeriodModel.Period>();
                    JSONArray jsonPeriod = new JSONArray(jsonSection.getJSONArray("periodList").toString());
                    Log.d("jsonPeriod @@@@ :",jsonPeriod.toString());
                    for ( int k = 0; k < jsonPeriod.length() ; k++ ) {
                        JSONObject jsonObject = new JSONObject(jsonPeriod.get(k).toString());

                        long periodID = jsonObject.getLong("periodID");
                        String dayOfWeek = jsonObject.getString("dayOfWeek");
                        String studyType = jsonObject.getString("studyType");
                        String periodStart = jsonObject.getString("periodStartTime");
                        String periodEnd = jsonObject.getString("periodEndTime");
                        PeriodModel periodModel = new PeriodModel();
                        periodModel.getPeriod().setPeriodID(periodID);
                        periodModel.getPeriod().setDayOfWeek(dayOfWeek);
                        periodModel.getPeriod().setStudyType(studyType);
                        periodModel.getPeriod().setPeriodStartTime(periodStart);
                        periodModel.getPeriod().setPeriodEndTime(periodEnd);

                        JSONObject jsonRoom = jsonObject.getJSONObject("room");
                        long roomId = jsonRoom.getLong("roomID");
                        String roomName = jsonRoom.getString("roomName");
                        RoomModel roomModel = new RoomModel();
                        roomModel.getRoom().setRoomID(roomId);
                        roomModel.getRoom().setRoomName(roomName);

                        JSONObject jsonBuilding = jsonRoom.getJSONObject("building");
                        long buildingID = jsonBuilding.getLong("buildingID");
                        String buildingName = jsonBuilding.getString("buildingName");
                        BuildingModel buildingModel = new BuildingModel();
                        buildingModel.getBuilding().setBuildingID(buildingID);
                        buildingModel.getBuilding().setBuildingName(buildingName);

                        roomModel.getRoom().setBuilding(buildingModel.getBuilding());
                        periodModel.getPeriod().setRoom(roomModel.getRoom());

                        listPeriod.add(periodModel.getPeriod());
                    }
                    /*get teahcer to attendance*/
                    JSONArray jsonteacher = new JSONArray(jsonSection.getJSONArray("teacherList").toString());
                    subjectDetail[6] = "";
                    for ( int k = 0; k < jsonteacher.length() ; k++ ) {
                        JSONObject jsonObject = new JSONObject(jsonteacher.get(k).toString());
                        subjectDetail[6] += jsonObject.getString("title")+" "+jsonObject.getString("firstName")+" "+jsonObject.getString("lastName")+"\n";

                    }

                    Log.d("LIST PERIOD :: " , listPeriod.toString());

                    GridLayout gridLayout = (GridLayout) findViewById(R.id.period_gridlayout);
                    for ( int g = 0 ; g < listPeriod.size() ; g++){
                        View view = getLayoutInflater().inflate(R.layout.period_layout,null);

                        TextView txtDay = (TextView) view.findViewById(R.id.textViewPeriodDay);
                        TextView txtTime = (TextView) view.findViewById(R.id.textViewPeriodTime);
                        TextView txtType = (TextView) view.findViewById(R.id.textViewPeriodType);
                        TextView txtRoom = (TextView)  view.findViewById(R.id.textViewPeriodRoom);
                        TextView txtBuild = (TextView) view.findViewById(R.id.textViewPeriodBuild);
                        Button viewAttendance = (Button) view.findViewById(R.id.buttonViewAttendance);
                        txtDay.setText("วันที่เรียน : " +listPeriod.get(g).getDayOfWeek());
                        txtTime.setText("เวลา : " + listPeriod.get(g).getPeriodStartTime() + " - " + listPeriod.get(g).getPeriodEndTime());
                        txtType.setText("ประเภท : " + listPeriod.get(g).getStudyType());
                        txtRoom.setText("ห้อง : " + listPeriod.get(g).getRoom().getRoomName());
                        txtBuild.setText("ตึก : " +listPeriod.get(g).getRoom().getBuilding().getBuildingName());



                        final Long periodForAttendance = listPeriod.get(g).getPeriodID();
                        final String time = listPeriod.get(g).getPeriodStartTime() + " - " + listPeriod.get(g).getPeriodEndTime();
                        final String type = listPeriod.get(g).getStudyType();
                        final String room = listPeriod.get(g).getRoom().getRoomName();
                        /****When we click inform leave button*****/
                        if(gb.getTypeUser().equals("teacher")){
                            Button btn = (Button) view.findViewById(R.id.buttonInformLeave);
                            btn.setText("ประกาศข่าว");

                            final int finalG = g;
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(PeriodActivity.this, AnnouceNewsActivity.class);
                                    intent.putExtra("periodID",listPeriod.get(finalG).getPeriodID());
                                    intent.putExtra("subjectNumber",subjectNumber);
                                    intent.putExtra("subjectName",subjectName);
                                    intent.putExtra("subjectType",listPeriod.get(finalG).getStudyType());
                                    intent.putExtra("subjectDay",listPeriod.get(finalG).getDayOfWeek());
                                    startActivity(intent);
                                }
                            });
                        }else if(gb.getTypeUser().equals("student")) {
                            Button btn = (Button) view.findViewById(R.id.buttonInformLeave);
                            btn.setText("ลาเรียน");
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(PeriodActivity.this, InformLeaveActivity.class);
                                    intent.putExtra("subject", subjectNumber);
                                    intent.putExtra("periodId", periodForAttendance.toString());
                                    intent.putExtra("studentID",gb.getLoginModel().getLogin().getUsername());
                                    intent.putExtra("studentName",gb.getLoginModel().getLogin().getPerson().getTitle()+
                                            gb.getLoginModel().getLogin().getPerson().getFirstName()+" "+
                                            gb.getLoginModel().getLogin().getPerson().getLastName());
                                    startActivity(intent);
                                }
                            });
                        }else {
                            Button btn = (Button) view.findViewById(R.id.buttonInformLeave);
                            btn.setVisibility(View.INVISIBLE);
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                progress.dismiss();
            }
        });


    }


}
