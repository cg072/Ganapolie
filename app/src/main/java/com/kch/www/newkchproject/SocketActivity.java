package com.kch.www.newkchproject;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketActivity extends AppCompatActivity implements View.OnClickListener{

    Handler handler;
    Socket client;

    OutputStream os;
    InputStream is;
    DataOutputStream dos;
    DataInputStream dis;

    EditText numberEt;
    Button sendBtn;
    Button linkBtn;
    Button sendpfBtn;
    Button saveBtn;

    private static final int columnMax = 7;

    TextView tv1;
    String type[] = {"이름","주소","직책","E-mail","Fax","Call","Phone"};
    String str[] = new String[columnMax];
    String buf[] = new String[columnMax];


    private String ip = "192.168.62.74";
    private int port = 9998;

    String idByANDROID_ID;

    // Request code for WRITE_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_WRITE_CONTACTS = 100;

    String dbName;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        numberEt = (EditText)findViewById(R.id.numberEt);
        sendBtn = (Button)findViewById(R.id.sendBtn);
        linkBtn = (Button)findViewById(R.id.linkBtn);
        sendpfBtn = (Button)findViewById(R.id.sendpfBtn);
        saveBtn = (Button)findViewById(R.id.saveBtn);

        tv1 = (TextView)findViewById(R.id.tv1);

        sendBtn.setOnClickListener(this);
        linkBtn.setOnClickListener(this);
        sendpfBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);


        Intent intent = getIntent();
        buf[0] = intent.getStringExtra("name");
        buf[1] = intent.getStringExtra("addr");
        buf[2] = intent.getStringExtra("position");
        buf[3] = intent.getStringExtra("email");
        buf[4] = intent.getStringExtra("fax");
        buf[5] = intent.getStringExtra("call");
        buf[6] = intent.getStringExtra("phone");

        Log.d("intent name : ",buf[0]+" "+buf[1]+ " "+buf[2]);

        //DB

        dbName = "kch2DB";

        DBHelper helper = new DBHelper(this, dbName, null, 3);
        database = helper.getWritableDatabase();




        handler = new Handler();

        idByANDROID_ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        tv1.append("식별 코드 :"+idByANDROID_ID+"\n\n");
        Log.d("id : ", idByANDROID_ID);

        SocketThread thread = new SocketThread();
        thread.start();


    }


    public void socketSet() {

        try {
            client = new Socket(ip, port);

            Log.d("Socket -","Connect Success");

            os = client.getOutputStream();
            is = client.getInputStream();
            dos = new DataOutputStream(os);
            dis = new DataInputStream(is);



        } catch (IOException e) {
            Log.d("Socket Error  :", "Connect False");

        }
    }

    public void socketClose()
    {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sendBtn) {

            SendThread sendThread = new SendThread(numberEt.getText().toString());
            sendThread.start();

        }
        else if(v.getId() == R.id.linkBtn) {
            SendThread sendThread = new SendThread(idByANDROID_ID);
            sendThread.start();
            //oos.writeObject(idByANDROID_ID);

        }
        else if(v.getId() == R.id.sendpfBtn) {

            DataThread dataThread = new DataThread(buf);
            dataThread.start();

        }
        else if(v.getId() == R.id.saveBtn)
        {
            this.showContacts();
            Cursor cursor = database.rawQuery("select * from king",null);
            int num = cursor.getCount()+1;
            Log.d("num : "," "+num);
            database.execSQL("INSERT INTO king VALUES("+num+", '"+buf[1]+"','"+buf[2]+"','"+buf[3]+"','"+buf[4]+"','"+buf[5]+"',' ')");

            cursor.close();
        }
    }

    class SocketThread extends Thread {
        public void run() {
            socketSet();
            ReadThread readThread = new ReadThread();
            readThread.start();


        }
    }

    class SendThread extends Thread{
        String buf;

        public SendThread(String buf)
        {
            this.buf = buf;
        }

        public void run()
        {
            try {
                dos.writeUTF(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class DataThread extends Thread{
       String buf[];

        public DataThread(String buf[])
        {
            this.buf = buf;
        }

        public void run()
        {
            try {
                dos.writeUTF(buf[0]);
                dos.writeUTF(buf[1]);
                dos.writeUTF(buf[2]);
                dos.writeUTF(buf[3]);
                dos.writeUTF(buf[4]);
                dos.writeUTF(buf[5]);
                dos.writeUTF(buf[6]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ReadThread extends Thread{
        boolean flag = true;
        public void run()
        {
            while(flag) {
                try {
                    for(int i = 0; i<columnMax ; i++) {
                        str[i] = dis.readUTF();
                    }


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for(int i = 0; i<columnMax ; i++) {
                                tv1.append(type[i]+" : " + str[i] + "\n");
                            }
                            tv1.append("\n\n정보가 맞으시면 저장하기를 눌러주세요!");
                        }
                    });

                } catch (IOException e) {
                    Log.d("ppppp","서버와 연결이 끊켰습니다.");
                    //Toast.makeText(getApplicationContext(),"서버와 연결이 끊켰습니다.",Toast.LENGTH_SHORT).show();
                }
                finally {
                    flag = false;
                }

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if(client!=null) {
                os.close();
                is.close();
                dos.close();
                dis.close();
                client.close();
                Log.d("Stop!!!!!!","in!!!!");
            }

            Log.d("Stop!!!!!!","out!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //연락처 부르는 메서드

    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST_WRITE_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            this.contactsInsert();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void contactsInsert()
    {

        // rawContact를 삽입
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.RawContacts.CONTACT_ID, 0);
        contentValues.put(ContactsContract.RawContacts.AGGREGATION_MODE, ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED);
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        long rawContactId = ContentUris.parseId(rawContactUri);

// 전화번호
        contentValues.clear();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        //contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "연락처번호");
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, str[6]);
        Uri dataUri = getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contentValues);

// 이름
        contentValues.clear();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        //contentValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,"연락처이름");
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,str[0]);
        dataUri = getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contentValues);

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

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE IF NOT EXISTS king(_id INTEGER PRIMARY KEY AUTOINCREMENT,addr TEXT, position TEXT, email TEXT, fax TEXT, call TEXT, memo TEXT)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
