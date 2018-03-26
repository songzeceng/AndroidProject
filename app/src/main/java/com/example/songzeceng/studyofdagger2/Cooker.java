package com.example.songzeceng.studyofdagger2;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by songzeceng on 2018/3/22.
 */

public class Cooker {
    private String name;
    private String coffee;

    public Cooker(String name, String coffee) {
        this.name = name;
        this.coffee = coffee;
    }

    public void makeCoffee(){
        Log.i("Cooker",name+" makes "+coffee);
    }
}
