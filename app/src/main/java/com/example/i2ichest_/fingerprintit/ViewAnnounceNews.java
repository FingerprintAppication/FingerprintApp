package com.example.i2ichest_.fingerprintit;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.i2ichest_.fingerprintit.DBHelper.AnnounceSqlite;
import com.example.i2ichest_.fingerprintit.DBHelper.DatabaseHelper;
import com.example.i2ichest_.fingerprintit.adapter.InformViewAdapter;
import com.example.i2ichest_.fingerprintit.adapter.ViewnounceAdapter;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.AnnouceNewsModel;
import com.example.i2ichest_.fingerprintit.model.InformLeaveModel;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewAnnounceNews extends AppCompatActivity {
    WSManager wsManager;
    private GlobalClass gb;
    AnnounceSqlite myDb;
    int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announce_news);
        wsManager = WSManager.getWsManager(this);
        showListAnnounceNews();

    }

    public void onStart (){
        super.onStart();
        ListView view = (ListView) findViewById(R.id.listAnnounce);
        if(view.getCount()!=0){
            View adat = (View) getViewByPosition(index,view);
            TextView tee = (TextView)adat.findViewById(R.id.informTxt);
            tee.setBackgroundColor(Color.WHITE);
        }
    }

    public void showListAnnounceNews (){
        myDb = new AnnounceSqlite(this);
        final ProgressDialog progress = ProgressDialog.show(this,"Please Wait...","Please wait...",true);
        wsManager.getAnnounceNewsFromStudentId("3433", new WSManager.WSManagerListener() {
            @Override
            public void onComplete(Object response) {
                myDb.deleteAnnounce("29");
                myDb.deleteAnnounce("30");
                ListView view = (ListView) findViewById(R.id.listAnnounce);
                final List<AnnouceNewsModel.AnnouceNews> listAnnounce =(List<AnnouceNewsModel.AnnouceNews>) response;
                List<String> setColor = new ArrayList<String>();
                Map<String,String> unRead = myDb.getAllAnnounces();
                if(!listAnnounce.isEmpty()){

                    for (AnnouceNewsModel.AnnouceNews news : listAnnounce) {
                        if(unRead.get(news.getAnnouceNewsID()+"") == null){
                            setColor.add("set");

                        }else {
                            setColor.add("off");
                        }


                    }
                    ViewnounceAdapter viewnounceAdapter = new ViewnounceAdapter(ViewAnnounceNews.this, listAnnounce, listAnnounce.size(), setColor);
                    view.setAdapter(viewnounceAdapter);
                    progress.dismiss();
                    view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            myDb.addAnnounceRead(listAnnounce.get(i));
                            index =i;
                        }
                    });
                }else{
                    String show [] =  {"ไม่พบรายการประกาศข่าวสาร"};
                    view.setAdapter(new ArrayAdapter<String>(ViewAnnounceNews.this, android.R.layout.simple_list_item_1, show));
                    progress.dismiss();
                }



            }

            @Override
            public void onError(String error) {
                Log.d("TAG ERROR ", "onError: "+ error);
            }


        });

    }

    /************get view in listview*********/
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

   /* public void showToken(View view) {
        // แสดง token มาให้ดูหน่อยเสะ
        TextView mTextView = (TextView)findViewById(R.id.txt);
        mTextView.setText(FirebaseInstanceId.getInstance().getToken());
        Log.i("token", FirebaseInstanceId.getInstance().getToken());
    }
    public void subscribe(View view) {
        // สับตะไคร้หัวข้อ news
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        TextView mTextView = (TextView)findViewById(R.id.txt);
        mTextView.setText("subscribed");
    }
    public void unsubscribe(View view) {
        // ยกเลิกสับตะไคร้หัวข้อ news
        FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
        TextView mTextView = (TextView)findViewById(R.id.txt);
        mTextView.setText("unsubscribed");
    }
    public void sendMessage(View view) {
        String proId = "12345";
        String fcmconnect = "https://fcm.googleapis.com/fcm/send";
        TextView mTextView = (TextView)findViewById(R.id.sender);
        // ยกเลิกสับตะไคร้หัวข้อ news
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(proId+fcmconnect).setMessageId("2")
                .addData("message",mTextView.getText().toString()).addData("action","Message").build());

        mTextView.setText("unsubscribed");
    }*/
}
