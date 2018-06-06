package com.example.songzeceng.studyofipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.songzeceng.studyofipc.IPersonManagerInterface.Stub;

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
        Person p = new Person("szc", 21);
        peopleList.add(p);
    }

    @Override
    public IBinder onBind(Intent intent) {
        logger("有连接请求");
        logger(intent.toString());
        return peopleManager;
    }
}
