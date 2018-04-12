package com.example.songzeceng.studyofretrofit;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.songzeceng.studyofretrofit.recyclerView.AdapterForRecyclerVIew;
import com.example.songzeceng.studyofretrofit.rxBus.MyRxBus;
import com.example.songzeceng.studyofretrofit.rxBus.Student;
import com.example.songzeceng.studyofretrofit.subject.ReactiveList;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

public class MainActivity extends Activity {
    private final static String TAG = "MainActivity";
    public static final String BASE_URL = "http://fy.iciba.com/";
    public static final int RETRY_DELAY = 1000;
    public static final String SRC_FILE_PATH = "/storage/emulated/0/Download/download.jpg";

    private Subscription subscription = null;
    private ReactiveList<Student> reactiveList = null;

    @BindView(R.id.tv_show)
    TextView tv_show;
    @BindView(R.id.et_input)
    EditText et_input;
    @BindView(R.id.rv_recycler)
    RecyclerView recyclerView;
    private DialogFragment dialog = null;//尽量使用DialogFragment，以保持生命周期和activity一致

    private AdapterForRecyclerVIew adapter = null;

    private String input = "";
    private String inputs[] = new String[2];
    private int tryCount = 0;
    private int clickCount = 0;
    private int changeCount = 0;

    private int index = 0;
    private String[] names = {"A", "B", "C", "D", "E"
            , "F", "G", "H", "I", "J"
            , "K", "L", "M", "N", "O"
            , "P", "R", "S", "T", "U"};
    private Student[] students = new Student[]{
            new Student("001", "杰森伯恩"),
            new Student("002", "托马斯穆勒"),
            new Student("003", "莱万多夫斯基"),
            new Student("004", "阿尔杰罗本"),
            new Student("005", "弗兰克兰帕德"),
            new Student("006", "史蒂文杰拉德"),
            new Student("007", "诺伊尔"),
            new Student("008", "罗伯特卡洛斯"),
            new Student("009", "因扎吉"),
            new Student("010", "里奥费迪南德")
    };

    private LinkedList<String> urls = new LinkedList<>();
    private LinkedList<String> descriptions = new LinkedList<>();
    private LinkedList<Student> studentList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //高版本的手机需要手动赋予权限，并在onRequestPermissionsResult()方法中处理结果
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}
                    ,0);
        }else{
           // studyOfExecutor();
        }

//        usingIntervalRange();
//
//        simpleUseOfSubject();

//        useOfThreeKindsOfSubjects();

