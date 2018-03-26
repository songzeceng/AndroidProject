package com.example.songzeceng.studyofdagger2.Interfaces;

import com.example.songzeceng.studyofdagger2.Cooker;
import com.example.songzeceng.studyofdagger2.FatherAndSonCoffeeMaker;
import com.example.songzeceng.studyofdagger2.SimpleMaker;

import dagger.Module;
import dagger.Provides;

/**
 * Created by songzeceng on 2018/3/22.
 */

@Module
public class SimpleModule {
    private String name;
    private String kind;

    public SimpleModule(String name, String kind) {
        this.name = name;
        this.kind = kind;
    }

    public SimpleModule() {}

    @Provides
    @A
    Cooker provideCooker() {
        return new Cooker(name, kind);
    }

    @Provides
    @B
    Cooker provideAnotherCooker() {
        return new Cooker(name + "`s son", kind);
    }

    @Provides
    @A
    CoffeeMaker provideCoffeeMaker(@A Cooker cooker) {
        return new SimpleMaker(cooker);
    }

    @Provides
    @B
    @perActivity
    CoffeeMaker provideFDSCoffeeMaker(@A Cooker father, @B Cooker son) {
        return new FatherAndSonCoffeeMaker(father, son);
    }

}
