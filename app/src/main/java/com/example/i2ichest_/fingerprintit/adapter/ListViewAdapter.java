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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.i2ichest_.fingerprintit.R;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.AlarmReceiver;
import com.example.i2ichest_.fingerprintit.model.PeriodModel;
import com.example.i2ichest_.fingerprintit.model.SectionModel;
import com.example.i2ichest_.fingerprintit.model.SubjectModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
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
                wsManager = WSManager.getWsManager(parentActivity);
                SubjectModel subjectModel = new SubjectModel();
                subjectModel.getSubject().setSubjectID(Long.parseLong(subject));
                wsManager.doSearchPeriod(subjectModel, new WSManager.WSManagerListener() {
                    @Override
                    public void onComplete(Object response) {
                        try {
                            showTimeToStudy="";
                            JSONArray jsonArray = new JSONArray(response.toString());
                            JSONObject jsonSection = new JSONObject(jsonArray.get(0).toString());
                            final SectionModel section = new SectionModel(jsonSection.toString());

                            for(PeriodModel.Period period:section.getSection().getPeriodList()){
                                    showTimeToStudy += "วัน "+period.getDayOfWeek()+" "+period.getPeriodStartTime()+"-"+period.getPeriodEndTime()+"\n";
                            }

                            Toast.makeText(parentActivity,showTimeToStudy, Toast.LENGTH_LONG).show();
                            alertDialog.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                            Switch onOff = (Switch)alertView.findViewById(R.id.onOff);
                                            TimePicker time = (TimePicker) alertView.findViewById(R.id.timePicker);
                                                for(PeriodModel.Period period:section.getSection().getPeriodList()){
                                                    intent = new Intent(parentActivity, AlarmReceiver.class);
                                                    AlarmManager alm = (AlarmManager) parentActivity.getSystemService(parentActivity.ALARM_SERVICE);
                                                    Calendar calendar  = Calendar.getInstance();
                                                    int start = Integer.parseInt(period.getPeriodStartTime().split(":")[0]);
                                                    if(onOff.isChecked() == true) {


                                                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                                        Log.d("Days of week   ",dayOfWeek+" == "+setDateSubject(period.getDayOfWeek())+" GET NUM OF WEEK "+Calendar.SUNDAY);
                                                        if(dayOfWeek == setDateSubject(period.getDayOfWeek())){
                                                            if(calendar.get(Calendar.HOUR_OF_DAY)<start){
                                                                //calendar = Calendar.getInstance();
                                                               // Log.d("start and getHour ",start+" == "+time.getHour());
                                                                calendar.set(Calendar.HOUR_OF_DAY, start - time.getHour());
                                                                calendar.set(Calendar.MINUTE, time.getMinute());
                                                                Log.d("EQULAS  ",start+" == "+time.getHour());
                                                            }else {
                                                                //calendar = Calendar.getInstance();
                                                                calendar.add(Calendar.DATE,7);
                                                                calendar.set(Calendar.HOUR_OF_DAY, start - time.getHour());
                                                                calendar.set(Calendar.MINUTE, time.getMinute());
                                                            }
                                                        }else if (dayOfWeek > setDateSubject(period.getDayOfWeek())){
                                                            //calendar = Calendar.getInstance();
                                                            calendar.add(Calendar.DATE,7-(dayOfWeek - setDateSubject(period.getDayOfWeek())));
                                                            calendar.set(Calendar.HOUR_OF_DAY, start - time.getHour());
                                                            calendar.set(Calendar.MINUTE, time.getMinute());
                                                            Log.d("MORE THAN  ",start+" == "+time.getHour());

                                                        }else if (dayOfWeek < setDateSubject(period.getDayOfWeek())) {
                                                            //calendar = Calendar.getInstance();
                                                            calendar.add(Calendar.DATE,(setDateSubject(period.getDayOfWeek())-dayOfWeek));
                                                            calendar.set(Calendar.HOUR_OF_DAY, start - time.getHour());
                                                            calendar.set(Calendar.MINUTE, time.getMinute());
                                                            Log.d("LESS THAN  ",start+" == "+time.getHour());
                                                        }
                                                        final int tiemId = (int)(long)period.getPeriodID();
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.putExtra("study", splitSubject[1]);
                                                        intent.putExtra("period",period.getPeriodID());
                                                        pendingIntent = PendingIntent.getBroadcast(parentActivity,tiemId, intent, PendingIntent.FLAG_ONE_SHOT | Intent.FILL_IN_DATA);
                                                        alm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000, pendingIntent);
                                                        Toast.makeText(parentActivity, "SET TIME PERIOD ID + "+period.getPeriodID(), Toast.LENGTH_LONG).show();
                                                        Toast.makeText(parentActivity, "SET TIME "+calendar.getTime(), Toast.LENGTH_LONG).show();
                                                        //Log.d("DATE ",calendar.getTime()+"");
                                                    }else{
                                                        pendingIntent = PendingIntent.getActivity(parentActivity,(int)(long)period.getPeriodID(), intent,PendingIntent.FLAG_UPDATE_CURRENT| Intent.FILL_IN_DATA);
                                                        pendingIntent.cancel();
                                                        alm.cancel(pendingIntent);
                                                    }
                                                }
                                    dialogInterface.cancel();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

                    }
                });
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.position);
<<<<<<< HEAD
        String splitSubject[] = this.list.get(position).split("=");
        t.setText(splitSubject[1]);
=======
        Log.d("setText",this.list.get(position).toString());
        t.setText(this.list.get(position).toString());
>>>>>>> 35b51a2ae4936ee3e7ab747bc281893c7b5a4fdb
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


    public int setDateSubject (String date) {
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
