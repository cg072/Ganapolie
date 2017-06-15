package com.kch.www.newkchproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.kch.www.newkchproject.Adapter.MoveAdapter;
import com.kch.www.newkchproject.Adapter.cardAdapter;
import com.kch.www.newkchproject.DataSet.MoveListDataSet;
import com.kch.www.newkchproject.DataSet.RecyclerDataSete;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    List<RecyclerDataSete> items;
    cardAdapter cardAdapter;

    ArrayList<MoveListDataSet> moveList;

    ArrayList<Integer> datelist;
    String dbName;
    SQLiteDatabase database;

    private static final int version = 1;

    private static final int mil = 1000;
    private static final int sed = 60;
    private static final int min = 60;
    private static final int hour = 24;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_th);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //연락처
        Intent i = getIntent();

        moveList = i.getParcelableArrayListExtra("array");

        Log.d("SubActivity",moveList.get(1).getName());

        //db
        dbName = "dateDB";

        DBHelper helper = new DBHelper(this,dbName,null,version);
        database = helper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM userDate",null);

        Log.d(" DB count :"," "+cursor.getCount());

        cursor.moveToFirst();

        datelist = new ArrayList<>();
        int ii = 0;
        while(ii<cursor.getCount())
        {
            Log.d(" DB : ",cursor.getInt(0)+" "+cursor.getInt(1)+" "+cursor.getInt(2));
            datelist.add(cursor.getInt(2)-cursor.getInt(1));
            Log.d(" DB num : ",""+datelist.get(ii));
            ii=ii+1;
            cursor.moveToNext();
        }

        cursor.close();

        //recycler
        recyclerView = (RecyclerView)findViewById(R.id.recy);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();

        Log.d("this index dateList :"," "+datelist.size());
        Log.d("this index moveList :"," "+moveList.size());
        for(int num = 0; num< moveList.size();num++ )
        {
            items.add(new RecyclerDataSete(moveList.get(num).getImg(),moveList.get(num).getName(),datelist.get(num)));
        }
        // 검색 한번 찾아보기

        cardAdapter = new cardAdapter(getApplicationContext(),items,R.layout.cardlist_item,database,moveList);

        recyclerView.setAdapter(cardAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

        cardAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = database.rawQuery("SELECT * FROM userDate",null);

        Log.d(" DB count :"," "+cursor.getCount());

        cursor.moveToFirst();

        datelist = new ArrayList<>();
        int ii = 0;
        while(ii<cursor.getCount())
        {
            Log.d(" DB : ",cursor.getInt(0)+" "+cursor.getInt(1)+" "+cursor.getInt(2));
            datelist.add(cursor.getInt(2)-cursor.getInt(1));
            Log.d(" DB num : ",""+datelist.get(ii));
            ii=ii+1;
            cursor.moveToNext();
        }

        cursor.close();

        //recycler
        recyclerView = (RecyclerView)findViewById(R.id.recy);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();

        Log.d("this index dateList :"," "+datelist.size());
        Log.d("this index moveList :"," "+moveList.size());
        for(int num = 0; num< moveList.size();num++ )
        {
            items.add(new RecyclerDataSete(moveList.get(num).getImg(),moveList.get(num).getName(),datelist.get(num)));
        }


        cardAdapter = new cardAdapter(getApplicationContext(),items,R.layout.cardlist_item,database,moveList);

        recyclerView.setAdapter(cardAdapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(this, SubActivity.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putParcelableArrayListExtra("array",moveList);

            startActivity(i);
        } else if (id == R.id.nav_slideshow) {
            Intent in = new Intent(this, ThActivity.class);

            in.putParcelableArrayListExtra("array",moveList);

            startActivity(in);

        } else if (id == R.id.nav_manage) {
            Intent bc = new Intent(this, BusinessCardActivity.class);
            startActivity(bc);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class DBHelper extends SQLiteOpenHelper
    {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            db.execSQL("CREATE TABLE IF NOT EXISTS userDate(_id INTEGER PRIMARY KEY AUTOINCREMENT,createDate INTEGER, thisDate INTEGER)");

            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='userDate'" , null);
            cursor.moveToFirst();

            if(cursor.getCount()>0){
                Log.d("onOpenDB생성","완료");
            }else{
                Log.d("onOpenDB생성","실패");
            }

            cursor.close();

            linkData(db);
            dateUpdate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS userDate(_id INTEGER PRIMARY KEY AUTOINCREMENT,createDate INTEGER, thisDate INTEGER)");
            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='userDate'" , null);
            cursor.moveToFirst();

            if(cursor.getCount()>0){
                Log.d("onOpenDB생성","완료");
            }else{
                Log.d("onOpenDB생성","실패");
            }

            cursor.close();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void linkData(SQLiteDatabase db)
    {
        int tableCount = 0;


        Cursor cursor = db.rawQuery("select * from userDate",null);


        Toast.makeText(this, "data : "+cursor.getCount(), Toast.LENGTH_SHORT).show();
        tableCount = cursor.getCount();

        long now = System.currentTimeMillis();
        int days =(int)((now)/(mil*sed*min*hour));


        if(tableCount != moveList.size())
        {
            for(int i = tableCount+1; i <= moveList.size();i++)
            {
                db.execSQL("INSERT INTO userDate VALUES("+i+",'"+days+"','"+days+"')");
            }
        }

        cursor.close();

    }



    public void dateUpdate(SQLiteDatabase db) {
        long now = System.currentTimeMillis();
        int days =(int)((now)/(mil*sed*min*hour));

        db.execSQL("UPDATE userDate SET thisDate = "+days);
    }




}
