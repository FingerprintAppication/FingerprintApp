package com.example.i2ichest_.fingerprintit.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.i2ichest_.fingerprintit.R;
import com.example.i2ichest_.fingerprintit.manager.WSManager;
import com.example.i2ichest_.fingerprintit.model.AlarmReceiver;
import com.example.i2ichest_.fingerprintit.model.PeriodModel;
import com.example.i2ichest_.fingerprintit.model.SectionModel;
import com.example.i2ichest_.fingerprintit.model.SubjectModel;

import java.lang.reflect.Field;
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
    AlertDialog.Builder alertDialog;
    String subject;
    View alertView;
    NumberPicker hr;
    NumberPicker mit;

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
                subject = view.getTag().toString();
                alertDialog = new AlertDialog.Builder(parentActivity);
                alertView = LayoutInflater.from(mContext).inflate(R.layout.setting_before_class_alert, null);
                hr = (NumberPicker)alertView.findViewById(R.id.hour);
                setNumberPickerTextColor(hr,-16777216);
                mit = (NumberPicker)alertView.findViewById(R.id.minutes);
                setNumberPickerTextColor(mit,-16777216);
                hr.setMinValue(0);
                hr.setMaxValue(23);
                mit.setMinValue(10);
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
                                            List<String> settingData = new ArrayList<String>();
                                                for(PeriodModel.Period period:section.getSection().getPeriodList()){
                                                    Calendar calendar  = Calendar.getInstance();
                                                    int start = Integer.parseInt(period.getPeriodStartTime().split(":")[0]);
                                                    int timeID = (int)(long)period.getPeriodID();
                                                    if(onOff.isChecked() == true) {
                                                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                                        calendar.set(Calendar.HOUR_OF_DAY, start -  hr.getValue());
                                                        calendar.set(Calendar.MINUTE, 0 - mit.getValue());
                                                        calendar.set(Calendar.SECOND, 0);
                                                        if(dayOfWeek == setDateSubject(period.getDayOfWeek())){
                                                            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                                                            if(hourOfDay > start || hourOfDay == start || (start - hourOfDay) < 1){
                                                                calendar.add(Calendar.DATE,7);
                                                                calendar.set(Calendar.HOUR_OF_DAY, start -  hr.getValue());
                                                            }else{
                                                                calendar.set(Calendar.HOUR_OF_DAY, start -  hr.getValue());
                                                            }
                                                        }else if (dayOfWeek > setDateSubject(period.getDayOfWeek())){
                                                            calendar.add(Calendar.DATE,7-(dayOfWeek - setDateSubject(period.getDayOfWeek())));
                                                            Log.d("MORE THAN  ",start+" == "+ hr.getValue());

                                                        }else if (dayOfWeek < setDateSubject(period.getDayOfWeek())) {
                                                            calendar.add(Calendar.DATE,(setDateSubject(period.getDayOfWeek())-dayOfWeek));
                                                            Log.d("LESS THAN  ",start+" == "+ hr.getValue());
                                                        }
                                                        settingData.add("วัน "+period.getDayOfWeek()
                                                                +" เวลา "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+" นาฬิกา");
                                                        intent = new Intent(parentActivity, AlarmReceiver.class);
                                                        AlarmManager alm = (AlarmManager) parentActivity.getSystemService(parentActivity.ALARM_SERVICE);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.putExtra("study", splitSubject[1]);
                                                        intent.putExtra("period",period.getPeriodStartTime()+"-"+period.getPeriodEndTime()+" นาฬิกา");
                                                        pendingIntent = PendingIntent.getBroadcast(parentActivity,timeID, intent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
                                                        alm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                                    }else{

                                                        Intent in = new Intent(parentActivity, AlarmReceiver.class);
                                                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        AlarmManager alarmCancel = (AlarmManager) parentActivity.getSystemService(parentActivity.ALARM_SERVICE);
                                                        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(parentActivity,timeID, in,PendingIntent.FLAG_UPDATE_CURRENT| Intent.FILL_IN_DATA);
                                                        pendingIntentCancel.cancel();
                                                        alarmCancel.cancel(pendingIntentCancel);
                                                    }
                                                }
                                    dialogInterface.cancel();
                                    AlertDialog.Builder settingTime = new AlertDialog.Builder(parentActivity);
                                    settingTime.setIcon(R.drawable.clock);
                                    if(!settingData.isEmpty()){
                                        String data = "";
                                        for (String s : settingData) {
                                            data += s+"\n";
                                        }
                                        settingTime.setTitle("เวลาที่จะแจ้งเตือนก่อนเรียน");
                                        settingTime.setMessage(data );
                                    }else{
                                        settingTime.setTitle("สถานะการแจ้งเตือน");
                                        settingTime.setMessage("ยกเลิกการแจ้งเตือนเสร็จสมบูรณ์");
                                    }
                                    settingTime.create().show();

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

    /*This method is setting NumberPicker text color*/
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                   // Log.d("setNumberPickerTextColor", e);
                }
                catch(IllegalAccessException e){
                   // Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalArgumentException e){
                   // Log.w("setNumberPickerTextColor", e);
                }
            }
        }
        return false;
    }
}
