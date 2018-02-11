package com.example.songzeceng.studyofretrofit.rxBus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import rx.subjects.SerializedSubject;

/**
 * Created by songzeceng on 2018/2/6.
 */

public class MyRxBus {
    /*
    Subject类：既是被观察者，也是观察者
    被观察者方法：subscribe().里面传一个Action匿名对象，在call里面实现事件结果的处理
              ofType().里面传class对象，表示只接收这一类的事件。也就是广播过滤器
    观察者方法：onNext().里面传一个Object对象，传递的就是事件。

    PublishSubject:可充当广播
    SerializedSubject:线程安全
     */
    private Subject bus = null;
    private static volatile MyRxBus instance = null;

    private MyRxBus(){
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static MyRxBus getInstance(){
        if(instance == null){
            synchronized (MyRxBus.class){
                if (instance == null){
                    instance = new MyRxBus();
                }
            }
        }

        return instance;
    }

    public void post(Object object){
        bus.onNext(object);
    }

    public <T> Observable<T> tObservable(Class<T> classType){
        return bus.ofType(classType);
    }
}
