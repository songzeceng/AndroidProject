package com.example.songzeceng.client;

import android.app.Activity;
import android.content.ComponentName;
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
import android.view.View;
import android.widget.EditText;

import com.example.songzeceng.studyofipc.IPersonManagerInterface;
import com.example.songzeceng.studyofipc.PeopleService;
import com.example.songzeceng.studyofipc.Person;
import com.example.songzeceng.studyofipc.PersonProvider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";
    private IPersonManagerInterface personManager = null;

    private boolean isConnected = false;
    private Person p = new Person("Dustin", 27);

    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private boolean isOver = false;
    private Socket server;
    private EditText et_input;

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
            if (studyOfAidl(name, service)) return;

//            studyOfMessenger(service);
            System.out.println("service connected...");
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
        logger(name.toString());
        isConnected = true;
        if (service == null) {
            logger("service is null");
            return true;
        }

        try {
            logger(service.getClass().getCanonicalName());  // BinderProxy
            if (personManager == null) {
                personManager = IPersonManagerInterface.Stub.asInterface(service);
                // 客户端的是Stub.proxy，服务端的是Stub
                logger("客户端peopleManager类型：" + personManager.getClass().getCanonicalName());
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

        et_input = (EditText) findViewById(R.id.client_input);
        findViewById(R.id.client_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            outputStream = new BufferedOutputStream(server.getOutputStream());

                            String toServer = et_input.getText().toString();

                            outputStream.write(toServer.getBytes());
                            outputStream.flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

//        useOfContentProvider();
    }

    private void useOfContentProvider() {
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
            final Intent intent = new Intent(this, PeopleService.class);
            intent.setAction("com.example.songzeceng");
//            Intent intent = new Intent(this, MessengerService.class);
//            intent.setAction("com.example.songzeceng.Messenger");
            bindService(intent, connection, BIND_AUTO_CREATE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 等待1s，以便服务端创建socket
                    try {
                        Thread.sleep(1000);
                        server = new Socket("localhost", 12345);

                        inputStream = new BufferedInputStream(server.getInputStream());
                        while (!isOver) {
                            while (inputStream.available() <= 0);

                            byte[] bytes = new byte[1024];
                            int len;
                            StringBuffer stringBuffer = new StringBuffer();
                            while (inputStream.available() > 0 && (len = inputStream.read(bytes)) != -1) {
                                stringBuffer.append(new String(bytes, 0, len));
                            }
                            String fromServer = stringBuffer.toString();
                            System.out.println(fromServer);
                            isOver = fromServer.equals("over");
                        }
                        System.out.println("over..");
                        inputStream.close();
                        outputStream.close();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        /**
         * Activity四种启动模式
         *
         * Standard: 收到新的Intent的时候，创建新的实例，新的实例和发送intent的activity在同一个任务栈中
         * SingleTop: 收到新的Intent，如果这个activity在当前的任务栈顶，就复用，否则创建新的实例，和发送意图的activity在同一个任务栈
         * SingleTask: 收到新的intent，如果此activity在当期的任务栈顶，不创建新的实例直接启动，
         *                            否则将在其上面的activity全部弹出销毁，保证任务栈中只有自己的一个实例。
         * SingleInstance: 始终保持一个实例，独占一个任务栈
         *
         * 屏幕旋转：
         *
         * 不设置android:configChanges:切横屏调用一次生命周期，竖屏调用两次
         * android:configChanges = orientation:切横竖屏各一次
         * android:configChanges = orientation|hiddenKeyBoard:不会调用生命周期，只会执行onConfigurationChanged()方法
         *
         * Vector：线程安全 2
         *
         * ArrayList：线程不安全 1.5
         *
         * HashMap:2，允许null键值对(位置0)，线程不安全
         *
         * HashTable:2n + 1，不允许null键值对，线程安全，效率高
         *
         * view设置id和不设置id的区别：
         * 如果不给一个view设置一个id，那么在Activity调用onSaveInstantceState(Bundle outState)方法时，
         * 就没办法保存它的状态，而且即使它当前是焦点view，也没办法将其焦点状态记录在Bundle对象中，这会导致在需要取出Bundle状态对象时，出现问题。
         *
         * Fragment生命周期：
         * onAttach()
         * onCreate()
         * onCreateView()
         * onActivtyCreated()
         * onResume():Fragment可视
         * onPause()
         * onStop()
         * onDestroyView()：如果Fragment重新可视，回到onCreateView()
         * onDestroy()
         * onDetach()
         *
         * Http安全性：
         *  1、内容是明文 --》 ssl加密
         *  2、不验证通信方身份 --》 ssl证书
         *  3、不验证明文内容的完整性 -》 ssl摘要
         *
         * https密钥交换：
         *  1、网站花钱从CA买一个数字证书，包括一个公钥一个私钥，这两个是非对称加密的
         *  2、用户通过浏览器访问网站，网站服务器把公钥发给浏览器
         *  3、浏览器发现不是权威CA发放的证书，警告用户
         *  4、用户信任或本身就是权威CA的话，浏览器生成一个随机私钥，利用服务器发的公钥进行对称加密构成密文，发给服务器
         *  5、服务器收到密文后，利用服务器的私钥解密，得到浏览器的随机私钥。
         *
         * GC：
         *
         */

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isConnected) {
            unbindService(connection);
            isConnected = false;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
