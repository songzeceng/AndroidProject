package applicationmanager.com.example.a123.application;

import android.app.Application;

import applicationmanager.com.example.a123.caughter.MyExceptionCaughter;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(MyExceptionCaughter.getInstance(getApplicationContext()));
    }
}
