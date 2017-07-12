package com.example.i2ichest_.fingerprintit.model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.example.i2ichest_.fingerprintit.R;
import java.sql.Time;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "ALARM", Toast.LENGTH_LONG).show();
        Log.d("alarm alert!","Ok");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("Notifications Example");
        mBuilder.setContentText("This is a test notification");
        mBuilder.setSmallIcon(R.drawable.res);


        Intent notificationIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.

        mNotificationManager.notify(0, mBuilder.build());
    }
}
