package com.example.songzeceng.myndkdemo.model;

/**
 * Created by songzeceng on 2018/5/21.
 */

public class Man extends Person {
    public Man() {
        super();
    }

    public Man(String name, int age) {
        super(name, age);
    }

    @Override
    public String toString() {
        return "Man`s toString()";
    }
}
