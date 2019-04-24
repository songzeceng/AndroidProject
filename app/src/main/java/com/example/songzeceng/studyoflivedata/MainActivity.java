package com.example.songzeceng.studyoflivedata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.GridView;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

public class MainActivity extends FragmentActivity {
    private TimerTask mTimerTask;
    private Timer mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.activity_main);
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("a");
        linkedList.add("b");
        linkedList.add("c");
        linkedList.add("d");
        linkedList.add("e");

        MyAdapter adapter = new MyAdapter(linkedList, this);
        GridView gridView = findViewById(R.id.grid_view);
        gridView.setAdapter(adapter);

        try {
            VideoView videoView = findViewById(R.id.video);
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.movie));
            videoView.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        startService(new Intent(getApplicationContext(), LockScreenService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "onResume: ");
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i("MainActivity", "run: 任务被触发");
            }
        };
        mTimer.schedule(mTimerTask, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MainActivity", "onStop: ");
        mTimer.cancel();
    }
}
