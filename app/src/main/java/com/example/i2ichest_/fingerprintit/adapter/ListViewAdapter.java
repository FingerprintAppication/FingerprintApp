package com.example.i2ichest_.fingerprintit.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.i2ichest_.fingerprintit.R;

import java.sql.Time;
import java.util.List;

public class ListViewAdapter extends BaseSwipeAdapter {
    List<String> list;
    private Context mContext;
    int countView = 0;
    private Activity parentActivity;

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
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        v.findViewById(R.id.setAlarm).setTag(list.get(position));

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

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(parentActivity);
                final View v = LayoutInflater.from(mContext).inflate(R.layout.setting_before_class_alert, null);
                Button save = (Button)v.findViewById(R.id.save);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("get in button","===========");
                        TimePicker time = (TimePicker) v.findViewById(R.id.timePicker);
                        Toast.makeText(mContext, "click time picker = "+time.getHour()+" "+time.getMinute(), Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setView(v);
                AlertDialog alert = alertDialog.create();
                alert.show();
                Toast.makeText(mContext, "click alarm "+view.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.position);
        t.setText(this.list.get(position));
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
}
