package com.example.songzeceng.studyofdagger2.Interfaces;

import com.example.songzeceng.studyofdagger2.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by songzeceng on 2018/3/22.
 */

@perActivity
@Component(modules = SimpleModule.class)
public interface SimpleComponent {
    void inject(MainActivity activity);
}
