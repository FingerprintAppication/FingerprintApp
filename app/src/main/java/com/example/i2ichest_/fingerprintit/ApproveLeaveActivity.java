package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import Decoder.BASE64Decoder;

public class ApproveLeaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_leave);
        ShowInformLeave();
    }

    public void ShowInformLeave (){
        Intent intent = getIntent();
        InformLeaveModel.InformLeave inform = (InformLeaveModel.InformLeave)intent.getExtras().getSerializable("informleave");
        TextView studeId = (TextView)findViewById(R.id.studentIdTxt);
        TextView studeName = (TextView)findViewById(R.id.studentNameTxt);
        studeId.setText(inform.getStudent().getStudentID().toString());
        studeName.setText(inform.getStudent().getTitle()+inform.getStudent().getFirstName()+" "+inform.getStudent().getLastName());
        TableLayout table = (TableLayout)findViewById(R.id.tableApprove);
        TableRow mRow = (TableRow) table.getChildAt(0);
        TextView date = (TextView)mRow.findViewById(R.id.dateApprove);
        date.setText(inform.getSchedule().getScheduleDate());
        TableRow mRow2 = (TableRow) table.getChildAt(1);
        TextView type = (TextView)mRow2.findViewById(R.id.typeApprove);
        type.setText(inform.getInformType());
        TableRow mRow3 = (TableRow) table.getChildAt(2);
        TextView cases = (TextView)mRow3.findViewById(R.id.caseApprove);
        cases.setText(inform.getCaseDetail());

        if("ลาป่วย".equals(inform.getInformType())){
             if(!"".equals(inform.getSupportDocument())){
                 TableRow mRow4 = (TableRow) table.getChildAt(3);
                 ImageView image = (ImageView)mRow4.findViewById(R.id.imageView);
                 final Bitmap bb = decodeToImage(inform.getSupportDocument());
                 image.setImageBitmap(bb);
                 image.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         AlertDialog alertDialog = new AlertDialog.Builder(ApproveLeaveActivity.this).create();
                         View alertView = LayoutInflater.from(ApproveLeaveActivity.this).inflate(R.layout.image_click, null);
                         ImageView img = (ImageView) alertView.findViewById(R.id.imageView);
                         img.setImageBitmap(bb);
                         alertDialog.setView(alertView);
                         alertDialog.show();
                     }
                 });
             }
        }else {
            TableRow mRow4 = (TableRow) table.getChildAt(3);
            mRow4.setVisibility(View.INVISIBLE);
        }


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
