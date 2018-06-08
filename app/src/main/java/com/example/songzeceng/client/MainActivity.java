package com.example.songzeceng.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.util.Log;

import com.example.songzeceng.studyofipc.IPersonManagerInterface;
import com.example.songzeceng.studyofipc.MessengerService;
import com.example.songzeceng.studyofipc.PeopleService;
import com.example.songzeceng.studyofipc.Person;
import com.example.songzeceng.studyofipc.PersonProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";
    private IPersonManagerInterface personManager = null;

    private boolean isConnected = false;
    private Person p = new Person("Dustin", 27);

    private Messenger serviceMessenger;
    private Messenger clientMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bundle content = (Bundle) msg.obj;
                    String info = content.getString("info", "nothing");
                    logger(info);
                    break;
            }
        }
    });

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            if (studyOfAidl(name, service)) return;

//            studyOfMessenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected = false;
        }
    };

    private void insertData(Uri providerUrl, int id, String name, String description) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("description", description);
        getContentResolver().insert(providerUrl, values);
    }

    private void updateData(Uri providerUrl, int id, String name, String description) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("description", description);
        getContentResolver().update(providerUrl, values, "id = " + id, null);
    }

    private void deleteData(Uri providerUrl, int id) {
        getContentResolver().delete(providerUrl, "id = " + id, null);
    }

    private void queryData(Uri providerUrl) {
        Cursor cursor = getContentResolver().query(providerUrl, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String userName = cursor.getString(cursor.getColumnIndex("name"));
            String description = cursor.getString(cursor.getColumnIndex("description"));

            System.out.println(id + "--" + userName + "--" + description);
        }

        cursor.close();
        System.out.println("----------------------------------");
    }

    private void studyOfMessenger(IBinder service) {
        try {
            System.out.println(service.getClass().getCanonicalName());
            serviceMessenger = new Messenger(service);
            Field[] fields = serviceMessenger.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                System.out.println(field.get(serviceMessenger).getClass().getCanonicalName());
            }
            Message msg = Message.obtain();
            msg.what = 0;
            // msg.obj = "客户端来信，pid:" + Process.myPid(); // IPC时传递的数据类型必须实现Parcelable接口，然而String没有实现
            Bundle bundle = new Bundle();
            bundle.putString("info", "客户端来信，pid:" + Process.myPid());
            msg.obj = bundle;
            msg.replyTo = clientMessenger;
            serviceMessenger.send(msg);

            isConnected = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean studyOfAidl(ComponentName name, IBinder service) {
        logger(name.toString()); // BinderProxy
        isConnected = true;
        if (service == null) {
            logger("service is null");
            return true;
        }

        try {
            logger(service.getClass().getCanonicalName());
            if (personManager == null) {
                personManager = IPersonManagerInterface.Stub.asInterface(service);
                // Stub.proxy
                logger(personManager.getClass().getCanonicalName());
            }
            if (personManager != null) {

                logger("before add");
                logger(p.toString());
                logger("=================");
                personManager.addPerson(p);
                logger(p.toString());
                logger("=================");

                ArrayList<Person> people = (ArrayList<Person>) personManager.getPeople();
                // proxy返回的是ArrayList
                logger(people.toString());
                logger("=================");

                personManager.updatePerson(p);
                // out和inout似乎并不起作用
                logger(p.toString());
                logger("=================");

                personManager.updatePerson2(p);
                logger(p.toString());
                logger("=================");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void logger(String s) {
        Log.i(TAG, s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Uri providerUrl = PersonProvider.PERSON_URI;

            insertData(providerUrl, 1, "szc", "a simple boy");
            insertData(providerUrl, 2, "jason", "an interesting boy");

            queryData(providerUrl);

            updateData(providerUrl, 2, "dustin", "a brave boy");
            queryData(providerUrl);

            deleteData(providerUrl, 2);
            queryData(providerUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnected) {
//            Intent intent = new Intent(this, PeopleService.class);
//            intent.setAction("com.example.songzeceng");
//            Intent intent = new Intent(this, MessengerService.class);
//            intent.setAction("com.example.songzeceng.Messenger");
//            bindService(intent, connection, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isConnected) {
            unbindService(connection);
            isConnected = false;
        }
    }
}
