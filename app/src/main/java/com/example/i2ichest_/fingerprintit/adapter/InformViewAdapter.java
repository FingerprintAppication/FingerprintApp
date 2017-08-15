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
import java.util.List;

public class InformViewAdapter extends BaseAdapter{
    Context context;
    List<String> inform;
    List<String> setColor;
    int numCount = 0;

    public InformViewAdapter(Context context, List<String> inform, List<String> setColor, int numCount) {
        this.context = context;
        this.inform = inform;
        this.setColor = setColor;
        this.numCount = numCount;
    }

    @Override
    public int getCount() {
        return numCount;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_unread, null);
        TextView text = (TextView)view.findViewById(R.id.informTxt);
        text.setText(inform.get(i).toString());
        if(setColor.get(i).equals("set")){
            text.setBackgroundResource(R.color.grey_unread);
            Log.d(setColor.get(i),setColor.get(i));
        }else{
            text.setBackgroundColor(Color.WHITE);
        }

        return view;
    }
}
