package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewLeaveHistoryActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leave_history);
        showDetail();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDetail(){
        Intent intent = getIntent();
        final InformLeaveModel.InformLeave inform = (InformLeaveModel.InformLeave) intent.getSerializableExtra("inform");

        TextView subName = (TextView) findViewById(R.id.HisSubName);
        subName.setText(inform.getSchedule().getPeriod().getSection().getSubject().getSubjectName().toString());

        TextView date = (TextView) findViewById(R.id.HistoryDate);
        TextView type = (TextView) findViewById(R.id.HistoryType);
        TextView status = (TextView) findViewById(R.id.HistoryStatus);
        TextView detail = (TextView) findViewById(R.id.HistoryDetail);
        TextView cause = (TextView) findViewById(R.id.HistoryCause);

        date.setText(inform.getSchedule().getScheduleDate());
        type.setText(inform.getInformType());
        status.setText(inform.getStatus());
        detail.setText(inform.getDetail());
        cause.setText(inform.getCaseDetail());

        if(inform.getCaseDetail().equals("")){
            cause.setText("รอ");
        }

        ImageView image = (ImageView) findViewById(R.id.imageDoc);

        if (inform.getSupportDocument()!= null) {
            Bitmap bb = decodeToImage(inform.getSupportDocument());
            image.setImageBitmap(bb);
        }

        final Button btn = (Button) findViewById(R.id.btnAddDoc);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(ViewLeaveHistoryActivity.this).create();
                View alertView = LayoutInflater.from(ViewLeaveHistoryActivity.this).inflate(R.layout.image_click, null);
                ImageView img = (ImageView) alertView.findViewById(R.id.imageView);
                Bitmap bb = decodeToImage(inform.getSupportDocument());
                img.setImageBitmap(bb);
                alertDialog.setView(alertView);
                alertDialog.show();
            }
        });

    }

    public Bitmap decodeToImage(String imageString) {
        byte[] imageByte;
        Bitmap bitmap = null;
        try {
            imageByte = Base64.decode(imageString, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
