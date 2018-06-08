package com.example.songzeceng.studyofipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.example.songzeceng.studyofipc.IPersonManagerInterface.Stub;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by songzeceng on 2018/6/4.
 */

public class PeopleService extends Service {
    private static final String TAG = "PeopleService";
    private LinkedList<Person> peopleList = new LinkedList<>();
    private Random random = new Random();

    private boolean isOut = false;
    private boolean isOver = false;

    private final Stub peopleManager = new Stub() {

        @Override
        public List<Person> getPeople() throws RemoteException {
            return peopleList;
        }

        @Override
        public void addPerson(Person person) throws RemoteException {
            boolean isNull = person == null;
            logger("in person is null--" + isNull);
            person.setAge(person.getAge() + 1);
            peopleList.add(person);
        }

        @Override
        public Person updatePerson(Person person) throws RemoteException {
            boolean isNull = person == null;
            logger("out person is null--" + isNull);
            if (isNull) {
                person = new Person();
            } else {
                logger(person.toString());
            }
            person.setAge(random.nextInt() % 40);
            person.setName("jason");
            return person;
        }

        @Override
        public Person updatePerson2(Person person) throws RemoteException {
            boolean isNull = person == null;
            logger("inout person is null--" + isNull);
            if (isNull) {
                person = new Person();
            } else {
                logger(person.toString());
            }
            person.setAge(random.nextInt() % 40);
            person.setName("mike");
            return person;
        }
    };

    private void logger(String msg) {
        Log.i(TAG, msg);
    }

    @Override
    public void onCreate() {
//        Person p = new Person("szc", 21);
//        peopleList.add(p);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ServerSocket serverSocket = new ServerSocket(12345);
                    System.out.println("等待客户端连接");

                    while (!isOut) { // 服务端不退出
                        final Socket client = serverSocket.accept(); // 阻塞在此，直到客户端连接
                        System.out.println("客户端已连接，ip地址:" + client.getInetAddress().getHostAddress() + "端口号:" + client.getLocalPort());

                        // 客户端连接后，等待1s以便客户端发消息
                        Thread.sleep(1000);
                        try {
                            BufferedInputStream inputStream = new BufferedInputStream(client.getInputStream());
                            BufferedOutputStream outputStream = new BufferedOutputStream(client.getOutputStream());

                            System.out.println("开始和客户端通信");
                            while (!isOver) { // 消息不为over
                                while (inputStream.available() <= 0);

                                byte[] bytes = new byte[1024];
                                int len;
                                StringBuffer stringBuffer = new StringBuffer();

                                while (inputStream.available() > 0 && (len = inputStream.read(bytes)) != -1) {
                                    stringBuffer.append(new String(bytes, 0, len));
                                }

                                String fromClient = stringBuffer.toString();

                                System.out.println("客户端信息:" + fromClient);

                                outputStream.write(fromClient.getBytes());
                                outputStream.flush();

                                isOver = fromClient.equals("over");

                                isOut = isOver;
                            }

                            System.out.println("over..");
                            inputStream.close();
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        logger("有连接请求");
        logger(intent.toString());
//        return peopleManager;
        return null;
    }
}
