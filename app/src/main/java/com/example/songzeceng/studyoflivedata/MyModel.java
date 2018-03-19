package com.example.songzeceng.studyoflivedata;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by songzeceng on 2018/3/16.
 */

public class MyModel extends ViewModel {
    private MutableLiveData<String> name;

    public MutableLiveData<String> getName(){
        if(name ==null) {
            synchronized (this) {
                if(name == null){
                    name = new MutableLiveData<>();
                }
            }
        }

        return name;
    }
}
