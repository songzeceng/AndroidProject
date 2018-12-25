package applicationmanager.com.example.a123.studyofdatabinding;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.LinkedList;

import applicationmanager.com.example.a123.studyofdatabinding.adapter.MyListViewBindingAdapter;
import applicationmanager.com.example.a123.studyofdatabinding.javaBean.Person;
import applicationmanager.com.example.a123.studyofdatabinding.monitor.Monitor;
import applicationmanager.com.example.a123.studyofdatabinding.myInterface.IActivity;

public class MainActivity extends Activity implements IActivity {
    private ObservableInt countObservable = new ObservableInt();
    private ObservableField<String> infoObservable = new ObservableField<>();
    private ObservableArrayMap<String, String> mapObservable = new ObservableArrayMap<>();
    private ObservableArrayList<String> arraysObservable = new ObservableArrayList<>();
    private ObservableInt indexObservable = new ObservableInt();
    private Person person;
    private ViewDataBinding mBinding;

    private ListView mListView;
    private MyListViewBindingAdapter mAdapter;
    private LinkedList<Person> mPeole = new LinkedList<>();

    private Monitor mMonitor;

    private String[] names = new String[]{
            "A", "B", "C", "D"
    };
    private String[] descriptions = new String[]{
      "aa", "bb", "cc", "dd"
    };

    private String[] urls = new String[]{
        "http://p5.so.qhimgs1.com/bdr/_240_/t0158432ac9d02c74bb.jpg",
        "http://p4.so.qhmsg.com/bdr/_240_/t015300f4f65dc07f89.jpg",
        "http://p3.so.qhimgs1.com/bdr/_240_/t014bbc3f5ab3755383.jpg",
        "http://p0.so.qhimgs1.com/bdr/_240_/t017ee2f5c7e5fcef90.jpg",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // 加载绑定布局
        mListView = findViewById(R.id.list_people);
        mAdapter = new MyListViewBindingAdapter(mPeole, this);
        mListView.setAdapter(mAdapter);

        for (int i = 0; i < 10; i++) {
            String name = names[i % names.length];
            int age = i + 1;
            String url = urls[i % urls.length];
            mPeole.add(new Person(name, age, url));
        }

        mMonitor = new Monitor(this);
//        DataBindingUtil.setDefaultComponent(mMonitor);

        person = new Person("Jason", 24, "http://p0.so.qhmsg.com/bdr/_240_/t01825773612648be9f.jpg");
        mBinding.setVariable(applicationmanager.com.example.a123.studyofdatabinding.BR.person, person);
        mBinding.setVariable(applicationmanager.com.example.a123.studyofdatabinding.BR.count, countObservable);
        mBinding.setVariable(applicationmanager.com.example.a123.studyofdatabinding.BR.info, infoObservable);
        mBinding.setVariable(applicationmanager.com.example.a123.studyofdatabinding.BR.maps, mapObservable);
        mBinding.setVariable(applicationmanager.com.example.a123.studyofdatabinding.BR.arrays, arraysObservable);
        mBinding.setVariable(BR.index, indexObservable);
        mBinding.setVariable(BR.monitor,mMonitor);
        // 绑定数据

//        findViewById(R.id.increment).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                person.setAge(person.getAge() + 1);
//            }
//        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    String name = names[i % names.length];
                    String decription = descriptions[i % descriptions.length];

                    countObservable.set(i + 1); // set方法设值
                    infoObservable.set("当前计数器的值是：" + (i + 1) + ";线程名：" + Thread.currentThread().getName());
                    mapObservable.put("name", name);
                    mapObservable.put("description", decription);
                    arraysObservable.add(name);
//                    arraysObservable.set(0, name);

                    try {
                        Thread.sleep(3 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    indexObservable.set(i);
                    try {
                        Thread.sleep(3 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 0);
        }
    }

    @Override
    public void onDataChanged(int data) {
        mPeole.get(0).setAge(data);
    }

    @Override
    public int getData() {
        return mPeole.get(0).getAge();
    }

    @Override
    public void setUrl(String url, ImageView view) {
      //  Glide.with(getApplicationContext()).load(Uri.parse(url)).into(view);
        /**
         * 利用Monitor加载图片：
         *  1、设计加载器父类，加载图片的方法用BindAdapter注解，不用加static，最好写成抽象方法
         *  2、设计子类实现加载图片具体方法，如果用到Context，可以在父类用弱引用防止内存泄漏
         *  3、Monitor实现DataBindingComponent，实现方法返回加载图片类(子类)，返回前可以编译一次让DataBindingComponent出现
         *  4、在Activity或Fragment里设置默认Component为Monitor
         *
         *  利用MVP：
         *      1、Activity实现接口，在Monitor构造方法中传入接口对象，Monitor构造方法里为加载器的弱引用实例化
         *      2、在加载器子类的加载方法里，调用弱引用对象（接口）的加载方法，从而跳到Activity里
         *
         *  但这样似乎不利于实现多态
         */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
