package com.kch.www.newkchproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BusinessCardActivity extends AppCompatActivity implements View.OnClickListener{

    EditText nameEt;
    EditText addrEt;
    EditText positionEt;
    EditText emailEt;
    EditText faxEt;
    EditText callEt;
    EditText phoneEt;
    Button yBtn;
    Button nBtn;
    Button bcBtn;

    String dbName;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);

        nameEt = (EditText)findViewById(R.id.nameEt);
        addrEt = (EditText)findViewById(R.id.addrEt);
        positionEt = (EditText)findViewById(R.id.positionEt);
        emailEt = (EditText)findViewById(R.id.emailEt);
        faxEt = (EditText)findViewById(R.id.faxEt);
        callEt = (EditText)findViewById(R.id.callEt);
        phoneEt = (EditText)findViewById(R.id.phoneEt);
        yBtn = (Button)findViewById(R.id.yBtn);
        nBtn = (Button)findViewById(R.id.nBtn);
        bcBtn = (Button)findViewById(R.id.bcBtn);

        yBtn.setOnClickListener(this);
        nBtn.setOnClickListener(this);
        bcBtn.setOnClickListener(this);

        dbName = "self2DB";

        DBHelper helper = new DBHelper(this,dbName,null,1);
        database = helper.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.yBtn)
        {
            database.execSQL("UPDATE self SET  name = '" + nameEt.getText()+"'" );
            database.execSQL("UPDATE self SET  addr = '" + addrEt.getText()+"'" );
            database.execSQL("UPDATE self SET  position = '" + positionEt.getText()+"'" );
            database.execSQL("UPDATE self SET  mail = '" + emailEt.getText()+"'" );
            database.execSQL("UPDATE self SET  fax = '" + faxEt.getText()+"'" );
            database.execSQL("UPDATE self SET  call = '" + callEt.getText()+"'" );
            database.execSQL("UPDATE self SET  phone = '" + phoneEt.getText()+"'" );

            Cursor cursor = database.rawQuery("select * from self",null);

            cursor.moveToFirst();

            Log.d("data print :",cursor.getString(0) +" "+cursor.getString(1));

            Toast.makeText(this,"저장 되었습니다.",Toast.LENGTH_SHORT).show();

        }
        else if(v.getId()==R.id.nBtn)
        {
            finish();
        }
        else if(v.getId()==R.id.bcBtn)
        {
            Cursor cursor = database.rawQuery("select * from self",null);

            cursor.moveToFirst();


            Intent sock = new Intent(this, SocketActivity.class);
            sock.putExtra("name",cursor.getString(0));
            sock.putExtra("addr",cursor.getString(1));
            sock.putExtra("position",cursor.getString(2));
            sock.putExtra("email",cursor.getString(3));
            sock.putExtra("fax",cursor.getString(4));
            sock.putExtra("call",cursor.getString(5));
            sock.putExtra("phone",cursor.getString(6));
            startActivity(sock);

            cursor.close();
        }

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
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            Cursor cursor = db.rawQuery("select * from self",null);

            cursor.moveToFirst();
            nameEt.setText(cursor.getString(0));

            addrEt.setText(cursor.getString(1));
            positionEt.setText(cursor.getString(2));
            emailEt.setText(cursor.getString(3));
            faxEt.setText(cursor.getString(4));
            callEt.setText(cursor.getString(5));
            phoneEt.setText(cursor.getString(6));

            Toast.makeText(context,"data : "+cursor.getCount(),Toast.LENGTH_SHORT).show();
            cursor.close();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE self ('name' TEXT,'addr' TEXT,'position' TEXT,'mail' TEXT, 'fax' TEXT,'call' TEXT,'phone' TEXT)");

            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='self'" , null);
            cursor.moveToFirst();

            if(cursor.getCount()>0){
                Log.d("onOpenDB생성","완료");
            }else{
                Log.d("onOpenDB생성","실패");
            }

            cursor.close();

            db.execSQL("INSERT INTO self VALUES('','','','','','','')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
