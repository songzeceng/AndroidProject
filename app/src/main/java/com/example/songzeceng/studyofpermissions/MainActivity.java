package com.example.songzeceng.studyofpermissions;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private LocalBroadcastManager broadcastManager = null;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            logger(intent.getStringExtra("info"));
        }
    };

    @BindView(R.id.info1)
    EditText et1;
    @BindView(R.id.info2)
    EditText et2;
    @BindView(R.id.info3)
    EditText et3;
    @BindView(R.id.confirm)
    Button btn;

    private String info = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        broadcastManager = LocalBroadcastManager.getInstance(this);
        ButterKnife.bind(this);
        btn.setClickable(false);
//        LocalBroadcastManager.getInstance(this).sendBroadcast()
//        sendBroadcast();

        Observable<CharSequence> observable1 = createEditTextObservable(et1);
        Observable<CharSequence> observable2 = createEditTextObservable(et2);
        Observable<CharSequence> observable3 = createEditTextObservable(et3);

        //combineLatest()方法返回一个ObservableCombineLatest对象
        usingCombineLast(observable1, observable2, observable3);

        // usingZip(observable1, observable2, observable3);
        btn.setOnClickListener((v) -> {
            Intent intent = new Intent();
            intent.setAction(TAG);
            intent.putExtra("info", info);
            broadcastManager.sendBroadcast(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TAG);
        broadcastManager.registerReceiver(receiver, filter);
    }

    private void usingZip(Observable<CharSequence> observable1, Observable<CharSequence> observable2, Observable<CharSequence> observable3) {
        Observable.zip(observable1, observable2, observable3,
                (Function3<CharSequence, CharSequence, CharSequence, Object>)
                        (charSequence, charSequence2, charSequence3) -> {

                            boolean flag1 = !TextUtils.isEmpty(charSequence);
                            boolean flag2 = !TextUtils.isEmpty(charSequence2);
                            boolean flag3 = !TextUtils.isEmpty(charSequence3);

                            logger(flag1 + "--" + flag2 + "--" + flag3);

                            return flag1 && flag2 && flag3;
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof Boolean) {
                        btn.setClickable(((Boolean) o).booleanValue());
                        if (((Boolean) o).booleanValue()) {
                            info = et1.getText().toString() + "--" + et2.getText().toString() + "--" + et3.getText().toString();
                        }
                    }
                });
    }

    private void usingCombineLast(Observable<CharSequence> observable1, Observable<CharSequence> observable2, Observable<CharSequence> observable3) {
        Observable.combineLatest(observable1, observable2, observable3,
                (Function3<CharSequence, CharSequence, CharSequence, Object>)
                        (charSequence, charSequence2, charSequence3) -> {

                            boolean flag1 = !TextUtils.isEmpty(charSequence);
                            boolean flag2 = !TextUtils.isEmpty(charSequence2);
                            boolean flag3 = !TextUtils.isEmpty(charSequence3);

                            return flag1 && flag2 && flag3;
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object value) {
                        Log.i(TAG, value.getClass().getCanonicalName());
                        if (value instanceof Boolean) {
                            Log.i(TAG, ((Boolean) value).booleanValue() + "");
                            boolean flag = ((Boolean) value).booleanValue();
                            btn.setClickable(flag);
                            if (flag) {
                                info = et1.getText().toString() + "--" + et2.getText().toString() + "--" + et3.getText().toString();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void logger(String info) {
        Log.i(TAG, info);
    }

    private Observable<CharSequence> createEditTextObservable(EditText editText) {
        Observable<CharSequence> observable1 = RxTextView.textChanges(editText);
        //创建一个TextViewTextObservable对象
        return observable1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        broadcastManager.unregisterReceiver(receiver);
        //广播监听的注册和注销要成对出现
    }
}
