package com.example.songzeceng.studyofretrofit.subject;

import java.util.LinkedList;

import rx.Observable;
import rx.Observer;
import rx.observers.SerializedObserver;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * Created by songzeceng on 2018/2/6.
 */

public class ReactiveList<T> {
    /*
        PublishSubject:广播subject，向所有接收者发送事件.事件不进入缓存区
        ReplaySubject:发送缓存区中最后几个事件
        BehaviorSubject:只发送缓存区中最后一个事件
     */
    public enum ChangeType {
        ADD, REMOVE, UPDATE
    }
    private LinkedList<T> list = new LinkedList<T>();
    private Subject<ChangeType,ChangeType> changes = null;//改变种类
    private Subject<T,T> changeValues = null;//改变的对象
    private Subject<T,T> latestChanged = null;//最近的改变对象

    private Observer<T> addObserver = null;
    private Observer<T> removeObserver = null;
    private Observer<T> updateObserver = null;

    private static ReactiveList instance = null;

    private ReactiveList(){
        changes = PublishSubject.<ChangeType>create().toSerialized();
        changeValues = ReplaySubject.<T>createWithSize(5).toSerialized();
        latestChanged = BehaviorSubject.<T>create().toSerialized();

        addObserver = new SerializedObserver<>(new Observer<T>() {
            @Override
            public void onCompleted() {
                changes.onCompleted();
                changeValues.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                changes.onError(e);
                changeValues.onError(e);
            }

            @Override
            public void onNext(T t) {
                add(t);
            }
        });

        removeObserver = new SerializedObserver<>(new Observer<T>() {
            @Override
            public void onCompleted() {
                changes.onCompleted();
                changeValues.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                changes.onError(e);
                changeValues.onError(e);
            }

            @Override
            public void onNext(T t) {
                delete(t);
            }
        });

        updateObserver = new SerializedObserver<>(new Observer<T>() {
            @Override
            public void onCompleted() {
                changes.onCompleted();
                changeValues.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                changes.onError(e);
                changeValues.onError(e);
            }

            @Override
            public void onNext(T t) {
                update(t);
            }
        });
    }

    public static ReactiveList getInstance(){
        if(instance == null){
            synchronized (ReactiveList.class){
                if(instance == null){
                    instance = new ReactiveList();
                }
            }
        }
        return instance;
    }

    public Subject<ChangeType,ChangeType> changes(){
        return changes;
    }

    public Subject<T,T> changesValues(){
        return changeValues;
    }

    public Observable<T> latestChanged(){
        return latestChanged;
    }

    public Observer<T> adder(){
        return addObserver;
    }

    public Observer<T> remover(){
        return removeObserver;
    }

    public Observer<T> updater(){
        return updateObserver;
    }

    private void add(T value){
        list.add(value);
        changes.onNext(ChangeType.ADD);
        changeValues.onNext(value);
        latestChanged.onNext(value);
    }

    private void delete(T value){
        if(list.contains(value)){
            list.remove(value);
            changes.onNext(ChangeType.REMOVE);
            changeValues.onNext(value);
            latestChanged.onNext(value);
        }
    }

    private void update(T value){
        if(list.contains(value)) {
            int index = list.indexOf(value);
            list.set(index,value);
            changes.onNext(ChangeType.UPDATE);
            changeValues.onNext(value);
            latestChanged.onNext(value);
        }
    }

    public Observable<T> list(){
        LinkedList<T> copy = new LinkedList<T>();
        synchronized (copy){
            copy.addAll(list);
        }
        return Observable.from(copy);
        //创建操作符，创建一个被观察者，就自动发送事件
    }

}
