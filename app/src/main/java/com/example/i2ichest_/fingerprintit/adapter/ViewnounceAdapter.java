package com.example.i2ichest_.fingerprintit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.i2ichest_.fingerprintit.R;
import com.example.i2ichest_.fingerprintit.model.AnnouceNewsModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;

/**
 * Created by I2ichest_ on 8/20/2017.
 */

public class ViewnounceAdapter extends BaseAdapter {
    Context context;
    List<AnnouceNewsModel.AnnouceNews> an;
    int countAdapter = 0;
    List<String> setColor;

    public ViewnounceAdapter(Context context, List<AnnouceNewsModel.AnnouceNews> an, int countAdapter, List<String> setColor) {
        this.context = context;
        this.an = an;
        this.countAdapter = countAdapter;
        this.setColor = setColor;
    }

    public List<String> getSetColor() {
        return setColor;
    }

    public void setSetColor(List<String> setColor) {
        this.setColor = setColor;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<AnnouceNewsModel.AnnouceNews> getAn() {
        return an;
    }

    public void setAn(List<AnnouceNewsModel.AnnouceNews> an) {
        this.an = an;
    }

    public int getCountAdapter() {
        return countAdapter;
    }

    public void setCountAdapter(int countAdapter) {
        this.countAdapter = countAdapter;
    }

    @Override
    public int getCount() {
        return getCountAdapter();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_unread, null);
        TextView text = (TextView)view.findViewById(R.id.informTxt);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        text.setText("วิชา: "+an.get(i).getSchedule().getPeriod().getSection().getSubject().getSubjectNumber()+" "+an.get(i).getSchedule().getPeriod().getSection().getSubject().getSubjectName()+" "
        +an.get(i).getAnnouceNewsType()+" ผู้ประกาศ: "+an.get(i).getTeacher().getTitle()+an.get(i).getTeacher().getFirstName()
                +" "+an.get(i).getTeacher().getLastName()+" วันที่ประกาศ: "+sdf.format(an.get(i).getSchedule().getScheduleDate())
        );
        if(setColor.get(i).equals("set")){
            text.setBackgroundResource(R.color.grey_unread);
            Log.d(setColor.get(i),setColor.get(i));
        }else{
            text.setBackgroundColor(Color.WHITE);
        }

        return view;
    }
}
