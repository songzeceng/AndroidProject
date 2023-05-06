package com.example.test1.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Browser;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

import com.example.test1.R;
import com.example.test1.utils.PerformanceUtil;

import java.net.URISyntaxException;

//@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
    public static final String JS_URL_B = "javascript:bb('这是Java调用JavaScript的结果')";
    private static final String TAG = "MainActivity";
    private WebView mWebView;
    private String mURLRemote = "http://www.baidu.com";
    private String mURLLocal = "file:///android_asset/index.html";
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: Current temperature: " + intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10f);
        }
    };
    // file:///android_asset/index.html
    // https://m.baidu.com/?from=844b&vit=fps
    // http://www.baidu.com

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.web_view);


//        openUrlWithWebView();

//        PerformanceUtil.testBashCommand();

//        testMainThread();
//
//        new Thread(MainActivity::testWorkerThread).start();

//        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

//        System.out.println(openUrlWithIntent(mURLLocal));
    }

    @MainThread
    private static void testMainThread() {
        Log.i(TAG, "testMainThread: Thread`s name: " + Thread.currentThread().getName());
    }

    @WorkerThread
    private static void testWorkerThread() {
        Log.i(TAG, "testWorkerThread: Thread`s name: " + Thread.currentThread().getName());
    }

    private void openUrlWithWebView() {
        WebSettings webSettings = mWebView.getSettings();
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "shouldOverrideUrlLoading: url = " + url);
                Log.i(TAG, "shouldOverrideUrlLoading: Thread`s name: " + Thread.currentThread().getName());
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebView.evaluateJavascript(JS_URL_B, value ->
                        Log.i(TAG, "onReceiveValue: Thread`s name: "
                                + Thread.currentThread().getName()
                                + ", value: " + value));
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {});
        mWebView.loadUrl(mURLRemote);

        mWebView.addJavascriptInterface(this, "obj");
    }

    @JavascriptInterface
    public String cc() {
        String threadName = Thread.currentThread().getName();
        Log.i(TAG, "cc: Thread`s name: " + threadName);
        Log.i(TAG, "cc: Is now in main thread? " + (Looper.myLooper() == Looper.getMainLooper()));
        Log.i(TAG, "cc: Current temperature: " + PerformanceUtil.getTemperature(this));
        Log.i(TAG, "cc: Current cpu rate: " + PerformanceUtil.getCpuUsage());
        return "cc in native.Thread`s name: " + threadName;
    }

    private boolean openUrlWithIntent(String url) {
        Intent intent = null;
        // Perform generic parsing of the URI to turn it into an Intent.
        try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            Log.i(TAG, "Bad Url: " + url);
            e.printStackTrace();
            return false;
        }
        if (intent == null) {
            return false;
        }
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setComponent(null);
        Intent selector = intent.getSelector();
        if (selector != null) {
            selector.addCategory(Intent.CATEGORY_BROWSABLE);
            selector.setComponent(null);
        }

        intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException ex) {
            Log.w(TAG, "No application can handle " + url);
        } catch (SecurityException ex) {
            Log.w(TAG, "SecurityException when starting intent for " + url);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mReceiver);
        mWebView.destroy();
    }
}