package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);
        showPeriod();
    }

    public void showPeriod(){
        Intent intent = getIntent();
        long subjectID = intent.getLongExtra("subjectID",1L);
        String subjectNumber = intent.getStringExtra("subjectNumber");
        String subjectName = intent.getStringExtra("subjectName");

        TextView textViewSubjectName = (TextView) findViewById(R.id.textViewSubjectName);
        final TextView textViewSectionTitle = (TextView) findViewById(R.id.textViewSectionTitle);

        textViewSubjectName.setText(subjectNumber + " : " + subjectName);

        final ProgressDialog progress = ProgressDialog.show(PeriodActivity.this,"Please Wait...","Please wait...",true);
        wsManager = WSManager.getWsManager(this);

        SubjectModel subjectModel = new SubjectModel();
        subjectModel.getSubject().setSubjectID(subjectID);
        wsManager.doSearchPeriod(subjectModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                progress.dismiss();
                Toast.makeText(PeriodActivity.this, "CONNECT SUCCESS", Toast.LENGTH_SHORT).show();

                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    Log.d("SECTION @@@@ :",jsonArray.toString());

                    JSONObject jsonSection = new JSONObject(jsonArray.get(0).toString());
                    String sectionNumber = jsonSection.getString("sectionNumber");
                    int semester = jsonSection.getInt("semester");
                    int schoolYear = jsonSection.getInt("schoolYear");
                    textViewSectionTitle.setText("กลุ่มเรียน " + sectionNumber + " : ภาคเรียนที่ " + semester + " : ปีการศึกษา " + schoolYear);

                    List<PeriodModel.Period> listPeriod = new ArrayList<PeriodModel.Period>();
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
                    Log.d("LIST PERIOD :: " , listPeriod.toString());

                    GridLayout gridLayout = (GridLayout) findViewById(R.id.period_gridlayout);
                    for ( int g = 0 ; g < listPeriod.size() ; g++){
                        View view = getLayoutInflater().inflate(R.layout.period_layout,null);

                        TextView txtDay = (TextView) view.findViewById(R.id.textViewPeriodDay);
                        TextView txtTime = (TextView) view.findViewById(R.id.textViewPeriodTime);
                        TextView txtType = (TextView) view.findViewById(R.id.textViewPeriodType);
                        TextView txtRoom = (TextView)  view.findViewById(R.id.textViewPeriodRoom);
                        TextView txtBuild = (TextView) view.findViewById(R.id.textViewPeriodBuild);

                        txtDay.setText("วันที่เรียน : " +listPeriod.get(g).getDayOfWeek());
                        txtTime.setText("เวลา : " + listPeriod.get(g).getPeriodStartTime() + " - " + listPeriod.get(g).getPeriodEndTime());
                        txtType.setText("ประเภท : " + listPeriod.get(g).getStudyType());
                        txtRoom.setText("ห้อง : " + listPeriod.get(g).getRoom().getRoomName());
                        txtBuild.setText("ตึก : " +listPeriod.get(g).getRoom().getBuilding().getBuildingName());

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
