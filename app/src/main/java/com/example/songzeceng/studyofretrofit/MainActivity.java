package com.example.songzeceng.studyofretrofit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.songzeceng.studyofretrofit.recyclerView.AdapterForRecyclerVIew;
import com.example.songzeceng.studyofretrofit.rxBus.MyRxBus;
import com.example.songzeceng.studyofretrofit.rxBus.Student;
import com.example.songzeceng.studyofretrofit.subject.ReactiveList;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.schedulers.Schedulers;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends Activity {
    private final static String TAG = "MainActivity";
    public static final String BASE_URL = "http://fy.iciba.com/";
    public static final int RETRY_DELAY = 1000;

    private Subscription subscription = null;
    private ReactiveList<Student> reactiveList = null;

    @BindView(R.id.tv_show) TextView tv_show;
    @BindView(R.id.et_input) EditText et_input;
    @BindView(R.id.rv_recycler) RecyclerView recyclerView;
    private ProgressDialog dialog = null;

    private AdapterForRecyclerVIew adapter = null;

    private String input = "";
    private String inputs[] = new String[2];
    private int tryCount = 0;
    private int clickCount = 0;

    private int index = 0;
    private String[] names = {"A","B","C","D","E"
                            ,"F","G","H","I","J"
                            ,"K","L","M","N","O"
                            ,"P","R","S","T","U"};
    private Student[] students = new Student[]{
            new Student("001","杰森伯恩"),
            new Student("002","托马斯穆勒"),
            new Student("003","莱万多夫斯基"),
            new Student("004","阿尔杰罗本"),
            new Student("005","弗兰克兰帕德"),
            new Student("006","史蒂文杰拉德"),
            new Student("007","诺伊尔"),
            new Student("008","罗伯特卡洛斯"),
            new Student("009","因扎吉"),
            new Student("010","里奥费迪南德")
    };

    private LinkedList<String> urls = new LinkedList<>();
    private LinkedList<String> descriptions = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

//        usingIntervalRange();
//
//        simpleUseOfSubject();

//        useOfThreeKindsOfSubjects();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new AdapterForRecyclerVIew(urls,descriptions,this);

        urls.add("http://n.sinaimg.cn/sports/transform/20170216/1s3V-fyarzzv2801842.jpg");
        urls.add("http://www.zq1.com/Upload/20170415/235459msc9i5yiqracirfw.jpg");
        urls.add("https://c1.hoopchina.com.cn/uploads/star/event/images/171106/a694dde65a88b5f820c02c03d62d76caf02aeb09.jpg");
        urls.add("http://k.sinaimg.cn/n/sports/transform/20160424/dfCS-fxrqhar9877773.JPG/w570fe9.jpg");
        urls.add("http://n.sinaimg.cn/sports/transform/20170423/L9Uj-fyeqcac1387497.jpg");
        descriptions.add("南部之星拜仁慕尼黑");
        descriptions.add("骄傲的大黄蜂多特蒙德");
        descriptions.add("蓝月亮曼城");
        descriptions.add("红魔曼联");
        descriptions.add("蓝军切尔西");

        recyclerView.setAdapter(adapter);

    }

    @OnClick(R.id.btn_confirm)
    public void translate(Button button){
        Log.i("onClickListener", "onClick");

        input = et_input.getText().toString();
        input = input.replaceAll(" ", "%20") + ".";
        inputs[clickCount] = input;

        if (clickCount == 1) {
            createDialog();

            //requestJustByRetrofit();

            //requestByRetrofitAndRxJava();

            requestByRetrofitAndRxJavaUsingZip();

            clickCount = 0;
        } else {
            clickCount++;
        }
        et_input.setText("");
    }

    private void useOfThreeKindsOfSubjects() {
        reactiveList = ReactiveList.getInstance();

        /**
         * 这种情况：先发事件，再订阅
         * 对于Replay和Behavior，由于发事件的时候没有订阅者，事件全部进入缓存区。
         * 而对于PublishSubject，它的事件由于不存入缓存区，所以都流失了。
         * 最终结果：Replay和Behavior正常，Publish没有输出任何数据
         *
         * 此处，订阅者和subject就是观察者，我们(用户)、observer和subject则是被观察者，
         * 因为我们调用了观察者observer的onNext()方法，observer调用了subject的onNext()方法，subject调用了订阅者的call()方法
         *
         * 所以这个案例的观察链就是：
         *            用户->observer->subject->订阅者
         *
         */
//        dataOperation();

        reactiveList.changes().subscribe(new Action1<ReactiveList.ChangeType>() {
            @Override
            public void call(ReactiveList.ChangeType changeType) {
                Log.i(TAG,"changeType:"+changeType);
            }
        });


        /**
         * 这种情况：先订阅Publish，再发数据，最后订阅Replay和Behavior:
         * 由于发事件的时候，对于Replay和Behavior，情况和第一种一样，所以依旧正常
         * 而对于Publish，由于是先订阅，所以数据发一次，订阅者就接收一次，故而正常输出
         */
        dataOperation();


        reactiveList.changesValues().subscribe(new Action1<Student>() {
            @Override
            public void call(Student student) {
                Log.i(TAG,"changesValues:"+student.toString());
            }
        });

        reactiveList.latestChanged().subscribe(new Action1<Student>() {
            @Override
            public void call(Student student) {
                Log.i(TAG,"lastedChanged:"+student.toString());
            }
        });

        /**
         * 这种情况：先订阅，再发事件
         * 对于Replay,Behavior和Publish，由于发来一个事件就直接有订阅者接收，就不存在缓冲区的问题了
         * 所以结果是发一次事件，三个都按接收顺序各输出一次
         */
//        dataOperation();
    }

    private void dataOperation() {
        for (int i=0;i<students.length;i++){
            reactiveList.adder().onNext(students[i]);
        }

        reactiveList.list().subscribe(new Action1<Student>() {
            @Override
            public void call(Student student) {
                Log.i(TAG,"list:"+student.toString());
            }
        });

        students[5].setName("卡尼吉亚");
        reactiveList.updater().onNext(students[5]);

        reactiveList.remover().onNext(students[0]);
    }

    private void simpleUseOfSubject() {
        subscription = MyRxBus.getInstance().tObservable(Student.class).subscribe(new Action1<Student>() {
            @Override
            public void call(Student student) {
                Log.i(TAG,student.toString());
            }
        });
    }

    private void usingIntervalRange() {
        Observable<Long> observable = Observable.intervalRange(0,20,1,1, TimeUnit.SECONDS);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object value) {
                index = ((Long)value).intValue() % 4;
                Log.i(TAG,"index:"+index+"--value:"+value);
                MyRxBus.getInstance().post(new Student(""+value,names[((Long) value).intValue()]));
            }

            @Override
            public void onComplete() {
                Log.i(TAG,"event complete");
            }
        });
    }

    private void requestByRetrofitAndRxJavaUsingZip() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        IRequest iRequest = retrofit.create(IRequest.class);
        Observable<Result> observable1 = iRequest.getResultInRxJava("fy", "auto", "auto", inputs[0]).subscribeOn(Schedulers.io());
        Observable<Result> observable2 = iRequest.getResultInRxJava("fy", "auto", "auto", inputs[1]).subscribeOn(Schedulers.io());
        Observable.zip(observable1, observable2, new BiFunction<Result, Result, String>() {
            //zip():按顺序合并两个observable的请求，一起显示结果
            @Override
            public String apply(Result result, Result result2) throws Exception {
                //合并两个observable的结果。合并的结果返回到onNext()中去
                return result.toString() + "\n" + result2.toString();
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof IOException) {
                            Log.i(TAG, "IO异常，次数:" + (tryCount + 1));
                            if (tryCount < 3) {
                                tryCount++;
                                return Observable.just(1).delay(RETRY_DELAY, TimeUnit.MILLISECONDS);
                            } else {
                                return Observable.error(new Throwable("连接次数过多"));
                            }
                        } else {
                            return Observable.error(new Throwable("连接出错"));
                        }
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                Log.i(TAG, "连接成功");
                tv_show.setText(value);
                dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "出错：" + e.toString());
                dismissDialog();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void requestByRetrofitAndRxJava() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        IRequest request = retrofit.create(IRequest.class);
        Observable<Result> observable = request.getResultInRxJava("fy", "auto", "auto", "" + input);
        observable.retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                                 //tryWhen:出错时调用里面的apply方法
                                 @Override
                                 public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {

                                     return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                                         //flatMap:将错误obserable转换为异常throwable
                                         @Override
                                         public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                             //把throwable(出错的起源)转换为observable(retryWhen的对象).
                                             if (throwable instanceof IOException) {
                                                 Log.i(TAG, "io异常");
                                                 if (tryCount < 3) {
                                                     tryCount++;
                                                     return Observable.just(1).delay(RETRY_DELAY, TimeUnit.MILLISECONDS);
                                                 } else {
                                                     return Observable.error(new Throwable("连接重试次数过多"));
                                                 }
                                             } else {
                                                 return Observable.error(new Throwable("连接出错"));
                                             }
                                         }
                                     });
                                 }
                             }


        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result value) {
                        Log.i(TAG, "发送成功");
                        tv_show.setText(value.toString());
                        dismissDialog();
                        tryCount = 0;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "发送失败：" + e.toString());
                        dismissDialog();
                        tryCount = 0;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void requestJustByRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        final IRequest request = retrofit.create(IRequest.class);
        Call<Result> call = request.getResult("fy", "auto", "auto", "" + input);
        call.enqueue(new Callback<Result>() {
            //enqueue():异步
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                int code = response.code();
                if (code == 200) {
                    String result = response.body().toString();
                    tv_show.setText(result.substring(0, result.length() - 1));
                } else {
                    Log.i(TAG, "responseCode=" + code);
                }
                dismissDialog();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                t.printStackTrace();
                dismissDialog();
            }
        });
        //execute():同步
    }

    private void createDialog() {
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("翻译中");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
    }

    private void dismissDialog() {
        dialog.dismiss();
        dialog = null;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "MainActivity----dispatchTouchEvent:" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "MainActivity----onTouchEvent:" + event.getAction());
        return false;
        /*
         */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
        subscription = null;
    }
}
