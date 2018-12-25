package com.example.songzeceng.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
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
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    public static final int ANIMATOR_DURATION = 10 * 1000;
    private IPersonManagerInterface personManager = null;

    private boolean isConnected = false;
    private Person p = new Person("Dustin", 27);

    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private boolean isOver = false;
    private Socket server;
    private EditText et_input;
    private TextView tv_test;
    private ImageView image;
    private boolean hasPlayAnimations = false;

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

    private int count = 0;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            if (studyOfAidl(name, service)) return;

//            studyOfMessenger(service);
            System.out.println("service connected...");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected = false;
        }
    };
    private ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (!hasPlayAnimations) {
                studyOfAnimator();
                hasPlayAnimations = true;
            }
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
        image = findViewById(R.id.image);
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

        tv_test = findViewById(R.id.text_test);
        tv_test.setText("宋泽嶒");
        logger("textView尺寸：" + tv_test.getHeight() + "--" + tv_test.getWidth());
        ViewTreeObserver viewTreeObserver = tv_test.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(listener);

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
  //      tcpCommunication();

        logger("主线程名：" + Thread.currentThread().getName());
        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("name", "szc");
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
     //   mHandler.sendEmptyMessageDelayed(0, 5000);
    }

    private void studyOfAnimator() {
        float initX = image.getLeft();
        float initY = image.getTop();
        logger("初始位置：(" + initX + ", " + initY + ")");

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(image, View.X, initX, initX + 100, initX);
        animator1.setDuration(ANIMATOR_DURATION);
        animator1.setInterpolator(new SinInterpolator());
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(image, View.Y, initY, initY + 100, initY);
        animator2.setDuration(ANIMATOR_DURATION);
        animator2.setInterpolator(new BounceInterpolator());

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(image, View.ROTATION, 0, 360)
                .setDuration(ANIMATOR_DURATION);
        animator3.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animator4 = ObjectAnimator.ofFloat(image, View.SCALE_X, 1, 5, 1)
                .setDuration(ANIMATOR_DURATION);
        animator4.setInterpolator(new LinearInterpolator());

        ObjectAnimator animator5 = ObjectAnimator.ofFloat(image, View.SCALE_Y, 1, 5, 1)
                .setDuration(ANIMATOR_DURATION);
        animator5.setInterpolator(new LinearInterpolator());

        AnimatorSet animatorSet0 = new AnimatorSet();
        animatorSet0.playTogether(animator4, animator5);
        animatorSet0.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator1).with(animator2).before(animator3);
        animatorSet.setStartDelay(ANIMATOR_DURATION);
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator animator6 = ObjectAnimator.ofFloat(image, View.ALPHA, 1f, 0f, 1f).
                        setDuration(ANIMATOR_DURATION);
                animator6.setInterpolator(new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float input) {
                        return (float) Math.pow(input, 4);
                    }
                });
                animator6.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        MyToast.showToast(getApplicationContext(), " 动画结束");
                    }
                });
                animator6.start();
            }
        });
    }

    private void tcpCommunication() {
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
