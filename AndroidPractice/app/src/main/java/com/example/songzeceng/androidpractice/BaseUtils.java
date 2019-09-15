package com.example.songzeceng.androidpractice;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class BaseUtils {
    //获取手机类型
    private static String getMobileType() {
        return Build.MANUFACTURER;
    }

    public static void jumpStartInterface(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.i("HLQ_Struggle", "******************当前手机型号为：" + getMobileType());
            ComponentName componentName = null;
            if (getMobileType().equals("HUAWEI")) { // 华为测试通过
                componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            }
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            Log.e("HLQ_Struggle", e.getLocalizedMessage());
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

    /**
     * hook住OnClick方法
     * view.setOnClickListener()方法会给mListenerInfo对象里的mOnClickListener属性赋值
     * mListenerInfo对象通过getListenerInfo()方法获取
     *
     * @param context 上下文，提供动态代理的classLoader
     * @param view 被hook的view对象
     */
    public static void hookOnClick(Context context, View view) {
        try {
            // 1、获取mListenerInfo对象
            Method method = View.class.getDeclaredMethod("getListenerInfo");
            method.setAccessible(true);
            Object listenerInfo = method.invoke(view);

            // 2、获取mListener对象的mOnClickListener属性
            Class<?> listenerInfoClass = listenerInfo.getClass();
            Field field = listenerInfoClass.getField("mOnClickListener");
            final View.OnClickListener onClickListener = (View.OnClickListener) field.get(listenerInfo);

            // 3、通过动态代理，实例化代理对象
            Object proxyClickListener = Proxy.newProxyInstance(context.getClassLoader(),
                    new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    // 执行新的逻辑(埋点、输出日志等)
                    Log.i("MainActivity", "invoke: view is clicked");
                    // 执行老的逻辑
                    return method.invoke(onClickListener, args);
                }
            });

            // 4、更新mOnClickListener属性
            field.set(listenerInfo, proxyClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
