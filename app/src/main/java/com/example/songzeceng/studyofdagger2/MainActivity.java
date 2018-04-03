package com.example.songzeceng.studyofdagger2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.songzeceng.studyofdagger2.Interfaces.AppComponent;
import com.example.songzeceng.studyofdagger2.Interfaces.AppModule;
import com.example.songzeceng.studyofdagger2.Interfaces.DaggerAppComponent;
import com.example.songzeceng.studyofdagger2.Interfaces.DaggerSimpleComponent;
import com.example.songzeceng.studyofdagger2.Interfaces.SimpleComponent;
import com.example.songzeceng.studyofdagger2.Interfaces.SimpleModule;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private SimpleComponent simpleComponent;
    private AppComponent appComponent;
    @Inject
    CoffeeMachine coffeeMachine;

    @Inject
    CoffeeMachine coffeeMachine2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleComponent = DaggerSimpleComponent.builder().simpleModule(new SimpleModule("我","洞庭碧螺春"))
                .build();
        simpleComponent.inject(this);
        coffeeMachine.makeCoffee1();
        Log.i("Cooker","-------------------------------------");
        coffeeMachine.makeCoffee2();
        Log.i("Cooker","-------------------------------------");
        Log.i("Cooker","Two fdsCoffeeMachine are same? --> "+(coffeeMachine.getFatherAndSonCoffeeMaker() == coffeeMachine2.getFatherAndSonCoffeeMaker()));

        appComponent = DaggerAppComponent.builder().appModule(new AppModule("张三","西湖龙井")).build();
        appComponent.inject(getApplication());

    }
}
