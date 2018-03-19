package com.example.songzeceng.studyoflivedata;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main,fragment).commit();
        //getSupportFragmentManager()只能在FragmentActivity或AppCompatActivity中调用
    }
}
