package com.example.songzeceng.studyofdagger2.Interfaces;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by songzeceng on 2018/3/26.
 */

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {
    void inject(Application application);
}
