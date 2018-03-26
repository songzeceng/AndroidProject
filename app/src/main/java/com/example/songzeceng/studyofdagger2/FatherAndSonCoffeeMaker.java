package com.example.songzeceng.studyofdagger2;

import com.example.songzeceng.studyofdagger2.Interfaces.A;
import com.example.songzeceng.studyofdagger2.Interfaces.B;
import com.example.songzeceng.studyofdagger2.Interfaces.CoffeeMaker;

import javax.inject.Inject;

/**
 * Created by songzeceng on 2018/3/25.
 */

public class FatherAndSonCoffeeMaker implements CoffeeMaker{
    private Cooker father,son;

    public FatherAndSonCoffeeMaker(@A Cooker father,@B Cooker son) {
        this.father = father;
        this.son = son;
    }


    @Override
    public void makeCoffee() {
        father.makeCoffee();
        son.makeCoffee();
    }
}
