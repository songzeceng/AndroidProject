package applicationmanager.com.example.a123;

import android.app.Activity;
import android.os.Bundle;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MainActivity extends Activity {
    private Master master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.logger("A - onCreate()");
//        startActivity(new Intent(this, BActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.logger("A - onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.logger("A - onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.logger("A - onResume");

        int total = 0;
        int coreNumber = Runtime.getRuntime().availableProcessors();
        Logger.logger("cpu核数：" + coreNumber);
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
        Logger.logger("用时：" + (int) (overTime - beginTime));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.logger("A - onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.logger("A - onStop");
   //     mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.logger("A - onDestroy");
    }
}
