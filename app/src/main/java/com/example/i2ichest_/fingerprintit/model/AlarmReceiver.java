package com.example.i2ichest_.fingerprintit.model;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.i2ichest_.fingerprintit.DialogActivity;
import com.example.i2ichest_.fingerprintit.R;

import java.io.Serializable;
import java.sql.Time;


public class AlarmReceiver extends BroadcastReceiver implements Serializable {
    int startFrom = 6000;
    int endAt = 6000;
    MediaPlayer mp;



    @Override
    public void onReceive(Context context, Intent intent) {
        AlertDialog.Builder stopBox = new AlertDialog.Builder(context);




        //Handler handler = new Handler();
        //handler.postDelayed(stopPlayerTask, endAt);

        //Toast.makeText(context, "ALARM Period "+intent.getExtras().get("period"), Toast.LENGTH_LONG).show();
        Log.d("alarm alert!","Ok");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("เตือนวิชาเรียนวันนี้ "+intent.getStringExtra("period"));

        mBuilder.setContentText(intent.getStringExtra("study"));
        mBuilder.setSmallIcon(R.drawable.clock);

        Intent notificationIntent = new Intent(context, AlarmReceiver.class);
        notificationIntent.putExtra("stop","success");
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        //notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

        Intent intentStop = new Intent(context, DialogActivity.class);
        intentStop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentStop);



    }

    Runnable stopPlayerTask = new Runnable(){
        @Override
        public void run() {
            mp.stop();
        }};

}
