package com.example.songzeceng.studyofretrofit;

import android.test.InstrumentationTestCase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by songzeceng on 2018/6/25.
 */

public class TestClass extends InstrumentationTestCase {
    public static final String TAG = "TestClass";

    public void testMethod() {
        List<String> letters = new LinkedList<>();
        letters.add("a");
        letters.add("b");
        letters.add("c");
        letters.add("d");
        letters.add("e");
        letters.add("f");

        //parallelStream():并行流
        letters.parallelStream().filter(s -> {
            Log.i(TAG, "filter:" + s + "--thread name:" + Thread.currentThread().getName());
            return true;
        }).map(s -> {
            Log.i(TAG, "map:" + s + "--thread name:" + Thread.currentThread().getName());
            return s.toUpperCase();
        }).forEach(s -> {
            Log.i(TAG, "forEach:" + s + "--thread name:" + Thread.currentThread().getName());
        });
        //D A C E B F
        //D B C E A F
        //D F E A B C
        //...随机运行

        letters.stream().forEach(s -> {
            Log.i(TAG, "forEach:" + s);
        });
    }
}
