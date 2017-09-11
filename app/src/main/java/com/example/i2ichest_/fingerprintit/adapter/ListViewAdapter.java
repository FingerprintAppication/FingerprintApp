package com.example.i2ichest_.fingerprintit.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.i2ichest_.fingerprintit.InformLeaveActivity;
import com.example.i2ichest_.fingerprintit.R;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.AlarmReceiver;
import com.example.i2ichest_.fingerprintit.model.PeriodModel;
import com.example.i2ichest_.fingerprintit.model.SectionModel;
import com.example.i2ichest_.fingerprintit.model.SubjectModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ListViewAdapter extends BaseSwipeAdapter {
    List<String> list;
    private Context mContext;
    int countView = 0;
    private Activity parentActivity;
    WSManager wsManager;
    PendingIntent pendingIntent;
    Intent intent;
    List<String> showTimeSubject;
    String showTimeToStudy;

    public ListViewAdapter(Context mContext,List<String> lists,Activity parentActivity) {
        this.mContext = mContext;
        this.list = lists;
        this.parentActivity = parentActivity;
    }

    public int getCountView() {
        return countView;
    }

    public void setCountView(int countView) {
        this.countView = countView;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, final ViewGroup parent) {
        final String splitSubject[] = this.list.get(position).split("=");
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        v.findViewById(R.id.setAlarm).setTag(splitSubject[0]);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));

        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        v.findViewById(R.id.setAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String subject = view.getTag().toString();
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(parentActivity);
                final View alertView = LayoutInflater.from(mContext).inflate(R.layout.setting_before_class_alert, null);
                final NumberPicker hr = (NumberPicker)alertView.findViewById(R.id.hour);
                final NumberPicker mit = (NumberPicker)alertView.findViewById(R.id.minutes);
                hr.setMinValue(0);
                hr.setMaxValue(23);
                mit.setMinValue(0);
                mit.setMaxValue(59);
                hr.setWrapSelectorWheel(true);
                wsManager = WSManager.getWsManager(parentActivity);
                SubjectModel subjectModel = new SubjectModel();
                subjectModel.getSubject().setSubjectID(Long.parseLong(subject));
                wsManager.doSearchPeriod(subjectModel, new WSManager.WSManagerListener() {
                    @Override
                    public void onComplete(Object response) {
                            String showTimeToStudy = "";
                            final SectionModel section =  (SectionModel)response;
                            for(PeriodModel.Period period:section.getSection().getPeriodList()){
                                showTimeToStudy+= "วัน "+period.getDayOfWeek()+" "+period.getPeriodStartTime()+"-"+period.getPeriodEndTime()+"\n";
                            }
                            TextView allPeriodShow = (TextView)alertView.findViewById(R.id.periodSubect);
                            allPeriodShow.setText(showTimeToStudy);
                            alertDialog.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                            Switch onOff = (Switch)alertView.findViewById(R.id.onOff);
                                                for(PeriodModel.Period period:section.getSection().getPeriodList()){

                                                    Calendar calendar  = Calendar.getInstance();
                                                    int start = Integer.parseInt(period.getPeriodStartTime().split(":")[0]);
                                                    final int timeID = (int)(long)period.getPeriodID();
                                                    if(onOff.isChecked() == true) {
                                                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                                        Log.d("Days of week   ",dayOfWeek+" == "+setDateSubject(period.getDayOfWeek())+" GET NUM OF WEEK "+Calendar.SUNDAY);
                                                        if(dayOfWeek == setDateSubject(period.getDayOfWeek())){
                                                            if(calendar.get(Calendar.HOUR_OF_DAY)<start){
                                                                calendar.set(Calendar.HOUR_OF_DAY, start -  hr.getValue());
                                                                calendar.set(Calendar.MINUTE, mit.getValue());
                                                                Log.d("EQULAS  ",start+" == "+ hr.getValue());
                                                            }else {
                                                                calendar.add(Calendar.DATE,7);
                                                                calendar.set(Calendar.HOUR_OF_DAY, start -  hr.getValue());
                                                                calendar.set(Calendar.MINUTE, mit.getValue());
                                                            }
                                                        }else if (dayOfWeek > setDateSubject(period.getDayOfWeek())){
                                                            calendar.add(Calendar.DATE,7-(dayOfWeek - setDateSubject(period.getDayOfWeek())));
                                                            calendar.set(Calendar.HOUR_OF_DAY, start -  hr.getValue());
                                                            calendar.set(Calendar.MINUTE, mit.getValue());
                                                            Log.d("MORE THAN  ",start+" == "+ hr.getValue());

                                                        }else if (dayOfWeek < setDateSubject(period.getDayOfWeek())) {
                                                            calendar.add(Calendar.DATE,(setDateSubject(period.getDayOfWeek())-dayOfWeek));
                                                            calendar.set(Calendar.HOUR_OF_DAY, start -  hr.getValue());
                                                            calendar.set(Calendar.MINUTE, mit.getValue());
                                                            Log.d("LESS THAN  ",start+" == "+ hr.getValue());
                                                        }
                                                        intent = new Intent(parentActivity, AlarmReceiver.class);
                                                        AlarmManager alm = (AlarmManager) parentActivity.getSystemService(parentActivity.ALARM_SERVICE);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.putExtra("study", splitSubject[1]);
                                                        intent.putExtra("period",period.getPeriodID());
                                                        pendingIntent = PendingIntent.getBroadcast(parentActivity,timeID, intent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
                                                        alm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000, pendingIntent);
                                                        Toast.makeText(parentActivity, "ตั้งเวลาการเตือน "+calendar.getTime(), Toast.LENGTH_LONG).show();
                                                    }else{
                                                        Intent in = new Intent(parentActivity, AlarmReceiver.class);
                                                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        AlarmManager alarmCancel = (AlarmManager) parentActivity.getSystemService(parentActivity.ALARM_SERVICE);
                                                        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(parentActivity,timeID, in,PendingIntent.FLAG_UPDATE_CURRENT| Intent.FILL_IN_DATA);
                                                        pendingIntentCancel.cancel();
                                                        alarmCancel.cancel(pendingIntentCancel);
                                                        Toast.makeText(parentActivity, "ยกเลิกการเตือน ", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                    dialogInterface.cancel();
                                }
                            });

                        alertDialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alertDialog.setView(alertView);
                        AlertDialog alert = alertDialog.create();
                        alert.show();
                    }

                    @Override
                    public void onError(String error) {
                        Log.d(TAG, "onError: "+error);
                    }
                });
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.position);
        String splitSubject[] = this.list.get(position).split("=");
        t.setText(splitSubject[1]);
    }

    @Override
    public int getCount() {
        return getCountView();
    }

    @Override
    public Object getItem(int position) {
        return this.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public int setDateSubject ( String date) {
        int numberDay = 1;
        if("จันทร์".equals(date)){
            numberDay = 2;
        }else if ("อังคาร".equals(date)) {
            numberDay = 3;
        }else if ("พุธ".equals(date)) {
            numberDay = 4;
        }else if ("พฤหัสบดี".equals(date)) {
            numberDay = 5;
        }else if ("ศุกร์".equals(date)) {
            numberDay = 6;
        }else if ("เสาร์".equals(date)) {
            numberDay = 7;
        }
        return numberDay;
    }
}
