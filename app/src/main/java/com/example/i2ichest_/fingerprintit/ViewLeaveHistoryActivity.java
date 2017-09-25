package com.example.i2ichest_.fingerprintit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.Base64Model;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import java.text.SimpleDateFormat;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
    Toolbar toolBar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leave_history);
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        wsManager = WSManager.getWsManager(this);
        base = new Base64Model();
        gb = (GlobalClass) this.getApplicationContext();
        toolBar = (Toolbar)findViewById(R.id.profile);
        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        showDetail();
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        date.setText(sdf.format(inform.getSchedule().getScheduleDate()));
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

        final Button btnAdd = (Button) findViewById(R.id.btnUpdate);
        Button btnAddImg = (Button) findViewById(R.id.btnAddDoc);

        if(inform.getStatus().equals("อนุมัติ") || inform.getInformType().equals("ลากิจ") ){
            btnAdd.setVisibility(View.GONE);
            btnAddImg.setVisibility(View.GONE);

            if (inform.getInformType().equals("ลากิจ")) {
                TextView txtDoc = (TextView) findViewById(R.id.txtHistoryDoc);
                txtDoc.setText("");
                image.setVisibility(View.GONE);
            }
        } else {
            btnAddImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermission();

                    try {
                        startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
    }

    @Override
    public void onActivityResult (int request,int resultC,Intent data){
        try{
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
        }catch(Exception s){
            s.getMessage();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            //openFilePicker();
        }
    }
}
