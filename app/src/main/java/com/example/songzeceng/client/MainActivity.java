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
import android.view.ViewTreeObserver;
import android.widget.EditText;
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
//            if (studyOfAidl(name, service)) return;

//            studyOfMessenger(service);
            System.out.println("service connected...");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected = false;
        }
    };
    private ViewTreeObserver.OnGlobalLayoutListener listener;

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

        final TextView tv_test = findViewById(R.id.text_test);
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
         * 如果不给一个view设置一个id，那么在Activity调用onSaveInstanceState(Bundle outState)方法时，
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
         * ActivityA 跳转到ActivityB，ActivityB按返回键回到A
         * onCreateA() -> onStartA() -> onResumeA() -> 启动B -> onPauseA() -> onCreateB()
         * -> onStartB() -> onResumeB() -> onStopA() -> B按返回键回到A -> onPauseB() -> onRetartA()
         * -> onStartA() -> onResumeA() -> onStopB() -> onDestroyB()
         *
         * 访问修饰符：
         *  protected:包内+子类
         *  默认：包内
         *
         * HandlerThread:
         *  本质是一个线程，但拥有自己的Looper
         *  Looper对象在run()方法里初始化
         *  外部的handler可以获取looper，从而在子线程里处理子线程的消息
         *  内部只有一个线程，所以是串行执行
         *
         * IntentService:
         *  本质是一个Service，但内部有一个HandlerThread子线程和Handler
         *  onStart()方法把意图封装给Message，发送给Handler
         *  handler处理消息，执行onHandleIntent方法，然后调用quitSelf()，传入自己的id，结束这个IntentService
         *  故而IntentService是在子线程里处理结果的，而且是串行执行，可以和BroadCastReceiver结合，更改UI
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
         *  栈内存，堆内存，常量区，方法区
         *
         *  栈：存放基本数据类型和对象的引用
         *  堆：存放new出来的对象，栈里面的引用可以作为GCRoot
         *
         *  GCRoot:栈中的对象引用、常量或静态变量的对象引用、jni中的对象引用(如Bitmap)
         *
         *  引用计数：引用一次计数+1，引用销毁，计数-1。
         *  可达性分析：从GCRoot往下遍历，判断一个对象是不是在GCRoot的引用链中
         *      如果不在GCRoot的引用链中，进行两次标记
         *      两次标记：如果对象没有覆写finalize()方法或者已经执行过，直接回收；否则标记之，放入F-Queue队列中，由Finalizer线程处理
         *      处理F-Queue队列时，如果对象在finalize()方法中关联上了GCRoot，就标记为不回收；否则回收之
         *
         * 内存泄漏：
         *  原因：要被销毁的对象还被持有引用，导致不能被销毁
         *  情况：
         *      1、内部handler、以及handler中的耗时操作。
         *          当我们要销毁一个activity时，handler还有消息没有发送，这时message持有handler的引用，handler持有Activity的引用，导致Activity不能被销毁
         *          或者handler在处理消息时，用了一个处理无限循环的子线程，导致子线程不能结束，同样Activity不能被销毁
         *
         *          方法：1、对于无限循环的子线程，可以通过标志位控制循环的结束和进行，在onDestroy()时改变标志位的值，就可以结束无限循环
         *                2、 对于内部handler，一种是在onDestroy()时，调用handler.removeAllMessagesAndCallbacks(null)清空未发送的消息
         *                3、另一种是让handler持有activity的弱引用
         *
         *      2、 单例、静态变量或属性
         *          当我们给一个静态变量传context时，由于静态变量生命周期是应用的生命周期，所以也不能及时释放activity
         *
         *          方法：传入Application的context
         *
         *      3、游标的释放、bitmap.recycle()、监听的取消订阅
         *
         *      4、异步任务：在onPause()里取消任务，或者持有activity的弱引用
         */
        logger("主线程名：" + Thread.currentThread().getName());
        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("name", "szc");
        startService(intent);

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
