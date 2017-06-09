package com.kch.www.newkchproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kch.www.newkchproject.DataSet.Right1DataSet;
import com.kch.www.newkchproject.R;

import java.util.ArrayList;

/**
 * Created by YONSAI on 2017-05-23.
 */

public class Right1Adapter extends BaseAdapter{

    ArrayList<Right1DataSet> data;

    public Right1Adapter(ArrayList<Right1DataSet> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.rightlist_item,parent,false);

        TextView tv1 = (TextView)convertView.findViewById(R.id.r1Tv);
        TextView tv2 = (TextView)convertView.findViewById(R.id.r2Tv);

        tv1.setText(data.get(position).getDate());
        tv2.setText(data.get(position).getMsg());

        return convertView;
    }

    public void addDate()
    {

    }
}
