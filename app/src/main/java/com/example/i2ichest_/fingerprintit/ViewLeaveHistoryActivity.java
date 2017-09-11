package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.Base64Model;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ViewLeaveHistoryActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_PERMISSION = 687;
    int GALLERY_REQUEST = 4567;
    GalleryPhoto galleryPhoto;
    InformLeaveModel.InformLeave inform;
    WSManager wsManager;
    AlertDialog alertDialog;
    View alertView;
    ImageView image;
    Base64Model base;
    Intent intent;
    private GlobalClass gb;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leave_history);
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        wsManager = WSManager.getWsManager(this);
        base = new Base64Model();
        gb = (GlobalClass) this.getApplicationContext();
        showDetail();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDetail(){
        intent = getIntent();
        inform = (InformLeaveModel.InformLeave) intent.getSerializableExtra("inform");
        inform.setSupportDocument(gb.getLargeImage());
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

        if(inform.getCaseDetail() == null){
            cause.setText("รอ");
        }

        image = (ImageView) findViewById(R.id.imageDoc);
        if (inform.getSupportDocument()!= null) {
            Log.d("TAG", "showDetail: "+inform.getSupportDocument());
            final Bitmap bb = base.decodeToImage(inform.getSupportDocument());
            image.setImageBitmap(bb);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog = new AlertDialog.Builder(ViewLeaveHistoryActivity.this).create();
                    alertView = LayoutInflater.from(ViewLeaveHistoryActivity.this).inflate(R.layout.image_click, null);
                    ImageView img = (ImageView) alertView.findViewById(R.id.imageView);
                    img.setImageBitmap(bb);
                    alertDialog.setView(alertView);
                    alertDialog.show();
                }
            });
        }

        Button btnImg = (Button) findViewById(R.id.btnAddDoc);
        ImageButton btnUpdate = (ImageButton) findViewById(R.id.chooseImage);

        if(inform.getStatus().equals("อนุมัติ")){
            btnImg.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
        } else {
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermission();
                    startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);
                }
            });

            btnImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String parseDate = "";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Log.d("DATE PARSE1", "PRINT DATE : "+inform.getSchedule().getScheduleDate());
                    try {
                        parseDate=  sdf.parse(inform.getSchedule().getScheduleDate()).getTime()+"";
                        Log.d("DATE PARSE2", "PRINT DATE : "+parseDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
<<<<<<< HEAD
=======

>>>>>>> 2fbdb5428c5d43d9aaa86ed1ca2c9e5528fa7cf4
                    inform.getSchedule().setScheduleDate(parseDate);
                /*Here WSManager to send class*/
                    final ProgressDialog progress = ProgressDialog.show(ViewLeaveHistoryActivity.this,"Please Wait...","Please wait...",true);
                    wsManager.updateImageLeaveHistory(inform, new WSManager.WSManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            Log.d("UPDATEIMAGE!", "onComplete: "+response.toString());
                            Intent intentHistory = new Intent(ViewLeaveHistoryActivity.this,ViewListLeaveHistoryActivity.class);
                            intentHistory.putExtra("personId",intent.getLongExtra("personId",1L));
                            intentHistory.putExtra("result",response.toString());
                            progress.dismiss();
                            finish();
                            startActivity(intentHistory);
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });

                }
            });
        }
<<<<<<< HEAD

=======
>>>>>>> 2fbdb5428c5d43d9aaa86ed1ca2c9e5528fa7cf4
    }

    @Override
    public void onActivityResult (int request,int resultC,Intent data){
        if(request == GALLERY_REQUEST){
            if(resultC == RESULT_OK) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                inform.setSupportDocument(base.encodeBase64(photoPath));
                ImageView image = (ImageView) findViewById(R.id.imageDoc);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
                image.setImageBitmap(bitmap);
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            //openFilePicker();
        }
    }
}
