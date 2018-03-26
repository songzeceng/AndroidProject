package com.example.songzeceng.studyofdagger2;

import com.example.songzeceng.studyofdagger2.Interfaces.A;
import com.example.songzeceng.studyofdagger2.Interfaces.B;
import com.example.songzeceng.studyofdagger2.Interfaces.CoffeeMaker;

import javax.inject.Inject;

/**
 * Created by songzeceng on 2018/3/22.
 */

public class CoffeeMachine {
    private SimpleMaker simpleMaker;
    private FatherAndSonCoffeeMaker fatherAndSonCoffeeMaker;

    @Inject
    public CoffeeMachine(@A CoffeeMaker maker, @B CoffeeMaker maker2) {
        this.simpleMaker = (SimpleMaker) maker;
        this.fatherAndSonCoffeeMaker = (FatherAndSonCoffeeMaker) maker2;
    }

    public void makeCoffee1(){
        simpleMaker.makeCoffee();
    }

    public void makeCoffee2(){
        fatherAndSonCoffeeMaker.makeCoffee();
    }

    public FatherAndSonCoffeeMaker getFatherAndSonCoffeeMaker() {
        return fatherAndSonCoffeeMaker;
    }
}
