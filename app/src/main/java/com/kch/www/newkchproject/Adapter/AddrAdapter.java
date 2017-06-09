package com.kch.www.newkchproject.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kch.www.newkchproject.MainActivity;
import com.kch.www.newkchproject.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by YONSAI on 2017-05-16.
 */

public class AddrAdapter extends BaseAdapter{

    Context context;
    int res;
    ArrayList<Map<String,Object>> data;

    public AddrAdapter(Context context, int res, ArrayList<Map<String, Object>> data) {
        this.context = context;
        this.res = res;
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
        convertView = inflater.inflate(res, parent, false);

        ImageView img = (ImageView)convertView.findViewById(R.id.userlist_img);
        TextView name = (TextView)convertView.findViewById(R.id.userlist_name);
        TextView number = (TextView)convertView.findViewById(R.id.userlist_text);

        Drawable d = new BitmapDrawable(convertView.getResources(), (Bitmap)data.get(position).get("img"));

        img.setImageDrawable(d);
        name.setText((String)data.get(position).get("name"));
        number.setText((String)data.get(position).get("number"));

        return convertView;
    }

}
