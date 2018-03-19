package com.example.songzeceng.studyoflivedata;

import android.arch.core.util.Function;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

/**
 * Created by songzeceng on 2018/3/16.
 */

public class MyModel extends ViewModel {
    private MutableLiveData<String> name;
    private MutableLiveData<String> name2;

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

    public MutableLiveData<String> getName2(){
        return name2;
    }

    public void setName2(MutableLiveData<String> name2) {
        this.name2 = name2;
    }
}
