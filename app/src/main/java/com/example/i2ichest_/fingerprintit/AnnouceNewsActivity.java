package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.PeriodModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AnnouceNewsActivity extends AppCompatActivity {
    WSManager wsManager;
    private GlobalClass gb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_annouce_news);
        gb = (GlobalClass) this.getApplicationContext();
        //showInformLeave();
    }

   /* public void showInformLeave(){
        Intent intent = getIntent();
        long periodID = intent.getLongExtra("periodID",1L);

        String subjectName = intent.getStringExtra("subjectName");
        String subjectNumber = intent.getStringExtra("subjectNumber");
        String subjectType = intent.getStringExtra("subjectType");
        String subjectDay = intent.getStringExtra("subjectDay");

        TextView txtSubID = (TextView) findViewById(R.id.textViewSubID);
        TextView txtSubName = (TextView) findViewById(R.id.textViewSubName);
        TextView txtSubType = (TextView) findViewById(R.id.textViewSubType);
        TextView txtSubDay = (TextView) findViewById(R.id.textViewSubDay);

        txtSubID.setText(subjectNumber);
        txtSubName.setText(subjectName);
        txtSubType.setText(subjectType);
        txtSubDay.setText(subjectDay);

        Spinner spType = (Spinner) findViewById(R.id.spinnerNewsType);
        String[] type = {"ทั่วไป", "งด"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AnnouceNewsActivity.this,android.R.layout.simple_list_item_1,type);
        spType.setAdapter(adapter);

        final ProgressDialog progress = ProgressDialog.show(AnnouceNewsActivity.this,"Please Wait...","Please wait...",true);
        wsManager = WSManager.getWsManager(this);

        PeriodModel periodModel = new PeriodModel();
        periodModel.getPeriod().setPeriodID(periodID);
        wsManager.doSearchScheduleDate(periodModel, new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                progress.dismiss();
                Spinner spDate = (Spinner) findViewById(R.id.spinnerNewsDate);
                List<String> listDate = new ArrayList<String>();

                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        String[] sp = jsonArray.get(i).toString().split("-");
                        String year = sp[0];
                        String month = sp[1];
                        String day = sp[2];
                        listDate.add(day + "-" + month + "-" + year);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AnnouceNewsActivity.this,android.R.layout.simple_list_item_1,listDate);
                spDate.setAdapter(adapter2);
            }

            @Override
            public void onError(String error) {
                progress.dismiss();
            }
        });
    }*/

    public void onClickAddAnnouceNews(View view){
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }
}
