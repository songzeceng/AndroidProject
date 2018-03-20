package com.example.songzeceng.studyoflivedata;

import android.arch.core.util.Function;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.songzeceng.studyoflivedata.room.CRUDDAO;
import com.example.songzeceng.studyoflivedata.room.DatabaseCreator;
import com.example.songzeceng.studyoflivedata.room.User;
import com.example.songzeceng.studyoflivedata.room.UserPerforms;
import com.example.songzeceng.studyoflivedata.room.UserSimple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by songzeceng on 2018/3/16.
 */

public class MainFragment extends Fragment {
    public static final String TAG = "MainFragment";

    private MyModel nameModel;
    private Observer<String> nameObserver;
    private Random r = new Random();

    private ArrayList<UserSimple> users;
    private ArrayList<User> usersAll;
    private ArrayList<User> usersShow;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    executeRunnable(new Runnable() {
                        @Override
                        public void run() {
                            //usersShow = (ArrayList<User>) DatabaseCreator.getInstance(getContext()).getDao().getAllUsers();
                            users = (ArrayList<UserSimple>) getDao().getUserWithLimits(10,10);
                            handler.sendEmptyMessage(1);
                        }
                    });
                    break;
                case 1:
                    for (int i=0;i<users.size();i++){
                        Log.i(TAG,users.get(i).toString());
                    }
                    break;
            }
        }
    };

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.btn_change)
    Button btn_change;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_main_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nameModel = ViewModelProviders.of(this).get(MyModel.class);
        //获取liveData对象.注意of()函数里面要传入的是Fragment对象
        nameModel.setName2((MutableLiveData<String>) Transformations.map(nameModel.getName(), new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input + "--" + input.length();
            }
        }));
        nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //liveData对象发生变化时的回调方法
                if (!TextUtils.isEmpty(s)) {
                    tv_name.setText("");
                    tv_name.setText(s);
                }
            }
        };
        //创建liveData观察者对象

        nameModel.getName2().observe(getActivity(), nameObserver);

        usersAll = (ArrayList<User>) Arrays.asList(new User[]{
                new User("11","Jason","中锋",new UserPerforms("11","32","19")),
                new User("17","Mike","前腰",new UserPerforms("17","37","20")),
                new User("2","Kane","中卫",new UserPerforms("2","1","5")),
                new User("8","Handerson","后腰",new UserPerforms("8","3","7")),
                new User("1","Frank","门将",new UserPerforms("1","0","0"))
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btn_change)
    public void changeName() {
        String name = "name-" + Math.abs(r.nextInt() % 20);

        //nameModel.getName().setValue(name);

        //更新liveData的方法.主线程:setValue()，子线程:postValue()

        executeRunnable(new Runnable() {
            @Override
            public void run() {
                getDao().insert((User[]) usersAll.toArray());
                usersAll.get(2).setPerforms(new UserPerforms("2","3","5"));
                getDao().update(usersAll.get(2));
                handler.sendEmptyMessage(0);
            }
        });

    }

    private CRUDDAO getDao() {
        return DatabaseCreator.getInstance(getContext()).getDao();
    }

    private void executeRunnable(Runnable runnable) {
        getExecutor().execute(runnable);
    }

    private ExecutorService getExecutor() {
        int core_number = Runtime.getRuntime().availableProcessors();
        int keep_alive_time = 3;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingDeque<>();
        return new ThreadPoolExecutor(core_number,core_number*2,
                keep_alive_time,timeUnit,taskQueue, Executors.defaultThreadFactory());
    }

    private void logger(String info) {
        Log.i(TAG, info);
    }
}
