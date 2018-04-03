package com.example.songzeceng.studyofdagger2.Interfaces;

import com.example.songzeceng.studyofdagger2.Cooker;
import com.example.songzeceng.studyofdagger2.SimpleMaker;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by songzeceng on 2018/3/26.
 */

@Module
@Singleton
public class AppModule {
    private String name;
    private String kind;

    public AppModule(String name, String kind) {
        this.name = name;
        this.kind = kind;
    }

    @Provides
    public SimpleMaker provideSimpleMaker(Cooker cooker){
        return new SimpleMaker(cooker);
    }

    @Provides
    public Cooker provideCooker(){
        return new Cooker(name,kind);
    }
}
