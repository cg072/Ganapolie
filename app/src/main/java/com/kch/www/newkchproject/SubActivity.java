package com.kch.www.newkchproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kch.www.newkchproject.Adapter.AddrAdapter;
import com.kch.www.newkchproject.Adapter.ListAdapter;
import com.kch.www.newkchproject.Adapter.MoveAdapter;
import com.kch.www.newkchproject.Adapter.PagerAdapter;
import com.kch.www.newkchproject.DataSet.ArraylistSen;
import com.kch.www.newkchproject.DataSet.MoveListDataSet;
import com.kch.www.newkchproject.DataSet.PagerDataSet;
import com.kch.www.newkchproject.DataSet.UserlistDataset;

import java.util.ArrayList;
import java.util.Map;

public class SubActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    ListView list;
    MoveAdapter moveAdapter;


    ViewPager pager;
    PagerAdapter pagerAdapter;
    ArrayList<PagerDataSet> pagerData;
    TabLayout tab;

    ArrayList<MoveListDataSet> moveList;

    int position = 0;

    String dbName;
    SQLiteDatabase database;
    String tableName;

    private static final int PageLimit = 3;
    private static final int version = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        pagerData = new ArrayList<PagerDataSet>();

        //pagerData.add(new PagerDataSet(new Sub3Fragment(),"Profile",R.drawable.ic_account_circle_black_24dp));
        pagerData.add(new PagerDataSet(new Sub2Fragment(),"Folder",R.drawable.ic_folder_shared_black_24dp));
        pagerData.add(new PagerDataSet(new Sub1Fragment(),"Memo",R.drawable.ic_text_fields_black_24dp));

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),pagerData);

        pager = (ViewPager)findViewById(R.id.pager);
        tab = (TabLayout)findViewById(R.id.tab);
        pager.setOffscreenPageLimit(PageLimit);

        pager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(pager);

        for(int i = 0; i<pagerData.size(); i++)
        {
            tab.getTabAt(i).setIcon(pagerData.get(i).getId());
        }



        //연락처
        Intent i = getIntent();

        moveList = i.getParcelableArrayListExtra("array");

        Log.d("SubActivity",moveList.get(1).getName());

        moveAdapter = new MoveAdapter(moveList);

        list = (ListView)findViewById(R.id.subList);

        list.setAdapter(moveAdapter);

        list.setOnItemClickListener(this);

        //DB

        dbName = "kch2DB";

        DBHelper helper = new DBHelper(this, dbName, null, version);
        database = helper.getWritableDatabase();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
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
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(this, SubActivity.class);

            i.putParcelableArrayListExtra("array",moveList);

            startActivity(i);

        } else if (id == R.id.nav_slideshow) {
            Intent in = new Intent(this, ThActivity.class);

            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.putParcelableArrayListExtra("array",moveList);

            startActivity(in);
        } else if (id == R.id.nav_manage) {
            Intent bc = new Intent(this, BusinessCardActivity.class);
            startActivity(bc);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnClick(int check)
    {
        switch (check)
        {
            case 1:

                Intent i = new Intent(this,ProfileActivity.class);
                i.putParcelableArrayListExtra("array",moveList);
                i.putExtra("position",position);
                i.putExtra("dbName",dbName);
                startActivity(i);

                Log.d("Position ",""+position);
                break;

            case 2:
                EditText et = (EditText)pagerData.get(1).getFrag().getView().findViewById(R.id.et);
                int dbPosition = position + 1;
                //db.execSQL("UPDATE 테이블명 SET update 할 컬럼명 = '업데이트 할 문자열' WHERE 조건이 되는 컬럼 = '값';")
                database.execSQL("UPDATE king SET memo = '"+et.getText()+"' WHERE _id =" + dbPosition);
                break;
        }
    }

    public void linkData(SQLiteDatabase db)
    {
        int tableCount = 0;

        //db.execSQL("delete from king");

        Cursor cursor = db.rawQuery("select * from king",null);


        Toast.makeText(this, "data : "+cursor.getCount(), Toast.LENGTH_SHORT).show();
        tableCount = cursor.getCount();



        if(tableCount != moveList.size())
        {
            for(int i = tableCount+1; i <= moveList.size();i++)
            {
                db.execSQL("INSERT INTO king VALUES("+i+",'','','','','','')");
            }
        }
//
//        cursor = db.rawQuery("select * from king",null);
//
//        cursor.moveToFirst();
//
//        Toast.makeText(this, "data : "+cursor.getCount(), Toast.LENGTH_SHORT).show();
//
//        for(int j =0; j<3;j++)
//        {
//            Log.d(" SELECT : 1. ", " : " + cursor.getInt(0));
//            cursor.moveToNext();
//        }

        cursor.close();

        //db.execSQL("INSERT INTO king (addr,position,email,fax,call)VALUES('화성시','시장','asdf@naver.com','03243241','010234235')");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ImageView iv = (ImageView)pagerData.get(0).getFrag().getView().findViewById(R.id.sub2img);
        TextView tv = (TextView)pagerData.get(0).getFrag().getView().findViewById(R.id.name);
        TextView tv2 = (TextView)pagerData.get(0).getFrag().getView().findViewById(R.id.phone);
        TextView tv3 = (TextView)pagerData.get(0).getFrag().getView().findViewById(R.id.addr);
        TextView tv4 = (TextView)pagerData.get(0).getFrag().getView().findViewById(R.id.position);
        TextView tv5 = (TextView)pagerData.get(0).getFrag().getView().findViewById(R.id.email);
        TextView tv6 = (TextView)pagerData.get(0).getFrag().getView().findViewById(R.id.fax);
        TextView tv7 = (TextView)pagerData.get(0).getFrag().getView().findViewById(R.id.call);
        EditText et = (EditText)pagerData.get(1).getFrag().getView().findViewById(R.id.et);


        for(int i = 0; i<moveList.size(); i++) {
            if (moveList.get(i) == parent.getItemAtPosition(position)) {
                Drawable d = new BitmapDrawable(getResources(), (Bitmap) moveList.get(i).getImg());
                iv.setImageDrawable(d);
                tv.setText("이름 : "+moveList.get(i).getName());
                tv2.setText("phone : "+moveList.get(i).getNumber());

                int dbPosition = position + 1;
                Cursor cursor = database.rawQuery("SELECT * FROM king WHERE _id ="+dbPosition,null);
                cursor.moveToFirst();


                tv3.setText("주소 : "+cursor.getString(1));
                tv4.setText("직책 : "+cursor.getString(2));
                tv5.setText("E-mail :"+cursor.getString(3));
                tv6.setText("Fax : "+cursor.getString(4));
                tv7.setText("Call : "+cursor.getString(5));
                et.setText(cursor.getString(6));

                cursor.close();
            }
        }

        this.position = position;

    }

    @Override
    protected void onStart() {
        super.onStart();


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
            db.execSQL("CREATE TABLE IF NOT EXISTS king(_id INTEGER PRIMARY KEY AUTOINCREMENT,addr TEXT, position TEXT, email TEXT, fax TEXT, call TEXT, memo TEXT)");

            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='king'" , null);
            cursor.moveToFirst();

            if(cursor.getCount()>0){
                Log.d("onOpenDB생성","완료");
            }else{
                Log.d("onOpenDB생성","실패");
            }

            cursor.close();

            linkData(db);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            tableName = "king";
            db.execSQL("CREATE TABLE IF NOT EXISTS king(_id INTEGER PRIMARY KEY AUTOINCREMENT,addr TEXT, position TEXT, email TEXT, fax TEXT, call TEXT, memo TEXT)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
