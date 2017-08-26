package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewLeaveHistoryActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_PERMISSION = 687;
    int GALLERY_REQUEST = 4567;
    GalleryPhoto galleryPhoto;
    InformLeaveModel.InformLeave inform;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leave_history);
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        showDetail();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDetail(){
        Intent intent = getIntent();
        inform = (InformLeaveModel.InformLeave) intent.getSerializableExtra("inform");

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

        ImageView image = (ImageView) findViewById(R.id.imageDoc);

        if (inform.getSupportDocument()!= null) {
            Log.d("TAG", "showDetail: "+inform.getSupportDocument());
            final Bitmap bb = decodeToImage(inform.getSupportDocument());
            image.setImageBitmap(bb);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ViewLeaveHistoryActivity.this).create();
                    View alertView = LayoutInflater.from(ViewLeaveHistoryActivity.this).inflate(R.layout.image_click, null);
                    ImageView img = (ImageView) alertView.findViewById(R.id.imageView);
                    img.setImageBitmap(bb);
                    alertDialog.setView(alertView);
                    alertDialog.show();
                }
            });
        }

        final Button btn = (Button) findViewById(R.id.btnAddDoc);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
                startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);
            }
        });

        /*Here WSManager to send class*/
        /*Here WSManager to send class*/

    }

    @Override
    public void onActivityResult (int request,int resultC,Intent data){
        if(request == GALLERY_REQUEST){
            if(resultC == RESULT_OK) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                inform.setSupportDocument(encodeBase64(photoPath));
                ImageView image = (ImageView) findViewById(R.id.imageDoc);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
                image.setImageBitmap(bitmap);
            }
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

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            //openFilePicker();
        }
    }

    public String encodeBase64 (String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOut);
        byte[] byteArray = byteArrayOut.toByteArray();

        return Base64.encodeToString(byteArray,Base64.DEFAULT);
    }


}
