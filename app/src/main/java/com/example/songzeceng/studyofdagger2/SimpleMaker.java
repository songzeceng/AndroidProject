package com.example.songzeceng.studyofdagger2;

import com.example.songzeceng.studyofdagger2.Interfaces.A;
import com.example.songzeceng.studyofdagger2.Interfaces.CoffeeMaker;

/**
 * Created by songzeceng on 2018/3/22.
 */

public class SimpleMaker implements CoffeeMaker {
    Cooker cooker;

    public SimpleMaker(@A Cooker cooker) {
        this.cooker = cooker;
    }

    @Override
    public void makeCoffee() {
        cooker.makeCoffee();
    }
}
