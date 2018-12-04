package applicationmanager.com.example.a123.caughter;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import applicationmanager.com.example.a123.util.Constants;
import applicationmanager.com.example.a123.util.Logger;

public class MyExceptionCaughter implements Thread.UncaughtExceptionHandler {
    private static MyExceptionCaughter caughter;

    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public static MyExceptionCaughter getInstance(Context context) {
        if (caughter == null) {
            synchronized (MyExceptionCaughter.class) {
                if (caughter == null && context != null) {
                    caughter = new MyExceptionCaughter(context);
                }
            }
        }

        return caughter;
    }

    private MyExceptionCaughter(Context context) {
        mContext = context;
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        printLog(t, e);

        saveInFile(e);

        if (mDefaultExceptionHandler != null) {
            mDefaultExceptionHandler.uncaughtException(t, e);
        }
    }

    private void printLog(Thread t, Throwable e) {
        ApplicationInfo applicationInfo = mContext.getApplicationInfo();
        String packageName = applicationInfo.packageName;
        String className = applicationInfo.className;
        String applicationName = applicationInfo.name;
        Logger.log("application包名：" + packageName);
        Logger.log("application包名类名：" + className);
        Logger.log("application包名名：" + applicationName);

        String tName = t.getName();
        long tId = t.getId();
        Logger.log("出错线程名：" + tName);
        Logger.log("出错线程id：" + tId);

        Logger.log("出错原因：" + e.getMessage());
    }

    private void saveInFile(Throwable e) {
        if (mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            File file = new File(Constants.INFO_PATH);
            file.deleteOnExit();
            file.createNewFile();

            PrintWriter writer = new PrintWriter(new FileOutputStream(file));
            e.printStackTrace(writer);
            writer.flush();
            writer.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