//        studyOfRecyclerView();

        //安卓7.0(SDK版本24)才支持lambda表达式
        //studyOfLambda();
      //  studyOfPallelStream();

        //correctWayToCreateThreadInAndroid();

        //使用FileProvider进行文件共享
        Uri imageUri = FileProvider.getUriForFile(this,"com.example.songzeceng.myFileProvider",new File(SRC_FILE_PATH));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,0);

    }

    private void correctWayToCreateThreadInAndroid() {
        int core_number = Runtime.getRuntime().availableProcessors();
        int keep_alive_time = 1;
        TimeUnit time_unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingDeque<>();
        //利用线程池构建线程。减少创建和销毁线程的系统开销
        ExecutorService executorService = new ThreadPoolExecutor(core_number, core_number * 2,
                keep_alive_time, time_unit, taskQueue,
                Executors.defaultThreadFactory());
        executorService.execute(() -> copyFile());
    }

    private void studyOfExecutor() {
        ExecutorService executor = Executors.newSingleThreadExecutor();//实例化一个单线程的executor
        executor.submit(() -> {
            copyFile();
        });

        executor = Executors.newFixedThreadPool(5);//我们也可以自己指定线程数目
        List<Callable<String>> callables = Arrays.asList(
                () -> "task1",
                () -> "task2",
                () -> "task3");
        try {
             executor.invokeAll(callables).stream().map((future)->{
                try {
                    return future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                return "";
            }).forEach((s) -> Log.i(TAG,s));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();//等待所有任务执行完，就关闭executor（其间不再接收新的任务submit）
        try {
            executor.awaitTermination(5,TimeUnit.SECONDS);//最多等待五秒，强制关闭所有任务
        } catch (InterruptedException e) {
            e.printStackTrace();
            executor.shutdownNow();//强制立刻关闭所有任务
        }
    }

    private void copyFile() {
        try {
            // /storage/emulated/0/Download/download.jpg
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(SRC_FILE_PATH)));
            File newFile = new File(getFilesDir().getPath()+"/new.jpg");
            //不要在系统根目录下直接写文件
            if(!newFile.exists()){
                newFile.createNewFile();
            }
            Log.i(TAG,newFile.getPath());
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newFile));

            byte[] bytes = new byte[1024];
            int len = 0;

            while((len = bufferedInputStream.read(bytes)) != -1){
                bufferedOutputStream.write(bytes,0,len);
                bufferedOutputStream.flush();
            }

            bufferedInputStream.close();
            bufferedOutputStream.close();

            Log.i(TAG,"copy finished...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void studyOfPallelStream(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //commonPool里最大并行线程数：系统CPU核数 - 1
            Log.i(TAG,"并行流最大线程数："+ ForkJoinPool.commonPool().getParallelism()+"\n线程池容量："+ForkJoinPool.commonPool().getPoolSize());//1 0
            Log.i(TAG,"可用CPU核数："+Runtime.getRuntime().availableProcessors());//2

            List<String> letters =  new LinkedList<>();
            letters.add("a");
            letters.add("b");
            letters.add("c");
            letters.add("d");
            letters.add("e");
            letters.add("f");

            //parallelStream():并行流
            letters.parallelStream().filter(s -> {
               Log.i(TAG,"filter:"+s+"--thread name:"+Thread.currentThread().getName());
               return true;
            }).map(s -> {
                Log.i(TAG,"map:"+s+"--thread name:"+Thread.currentThread().getName());
                return s.toUpperCase();
            }).forEach(s -> {
                Log.i(TAG,"forEach:"+s+"--thread name:"+Thread.currentThread().getName());
            });
            //D A C E B F
            //D B C E A F
            //D F E A B C
            //...随机运行

            letters.stream().forEach(s->{
                Log.i(TAG,"forEach:"+s);
            });

            //并行流的reduce
            /**
             * 执行总思路：二分
             * 过程：f,e合并，fe和d合并
             *      b,c合并, bc和a合并
             *      abc和def合并
             *
             *      每次合并结果的顺序和传入参数的顺序一样
             */
            String result = letters.parallelStream().reduce("", (s1, s2) -> {
                Log.i(TAG, "accumulator:<" + s1 + " " + s2 + ">--thread name:" + Thread.currentThread().getName());
                return s1 + "+" + s2;
            }, (s1,s2)->{
                Log.i(TAG, "combiner:<" + s1 + " " + s2 + ">--thread name:" + Thread.currentThread().getName());
                return s1 + "=" + s2;
            });
            Log.i(TAG,"result:"+result);

            //串行流的reduce
            result = letters.stream().reduce("", (s1, s2) -> {
                Log.i(TAG, "accumulator:<" + s1 + " " + s2 + ">--thread name:" + Thread.currentThread().getName());
                return s1 + "+" + s2;
            }, (s1,s2)->{
                Log.i(TAG, "combiner:<" + s1 + " " + s2 + ">--thread name:" + Thread.currentThread().getName());
                return s1 + "=" + s2;
            });
            Log.i(TAG,"result:"+result);
        }
    }

    private void studyOfLambda() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            addUrls();
            //lambda表达式之匿名内部类
            new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    System.out.println("I love you son");
                }
            }).start();

            //lambda表达式之设置监听
            findViewById(R.id.btn_confirm).setOnClickListener((view) -> Toast.makeText(MainActivity.this, "Here we go.." + view.getClass().getCanonicalName(), Toast.LENGTH_LONG).show());

            //lambda表达式之过滤
            try {
                filter(urls, (item) -> item.toString().startsWith("http"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            separate();

            //lambda表达式之过滤条件的合并
            Predicate<String> startWithHttp = (item) -> item.startsWith("http");
            Predicate<String> tenLettersLong = (item) -> item.length() >= 4;
            urls.stream().filter(tenLettersLong.and(startWithHttp)).forEach((item) -> Log.i(TAG, item.toString()));
            //类似的还有or(),xor()方法
            separate();

            //map方法
            urls.stream().map((item) -> item.toLowerCase()).forEach((item) -> Log.i(TAG, item.toString()));

            //构造列表 range()就相当于一个for循环，但返回的是一个stream对象
            IntStream.range(0, students.length).forEach((i) -> {
                studentList.add(students[i]);
            });

            //flatMap方法:第二个泛型必须是stream类型；而map方法第二个泛型没有限制
            studentList.stream().flatMap(student -> {
                List<String> info = new ArrayList<>();
                info.add(student.toString());
                return info.stream();
            }).forEach(item -> Log.i(TAG, item.toString()));
            separate();

            //reduce方法:折叠
            //效果：url1--url2--url3
            String result = urls.stream().map((item) -> item.toLowerCase()).reduce((item1, item2) -> (item1 + "--" + item2)).get();
            Log.i(TAG, result);
            separate();

            //collect方法：把stream对象变成list
            List<String> results = urls.stream().filter((item) -> item.length() > 20).collect(Collectors.toList());
            results.forEach((item) -> Log.i(TAG, item));
            separate();

            //joining方法 注意返回值不是list
            result = urls.stream().filter((item) -> item.length() > 20).collect(Collectors.joining(","));
            Log.i(TAG, result);
            separate();

            //distinct方法 去重
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 3, 2, 5);
            List<Integer> gets = numbers.stream().map((item) -> item * item).distinct().collect(Collectors.toList());
            gets.forEach((item) -> Log.i(TAG, item + ""));
            separate();

            //数字类型的stream可以先mapToInt，再获取intSummaryStatistics对象
            IntSummaryStatistics stats = gets.stream().mapToInt((item) -> item).summaryStatistics();
            Log.i(TAG, "最大的数：" + stats.getMax());
            Log.i(TAG, "最小的数：" + stats.getMin());
            Log.i(TAG, "和：" + stats.getSum());
            Log.i(TAG, "平均数:" + stats.getAverage());

            /**
             * lambda表达式限制：对于外部变量，只能访问但不能修改值。
             */
        }
    }

    private void separate() {
        Log.i(TAG,"========================================================================");
    }

    private void filter(List list, Predicate predicate) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (predicate.test(list.get(i))) {//test函数：参数是否符合过滤条件
                    Log.i(TAG, "" + list.get(i).toString());
                }
            }
        }
    }

    private void studyOfRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new AdapterForRecyclerVIew(urls, descriptions, this);

        addUrls();
        addDescriptions();

        recyclerView.setAdapter(adapter);
    }

    private void addDescriptions() {
        descriptions.add("南部之星拜仁慕尼黑");
        descriptions.add("骄傲的大黄蜂多特蒙德");
        descriptions.add("蓝月亮曼城");
        descriptions.add("红魔曼联");
        descriptions.add("蓝军切尔西");
    }

    private void addUrls() {
        urls.add("http://n.sinaimg.cn/sports/transform/20170216/1s3V-fyarzzv2801842.jpg");
        urls.add("http://www.zq1.com/Upload/20170415/235459msc9i5yiqracirfw.jpg");
        urls.add("https://c1.hoopchina.com.cn/uploads/star/event/images/171106/a694dde65a88b5f820c02c03d62d76caf02aeb09.jpg");
        urls.add("http://k.sinaimg.cn/n/sports/transform/20160424/dfCS-fxrqhar9877773.JPG/w570fe9.jpg");
        urls.add("http://n.sinaimg.cn/sports/transform/20170423/L9Uj-fyeqcac1387497.jpg");
    }

    @OnClick(R.id.btn_confirm)
    public void translate(Button button) {
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

        reactiveList.changes().subscribe(changeType -> Log.i(TAG, "changeType-"+(++changeCount)+":" + changeType));


        /**
         * 这种情况：先订阅Publish，再发数据，最后订阅Replay和Behavior:
         * 由于发事件的时候，对于Replay和Behavior，情况和第一种一样，所以依旧正常
         * 而对于Publish，由于是先订阅，所以数据发一次，订阅者就接收一次，故而正常输出
         */
        dataOperation();


        reactiveList.changesValues().subscribe(student -> Log.i(TAG, "changesValues:" + student.toString()));

        reactiveList.latestChanged().subscribe(student -> Log.i(TAG, "lastedChanged:" + student.toString()));

        /**
         * 这种情况：先订阅，再发事件
         * 对于Replay,Behavior和Publish，由于发来一个事件就直接有订阅者接收，就不存在缓冲区的问题了
         * 所以结果是发一次事件，三个都按接收顺序各输出一次
         */
//        dataOperation();
    }

    private void dataOperation() {
        for (int i = 0; i < students.length; i++) {
            reactiveList.adder().onNext(students[i]);
        }

        reactiveList.list().subscribe(student -> Log.i(TAG, "list:" + student.toString()));

        students[5].setName("卡尼吉亚");
        reactiveList.updater().onNext(students[5]);

        reactiveList.remover().onNext(students[0]);
    }

    private void simpleUseOfSubject() {
        subscription = MyRxBus.getInstance().tObservable(Student.class).subscribe(student -> Log.i(TAG, student.toString()));
    }

    private void usingIntervalRange() {
        Observable<Long> observable = Observable.intervalRange(0, 20, 1, 1, TimeUnit.SECONDS);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object value) {
                index = ((Long) value).intValue() % 4;
                Log.i(TAG, "index:" + index + "--value:" + value);
                MyRxBus.getInstance().post(new Student("" + value, names[((Long) value).intValue()]));
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "event complete");
            }
        });
    }

    private void requestByRetrofitAndRxJavaUsingZip() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        IRequest iRequest = retrofit.create(IRequest.class);
        Observable<Result> observable1 = iRequest.getResultInRxJava("fy", "auto", "auto", inputs[0]).subscribeOn(Schedulers.io());
        Observable<Result> observable2 = iRequest.getResultInRxJava("fy", "auto", "auto", inputs[1]).subscribeOn(Schedulers.io());
        //zip():按顺序合并两个observable的请求，一起显示结果
        Observable.zip(observable1, observable2, (result, result2) -> {
            //合并两个observable的结果。合并的结果返回到onNext()中去
            return result.toString() + "\n" + result2.toString();
        }).retryWhen(throwableObservable -> throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
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
        })).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
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
        //tryWhen:出错时调用里面的apply方法
        //flatMap:将错误obserable转换为异常throwable
        observable.retryWhen(throwableObservable -> throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
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
        })


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
        dialog = new MyDialog();
        dialog.show(getFragmentManager(),TAG);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //在这里处理权限赋予的结果
        switch (requestCode){
            case 0:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //studyOfExecutor();
                }
                break;
        }
    }
}
