package com.kch.www.newkchproject.Adapter;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kch.www.newkchproject.DataSet.MoveListDataSet;
import com.kch.www.newkchproject.DataSet.RecyclerDataSete;
import com.kch.www.newkchproject.R;
import com.kch.www.newkchproject.ThActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YONSAI on 2017-05-25.
 */

public class cardAdapter extends RecyclerView.Adapter<cardAdapter.ViewHolder>{

    Context context;
    List<RecyclerDataSete> items;
    int item_layout;
    SQLiteDatabase database;
    ArrayList<MoveListDataSet> moveList;


    public cardAdapter(Context context, List<RecyclerDataSete> items, int item_layout, SQLiteDatabase database, ArrayList<MoveListDataSet> moveList) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
        this.database = database;
        this.moveList = moveList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlist_item,null);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final RecyclerDataSete item = items.get(position);
        Log.d("items - "," "+items.get(position).getId());
        Log.d("item - "," "+item.getId());

        Drawable d = new BitmapDrawable(context.getResources(), (Bitmap)item.getId());

        holder.image.setImageDrawable(d);
        holder.title.setText(item.getText());
        holder.progress.setProgress(items.get(position).getProg());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,item.getText(),Toast.LENGTH_SHORT).show();
                Cursor cursor = database.rawQuery("SELECT * FROM userDate",null);
                Log.d(" DB count :"," "+cursor.getCount());
                //Log.d(" preg count :"," "+item.getProg());

                long now = System.currentTimeMillis();
                int days =(int)((now)/(1000*60*60*24));
                //int test = days-40;   //확인용

                String number = null;

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getText().equals(item.getText())) {
                        int j = i + 1;
                        database.execSQL("UPDATE userDate SET createDate = " + days + " WHERE _id =" + j);
                        Log.d(" i count :", " " + i);
                        number = moveList.get(i).getNumber();
                    }
                }

                //database.execSQL("UPDATE userDate SET createDate = "+days);
                cursor.close();

                calling(number);
            }
        });

    }


    @Override
    public int getItemCount() {
        return this.items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        ProgressBar progress;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.img);
            title=(TextView)itemView.findViewById(R.id.textTitle);
            progress = (ProgressBar)itemView.findViewById(R.id.progress);
            cardview=(CardView)itemView.findViewById(R.id.cardview);
        }
    }


    public void calling(String number)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(context,"권한 실패",Toast.LENGTH_SHORT).show();

            return;
        }
        context.startActivity(intent);
    }

}
