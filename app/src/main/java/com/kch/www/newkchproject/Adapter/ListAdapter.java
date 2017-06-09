package com.kch.www.newkchproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kch.www.newkchproject.DataSet.UserlistDataset;
import com.kch.www.newkchproject.R;

import java.util.ArrayList;

/**
 * Created by YONSAI on 2017-05-12.
 */

public class ListAdapter extends BaseAdapter{

    ArrayList<UserlistDataset> data;

    public ListAdapter(ArrayList<UserlistDataset> data) {
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
        convertView = inflater.inflate(R.layout.userlist_item,parent,false);

        ImageView img = (ImageView)convertView.findViewById(R.id.userlist_img);
        TextView name = (TextView)convertView.findViewById(R.id.userlist_name);
        TextView text = (TextView)convertView.findViewById(R.id.userlist_text);

        img.setImageResource(data.get(position).getId());
        name.setText(data.get(position).getName());
        text.setText(data.get(position).getText());


        return convertView;
    }
}
