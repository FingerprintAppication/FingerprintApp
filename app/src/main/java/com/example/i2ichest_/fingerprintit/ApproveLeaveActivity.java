package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import Decoder.BASE64Decoder;

public class ApproveLeaveActivity extends AppCompatActivity {
    WSManager wsManager;
    private GlobalClass gb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_leave);
        wsManager = WSManager.getWsManager(this);
        gb = (GlobalClass) this.getApplicationContext();
        ShowInformLeave();
    }

    public void ShowInformLeave (){
        Intent intent = getIntent();
        final InformLeaveModel.InformLeave inform = (InformLeaveModel.InformLeave)intent.getExtras().getSerializable("informleave");
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
        cases.setText(inform.getDetail());
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
        Button approve = (Button)findViewById(R.id.approve);
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ApproveLeaveActivity.this);
                View alertView = LayoutInflater.from(ApproveLeaveActivity.this).inflate(R.layout.approve_inform, null);
                final Spinner approveSpinner = (Spinner)alertView.findViewById(R.id.approveSpinner);
                final EditText caseDetail = (EditText)alertView.findViewById(R.id.caseDetail);

               approveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                       Log.d("", "onItemSelected: "+adapterView.getSelectedItem().toString());
                       if("ไม่อนุมัติ".equals(adapterView.getSelectedItem().toString())){
                           caseDetail.setEnabled(true);

                       }else{
                           caseDetail.setEnabled(false);
                       }
                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> adapterView) {

                   }
               });
                alertDialog.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(approveSpinner.getSelectedItem().toString().equals("อนุมัติ")){
                            inform.setStatus("อนุมัติ");
                        }else {
                            inform.setStatus("ไม่อนุมัติ");
                            inform.setCaseDetail(caseDetail.getText().toString());
                        }
                        final ProgressDialog progress = ProgressDialog.show(ApproveLeaveActivity.this,"Please Wait...","Please wait...",true);
                        wsManager.updateAttendanceStatus(inform, new WSManager.WSManagerListener() {
                            @Override
                            public void onComplete(Object response) {
                                Log.d("TAG", "onComplete: "+ response.toString());
                                if("success".equals(response)){
                                    Intent intent = new Intent(ApproveLeaveActivity.this,ViewListInformLeaveActivity.class);
                                    intent.putExtra("personId", gb.getLoginModel().getLogin().getPerson().getPersonID().toString());
                                    intent.putExtra("result", "ยืนยันการลาเรียนเสร็จสมบูรณ์");
                                    finish();
                                    startActivity(intent);
                                    progress.dismiss();
                                }else {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ApproveLeaveActivity.this).create();
                                    alertDialog.setTitle("สถานะการยืนยันการลาเรียน");
                                    alertDialog.setIcon(getResources().getDrawable(R.drawable.error));
                                    alertDialog.setMessage("ไม่สามารถยืนยันการลาเรียนได้");
                                    alertDialog.show();
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    }
                });
                alertDialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
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
