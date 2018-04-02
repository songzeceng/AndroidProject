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
import android.widget.TextView;

import com.example.songzeceng.studyoflivedata.Thread.ThreadUtil;
import com.example.songzeceng.studyoflivedata.entity.User;
import com.example.songzeceng.studyoflivedata.entity.UserPerforms;
import com.example.songzeceng.studyoflivedata.entity.UserSimple;
import com.example.songzeceng.studyoflivedata.room.CRUDUtil;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by songzeceng on 2018/3/16.
 */

public class MainFragment extends Fragment {
    public static final String TAG = "MainFragment";
    public static final int READY_TO_SHOW_WITH_LIMITS = 1;
    public static final int READY_TO_UPDATE_USERS_INFO = 2;

    private MyModel nameModel;
    private Observer<String> nameObserver;
    private Random r = new Random();

    private ArrayList<UserSimple> users = new ArrayList<UserSimple>();
    private ArrayList<User> usersAll = new ArrayList<User>();
    private ArrayList<User> usersShow = new ArrayList<User>();
    private ArrayList<UserPerforms> performsAll = new ArrayList<UserPerforms>();
    private ArrayList<UserPerforms> performs = new ArrayList<UserPerforms>();

    private CompositeDisposable disposable = new CompositeDisposable();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case READY_TO_SHOW_WITH_LIMITS:
                    traverseList(users);
                    separate("end of users in limits");
                    traverseList(usersShow);
                    separate("end of users to show");
                    traverseList(usersAll);
                    separate("end of all users");
                    break;
                case READY_TO_UPDATE_USERS_INFO:
                    ThreadUtil.executeRunnable(new Runnable() {
                        @Override
                        public void run() {
                            performs = nameModel.getAllPerforms(getContext());
                            for (int i = 0; i < usersAll.size(); i++) {
                                User user = usersAll.get(i);
                                user.setPerforms(performs.get(i));
                            }

                        }
                    });
                    break;
            }
        }
    };


    @BindView(R.id.tv_name)
    TextView tv_name;

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

        liveDataSimple();

        setInfo();
        Collections.sort(usersAll);

        disposable.add(nameModel.getAllUsersFlowable(getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        Collections.sort(users);
                        traverseList((ArrayList) users);
                        separate("end of users observable");
                    }
                }));
    }

    private void liveDataSimple() {
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
    }

    private void setInfo() {
        usersAll.add(new User(11, "Jason", "中锋"));
        usersAll.add(new User(17, "Mike", "前腰"));
        usersAll.add(new User(2, "Kane", "中卫"));
        usersAll.add(new User(8, "Handerson", "后腰"));
        usersAll.add(new User(1, "Frank", "门将"));
        performsAll.add(new UserPerforms(11, 32, 15));
        performsAll.add(new UserPerforms(1, 0, 0));
        performsAll.add(new UserPerforms(17, 20, 23));
        performsAll.add(new UserPerforms(8, 6, 10));
        performsAll.add(new UserPerforms(2, 3, 4));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btn_change)
    public void changeName() {
        //String name = "name-" + Math.abs(r.nextInt() % 20);

        //nameModel.getName().setValue(name);

        //更新liveData的方法.主线程:setValue()，子线程:postValue()

        ThreadUtil.executeRunnable(new Runnable() {
            @Override
            public void run() {
                nameModel.insert(usersAll.toArray(new User[1]), getContext());
                nameModel.insert(performsAll.toArray(new UserPerforms[1]), getContext());

                usersAll.get(2).setName("Justin");
                nameModel.update(usersAll.get(2), getContext());

                handler.sendEmptyMessage(READY_TO_UPDATE_USERS_INFO);
            }
        });

    }

    @OnClick(R.id.btn_update)
    public void update() {
        ThreadUtil.executeRunnable(new Runnable() {
            @Override
            public void run() {
                usersAll.get(1).setName("szc");
                nameModel.update(usersAll.get(1), getContext());
            }
        });
    }

    @OnClick(R.id.btn_clear)
    public void clear() {
        ThreadUtil.executeRunnable(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < usersAll.size(); i++) {
                    nameModel.delete(usersAll.get(i), getContext());
                    sleep(1000);
                }
            }
        });
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logger(String info) {
        Log.i(TAG, info);
    }

    private void traverseList(ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            logger(list.get(i).toString());
        }
    }

    private void separate(String info) {
        logger("------" + info + "------");
    }
}
