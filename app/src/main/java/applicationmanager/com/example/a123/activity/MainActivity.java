package applicationmanager.com.example.a123.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.alipay.euler.andfix.patch.PatchManager;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import applicationmanager.com.example.a123.R;
import applicationmanager.com.example.a123.application.BaseApplication;
import applicationmanager.com.example.a123.master.Master;
import applicationmanager.com.example.a123.util.Constants;
import applicationmanager.com.example.a123.util.Logger;

public class MainActivity extends Activity {
    private Master master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.log("A - onCreate()");
//        startActivity(new Intent(this, BActivity.class));
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Logger.log("申请写外存权限");
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        if (new File(Constants.PATCH_PATH).exists()) {
            Application application = getApplication();
            if (application instanceof BaseApplication) {
                try {
                    PatchManager patchManager = ((BaseApplication) application).getPatchManager();
                    patchManager.addPatch(Constants.PATCH_PATH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.log("A - onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.log("A - onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.log("A - onResume");

        int total = 0;
        int coreNumber = Runtime.getRuntime().availableProcessors();
        Logger.log("cpu核数：" + coreNumber);
        master = new Master(coreNumber);
        for (int i = 1; i < 50; i++) {
            master.addTask(i);
        }

        long beginTime = System.currentTimeMillis();

        master.beginWork();

        ConcurrentLinkedQueue<Object> results = master.getResults();
        while ((results != null && !results.isEmpty()) || !master.isComplete()) {
            Object obj = results.poll();
            if (obj != null && obj instanceof Integer) {
                int result = (Integer) obj;
                total += result;
            }
        }

        long overTime = System.currentTimeMillis();
        Logger.log("用时：" + (int) (overTime - beginTime));
        int i = 2/0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.log("A - onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.log("A - onStop");
   //     mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.log("A - onDestroy");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }
}
