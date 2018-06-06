package com.example.songzeceng.studyofipc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.LinkedList;

public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";
    private static boolean isConnected = false;

    private Person p = new Person("Dustin", 27);

    private IPersonManagerInterface.Stub peopleManager = null;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (peopleManager == null) {
                peopleManager = (IPersonManagerInterface.Stub) IPersonManagerInterface.Stub.asInterface(service);
            }

            isConnected = true;

            if (peopleManager != null) {
                try {
                    LinkedList<Person> people = (LinkedList<Person>) peopleManager.getPeople();
                    logger(people.toString());
                    logger("=================");

                    logger("before add");
                    logger(p.toString());
                    logger("=================");
                    peopleManager.addPerson(p);
                    logger(p.toString());
                    logger("=================");

                    peopleManager.updatePerson(p);
                    logger(p.toString());
                    logger("=================");

                    peopleManager.updatePerson2(p);
                    logger(p.toString());
                    logger("=================");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            logger(name + "已经断开连接");
            isConnected = false;
        }
    };

    private void logger(String info) {
        Log.i(TAG, info);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {
        super.onStop();
        tryDisconnectService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tryConnectService();
    }

    private void tryConnectService() {
        logger("try to connect service");
        if (!isConnected) {
            Intent intent = new Intent(this, PeopleService.class);
            intent.setAction("com.example.songzeceng");
            bindService(intent, connection, BIND_AUTO_CREATE);
        }
    }

    private void tryDisconnectService() {
        logger("try to disconnect service");
        if (isConnected) {
            unbindService(connection);
            isConnected = false;
        }
    }
}
