package com.kch.www.newkchproject;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kch.www.newkchproject.Adapter.AddrAdapter;
import com.kch.www.newkchproject.DataSet.MoveListDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView sub2img;
    EditText nameEt;
    EditText addrEt;
    EditText positionEt;
    EditText emailEt;
    EditText faxEt;
    EditText callEt;
    EditText phoneEt;

    Button eBtn;
    Button cBtn;

    ArrayList<MoveListDataSet> moveList;

    private int position = 0;
    private int phone = 0;
    private int dbPosition = 0;

    String dbName;
    SQLiteDatabase database;


    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sub2img = (ImageView)findViewById(R.id.sub2img);
        nameEt = (EditText)findViewById(R.id.nameEt);
        addrEt = (EditText)findViewById(R.id.addrEt);
        positionEt = (EditText)findViewById(R.id.positionEt);
        emailEt = (EditText)findViewById(R.id.emailEt);
        faxEt = (EditText)findViewById(R.id.faxEt);
        callEt = (EditText)findViewById(R.id.callEt);
        phoneEt = (EditText)findViewById(R.id.phoneEt);

        eBtn = (Button)findViewById(R.id.profileBtn1);
        cBtn = (Button)findViewById(R.id.profileBtn2);



        Intent i = getIntent();

        moveList = i.getParcelableArrayListExtra("array");

        int num = 0;
        position = i.getIntExtra("position",num);

        dbName = i.getStringExtra("dbName");

        DBHelper helper = new DBHelper(this, dbName, null, 3);
        database = helper.getWritableDatabase();

        Log.d("Profile Position",""+position);
        Log.d("Profile num",""+num);

        Drawable d = new BitmapDrawable(getResources(), (Bitmap) moveList.get(position).getImg());
        sub2img.setImageDrawable(d);
        nameEt.setText(moveList.get(position).getName());
        phoneEt.setText(moveList.get(position).getNumber());



        eBtn.setOnClickListener(this);
        cBtn.setOnClickListener(this);
    }



    class DBHelper extends SQLiteOpenHelper
    {
        Context context;

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            this.context = context;
        }

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
            this.context = context;
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Toast.makeText(context, "Database Not Create",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.profileBtn1) {


            //연락처와 DB 동기화 완료
            dbPosition = position + 1;
            Cursor cursor = database.rawQuery("SELECT _id FROM king WHERE _id =" + dbPosition, null);
            cursor.moveToFirst();

            Log.d(" cursor count ", " : " + cursor.getCount());

            Log.d(" SELECT : 1. ", " : " + cursor.getInt(0));

            //정보 입력 구현
            database.execSQL("UPDATE king SET  addr = '" + addrEt.getText() + "' WHERE _id=" + dbPosition);
            database.execSQL("UPDATE king SET  position = '" + positionEt.getText() + "' WHERE _id=" + dbPosition);
            database.execSQL("UPDATE king SET  email = '" + emailEt.getText() + "' WHERE _id=" + dbPosition);
            database.execSQL("UPDATE king SET  fax = '" + faxEt.getText() + "' WHERE _id=" + dbPosition);
            database.execSQL("UPDATE king SET  call = '" + callEt.getText() + "' WHERE _id=" + dbPosition);


            cursor.close();
        }

        finish();
    }
}
