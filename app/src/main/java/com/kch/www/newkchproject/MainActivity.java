package com.kch.www.newkchproject;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kch.www.newkchproject.Adapter.AddrAdapter;
import com.kch.www.newkchproject.Adapter.ListAdapter;
import com.kch.www.newkchproject.Adapter.Right1Adapter;
import com.kch.www.newkchproject.DataSet.ArraylistSen;
import com.kch.www.newkchproject.DataSet.MoveListDataSet;
import com.kch.www.newkchproject.DataSet.Right1DataSet;
import com.kch.www.newkchproject.DataSet.UserlistDataset;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * autor : kyung chang hyun
 * date	 :2017.06.16
 * comment: Home
 * memo : 1. 연락처 불러오기 완료
 *        2. SMS 완료
 *        3. 다량의 연락처로 엑티비티 실행불가 문제
 **/

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    BroadcastReceiver recv;

    Button mainBtn;

    MainRight1Fragment right1Fragment;
    MainRight2Fragment right2Fragment;

    ListView list;
    ArrayList<UserlistDataset> data;
    ListAdapter listAdapter;


    ArrayList<Map<String,Object>> addrData;

    ArrayList<MoveListDataSet> moveList;

    ArrayList<Right1DataSet> rightData;
    Right1Adapter rAdapter;
    ListView rList;
    String message;




    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //브로드케스트 리시버

        PermissionRequester.Builder requester = new PermissionRequester.Builder(this);
        requester.create().request(Manifest.permission.RECEIVE_SMS, 10000, new PermissionRequester.OnClickDenyButtonListener() {
            @Override
            public void onClick(Activity activity) {
                Toast.makeText(MainActivity.this, "권한을 얻지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });


        IntentFilter intentf = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentf.addAction("android.provider.Telephony.SMS_RECEIVED");

        recv = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d("recv : ", "Start!!!!!!!!!!!!!!!!!");

                if (intent.getAction().equals(
                        "android.provider.Telephony.SMS_RECEIVED")) {
                    StringBuilder sms = new StringBuilder();    // SMS문자를 저장할 곳
                    Bundle bundle = intent.getExtras();         // Bundle객체에 문자를 받아온다

                    if (bundle != null) {
                        // 번들에 포함된 문자 데이터를 객체 배열로 받아온다
                        Object[] pdusObj = (Object[]) bundle.get("pdus");

                        // SMS를 받아올 SmsMessage 배열을 만든다
                        SmsMessage[] messages = new SmsMessage[pdusObj.length];
                        for (int i = 0; i < pdusObj.length; i++) {
                            messages[i] =
                                    SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                            // SmsMessage의 static메서드인 createFromPdu로 pdusObj의
                            // 데이터를 message에 담는다
                            // 이 때 pdusObj는 byte배열로 형변환을 해줘야 함
                        }

                        // SmsMessage배열에 담긴 데이터를 append메서드로 sms에 저장
                        for (SmsMessage smsMessage : messages) {
                            // getMessageBody메서드는 문자 본문을 받아오는 메서드
                            sms.append(smsMessage.getMessageBody());
                        }

                        message = sms.toString(); // StringBuilder 객체 sms를 String으로 변환
                        //시간
                        // 현재시간을 msec 으로 구한다.
                        long now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd HH:mm:ss");
                        // nowDate 변수에 값을 저장한다.
                        String formatDate = sdfNow.format(date);

                        //메시지 list에 추가
                        rightData.add(new Right1DataSet(formatDate,message));
                        rAdapter.notifyDataSetChanged();

                        Log.d("recv : ", "gggg");
                        Toast.makeText(context, "문자 수신 Data : " + sms.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }


        };

        registerReceiver(recv, intentf);

        //Rightlist

        rightData = new ArrayList<>();

        rAdapter = new Right1Adapter(rightData);

        rList = (ListView)findViewById(R.id.rlist1) ;
        rList.setAdapter(rAdapter);


        //
        list = (ListView)findViewById(R.id.mainList);

        moveList = new ArrayList<>();

        addrData = getContactNames();
        AddrAdapter adapter = new AddrAdapter(this, R.layout.userlist_item, addrData);
        list.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        right1Fragment = new MainRight1Fragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(recv);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(this, SubActivity.class);

            //i.putParcelableArrayListExtra("array",moveList);

            startActivity(i);
        } else if (id == R.id.nav_slideshow) {
            Intent in = new Intent(this, ThActivity.class);

            //in.putParcelableArrayListExtra("array",moveList);

            startActivity(in);

        } else if (id == R.id.nav_manage) {
            Intent bc = new Intent(this, BusinessCardActivity.class);
            startActivity(bc);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    /**
     * autor : kyung chang hyun
     * data	 :2017.06.15
     * comment: 연락처DB에서 불러오기
     * memo : 추후 진행상황 각 엑티비티마다 연락처를 불러올것
     **/

    /**
     * Read the name of all the contacts.
     *
     * @return a list of names.
     */
    private ArrayList<Map<String,Object>> getContactNames() {
        HashMap<String,Object> map = new HashMap<>();
        ArrayList<Map<String,Object>> contacts = new ArrayList<>();


        // Get the ContentResolver
        ContentResolver cr = getContentResolver();
        // Get the Cursor of all the contacts
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        int ididx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int nameidx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

        // Move the cursor to first. Also check whether the cursor is empty or not.
        if (cursor.moveToFirst()) {
            // Iterate through the cursor
            do {
                map = new HashMap<>();
                // Get the contacts name
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                map.put("name",name);

                // Get the contacts number
                String id = cursor.getString(ididx);
                Cursor cursor2 = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id}, null);

                cursor2.moveToFirst();

                if( cursor2 != null && cursor2.moveToFirst() ) {


                    int numidx = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                    int imgidx = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);

                    String number = cursor2.getString(numidx);
                    map.put("number", number);

                    Log.d("Map Data : ", "" + name + " , " + number);

                    //전화번호를 통해 Photo를 받아옴

                    Bitmap img = getFacebookPhoto(number);
                    map.put("img", img);

                    contacts.add(map);

                    moveList.add(new MoveListDataSet(name, number, img));


                }
            } while (cursor.moveToNext());
        }
        // Close the curosor
        cursor.close();

        Log.d("Contacts : ",""+contacts.get(0).get("name")+" , "+contacts.get(1).get("name")+" , "+contacts.get(2).get("name"));

        return contacts;
    }

    public Bitmap getFacebookPhoto(String phoneNumber){
        Uri phoneUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Uri photoUri = null;
        ContentResolver cr = this.getContentResolver();
        Cursor contact = cr.query(phoneUri,
                new String[] { ContactsContract.Contacts._ID }, null, null, null);
        if (contact.moveToFirst()) {
            long userId = contact.getLong(contact.getColumnIndex(ContactsContract.Contacts._ID));
            photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, userId);
        }
        else {
            Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_report_image);
            return defaultPhoto;
        }
        if (photoUri != null) {
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(
                    cr, photoUri);
            if (input != null) {
                return BitmapFactory.decodeStream(input);
            }
        } else {
            Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_report_image);
            return defaultPhoto;
        }
        Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_report_image);
        return defaultPhoto;
    }


    @Override
    public void onClick(View v) {

    }
}
