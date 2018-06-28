package com.example.songzeceng.studyofretrofit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by songzeceng on 2018/6/28.
 */

public class MyTest {
    @Test
    public void myTest() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.addAll(Arrays.asList(1,3,2,7,4));
        System.out.println(arrayList);
    }
}
