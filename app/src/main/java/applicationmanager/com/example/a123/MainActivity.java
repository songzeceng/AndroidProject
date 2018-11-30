package applicationmanager.com.example.a123;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logger("A - onCreate()");
        startActivity(new Intent(this, BActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        logger("A - onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logger("A - onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger("A - onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logger("A - onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logger("A - onStop");
   //     mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logger("A - onDestroy");
    }

    public void logger(String info) {
        Log.i(TAG, info);
    }
}
