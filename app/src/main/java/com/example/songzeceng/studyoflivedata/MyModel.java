package com.example.songzeceng.studyoflivedata;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.songzeceng.studyoflivedata.room.CRUDUtil;
import com.example.songzeceng.studyoflivedata.entity.User;
import com.example.songzeceng.studyoflivedata.entity.UserPerforms;
import com.example.songzeceng.studyoflivedata.entity.UserSimple;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by songzeceng on 2018/3/16.
 */

public class MyModel extends ViewModel {
    private MutableLiveData<String> name;
    private MutableLiveData<String> name2;

    public MutableLiveData<String> getName() {
        if (name == null) {
            synchronized (this) {
                if (name == null) {
                    name = new MutableLiveData<>();
                }
            }
        }

        return name;
    }

    public MutableLiveData<String> getName2() {
        return name2;
    }

    public void setName2(MutableLiveData<String> name2) {
        this.name2 = name2;
    }

    public void insert(User[] data, Context context) {
        CRUDUtil.getDao(context).insert(data);
    }

    public void insert(UserPerforms[] data, Context context) {
        CRUDUtil.getDao(context).insert(data);
    }

    public void update(User data, Context context) {
        CRUDUtil.getDao(context).updateName(data.getName(), data.getId());
    }

    public ArrayList<User> getAllUsers(Context context) {
        return (ArrayList<User>) CRUDUtil.getDao(context).getAllUsers();
    }

    public ArrayList<UserPerforms> getAllPerforms(Context context) {
        return (ArrayList<UserPerforms>) CRUDUtil.getDao(context).getAllPerforms();
    }

    public ArrayList<UserSimple> getUsersWithLimits(Context context, int score, int assist) {
        return (ArrayList<UserSimple>) CRUDUtil.getDao(context).getUserWithLimits(score, assist);
    }

    public Flowable<List<User>> getAllUsersFlowable(Context context) {
        return CRUDUtil.getDao(context).getAllUsersFlowable();
    }

    public void delete(User u, Context context) {
        CRUDUtil.getDao(context).delete(u);
    }

}
