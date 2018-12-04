package applicationmanager.com.example.a123.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;

import com.alipay.euler.andfix.patch.PatchManager;

import applicationmanager.com.example.a123.caughter.MyExceptionCaughter;

public class BaseApplication extends Application {
    private PatchManager mPatchManager;
    private Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppContext == null) {
            mAppContext = getApplicationContext();
        }
        Thread.setDefaultUncaughtExceptionHandler(MyExceptionCaughter.getInstance(mAppContext));
        initPatchManager();
    }

    private void initPatchManager() {
        try {
            if (mPatchManager == null) {
                mPatchManager = new PatchManager(mAppContext);
            }
            PackageInfo packageInfo = getPackageManager().getPackageInfo(mAppContext.getPackageName(), 0);
            String versionName = packageInfo.versionName;
            mPatchManager.init(versionName);

            mPatchManager.loadPatch();
        } catch (Exception e) {

        }
    }

    public PatchManager getPatchManager() {
        return mPatchManager;
    }

    public Context getAppContext() {
        return mAppContext;
    }
}
