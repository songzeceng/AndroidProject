package com.example.test1.hook;

import android.util.Log;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.This;
import me.ele.lancet.base.annotations.ClassOf;
import me.ele.lancet.base.annotations.ImplementedInterface;
import me.ele.lancet.base.annotations.Insert;
import me.ele.lancet.base.annotations.Proxy;
import me.ele.lancet.base.annotations.TargetClass;

/**
 * Lancet Hook代理框架测试，官方教程：https://github.com/eleme/lancet/blob/develop/README_zh.md
 *
 * 应保持Hook类的纯净，即该类中只有Hook方法，没有普通方法
 */
public class LancetHook {
    private static final String TAG = "LancetHook";

    // 替换目标方法
    @Proxy("i")
    @TargetClass("android.util.Log")
    // 这里的访问修饰符、参数列表和返回值分别取决于要hook（代理，此处代理方式为替换）方法的访问修饰符和参数列表和返回值
    // 比如这个例子：
    //     要hook的方法是：public static int android.util.Log.i(String, String)
    //     所以hookLogI的方法签名为：public static int hookLogI(String, String)
    // 对Hook方法所在的类（此处为LancetHook）中的目标方法调用不生效
    public static int hookLogI(String tagi, String msg) {
        msg += " by hookLogI"; // 修改入参
        return (int) Origin.call(); // 调用原有方法
    }

    // 在目标方法调用前后添加执行逻辑，不替换目标方法
    @Insert(value = "onStop", mayCreateSuper = true)
    @TargetClass(value = "android.app.Activity", scope = Scope.LEAF)
    // mayCreateSuper = true，表示在目标方法没有被重写时，添加重写方法
    // scope = Scope.LEAF，表示代理范围为目标类的叶子结点子类，即最终子类
    protected void hookActivityOnStop() {
        Log.i(TAG, "hookActivityOnStop, before call original method");
        Origin.callVoid();
        Log.i(TAG, "hookActivityOnStop, after call original method");
    }
    /*
      Scope.SELF：代表仅匹配目标类本身。
      Scope.DIRECT：代表匹配目标类的直接子类。
      Scope.All：代表匹配目标类的所有子类。
      Scope.LEAF：代表匹配目标类的最终子类。
          众所周知java是单继承，所以继承关系是树形结构，所以这里代表了指定类为顶点的继承树的所有叶子结点。
     */

    @Insert(value = "funA")
    @ImplementedInterface(value = "com.example.test1.hook.hookCase.InterfaceA", scope = Scope.SELF)
    public void funAHookSelf() {
        Log.i(TAG, "funAHookSelf by " + This.get().getClass().getCanonicalName());
        Origin.callVoid();
        // 2023-05-06 15:58:53.214 12410-12410/com.example.test1 I/LancetHook: funAHookSelf by com.example.test1.hook.hookCase.ClassA
    }

    @Insert(value = "funA")
    @ImplementedInterface(value = "com.example.test1.hook.hookCase.InterfaceA", scope = Scope.DIRECT)
    public void funAHookDirect() {
        Log.i(TAG, "funAHookDirect by " + This.get().getClass().getCanonicalName());
        Origin.callVoid();
        /*
        2023-05-06 15:58:53.214 12410-12410/com.example.test1 I/LancetHook: funAHookDirect by com.example.test1.hook.hookCase.ClassA
        2023-05-06 15:58:53.215 12410-12410/com.example.test1 I/LancetHook: funAHookDirect by com.example.test1.hook.hookCase.ClassC
        2023-05-06 15:58:53.215 12410-12410/com.example.test1 I/LancetHook: funAHookDirect by com.example.test1.hook.hookCase.ClassD
         */
    }

    @Insert(value = "funA")
    @ImplementedInterface(value = "com.example.test1.hook.hookCase.InterfaceA", scope = Scope.LEAF)
    public void funAHookLeaf() {
        Log.i(TAG, "funAHookLeaf by " + This.get().getClass().getCanonicalName());
        Origin.callVoid();
        /*
        2023-05-06 15:58:53.214 12410-12410/com.example.test1 I/LancetHook: funAHookLeaf by com.example.test1.hook.hookCase.ClassB
        2023-05-06 15:58:53.215 12410-12410/com.example.test1 I/LancetHook: funAHookLeaf by com.example.test1.hook.hookCase.ClassD
        2023-05-06 15:58:53.215 12410-12410/com.example.test1 I/LancetHook: funAHookLeaf by com.example.test1.hook.hookCase.ClassE
         */
    }

    @Insert(value = "funA")
    @ImplementedInterface(value = "com.example.test1.hook.hookCase.InterfaceA", scope = Scope.ALL)
    public void funAHookAll() {
        Log.i(TAG, "funAHookAll by " + This.get().getClass().getCanonicalName());
        Origin.callVoid();
        /*
        2023-05-06 15:58:53.214 12410-12410/com.example.test1 I/LancetHook: funAHookAll by com.example.test1.hook.hookCase.ClassA
        2023-05-06 15:58:53.214 12410-12410/com.example.test1 I/LancetHook: funAHookAll by com.example.test1.hook.hookCase.ClassB
        2023-05-06 15:58:53.215 12410-12410/com.example.test1 I/LancetHook: funAHookAll by com.example.test1.hook.hookCase.ClassC
        2023-05-06 15:58:53.215 12410-12410/com.example.test1 I/LancetHook: funAHookAll by com.example.test1.hook.hookCase.ClassD
        2023-05-06 15:58:53.215 12410-12410/com.example.test1 I/LancetHook: funAHookAll by com.example.test1.hook.hookCase.ClassE
         */
    }

    // 如果hook函数中的参数类我们拿不到，可以使用ClassOf来实现类导入
    @Insert(value = "execute")
    @TargetClass(value = "com.example.test1.hook.hookCase.ClassWithInner")
    private void funHookInner(@ClassOf(value = "com.example.test1.hook.hookCase.ClassWithInner$ClassInner") Object inner) {
        Log.i(TAG, "funHookInner by " + This.get().getClass().getCanonicalName());
        Log.i(TAG, "funHookInner: inner object`s class is " + inner.getClass().getCanonicalName());
        Origin.callVoid();
        /*
        2023-05-06 15:58:53.215 12410-12410/com.example.test1 I/LancetHook: funHookInner by com.example.test1.hook.hookCase.ClassWithInner
        2023-05-06 15:58:53.215 12410-12410/com.example.test1 I/LancetHook: funHookInner: inner object`s class is com.example.test1.hook.hookCase.ClassWithInner.ClassInner
         */
    }

    // 异常捕获，可以通过Origin.callXXXThrowOne/Two/Three()来实现
    // 其实也可以像上面一样，调用Origin.callXXX()
    @Proxy(value = "test")
    @TargetClass(value = "com.example.test1.hook.hookCase.ExceptionThrow")
    public void hookExceptionTest() {
        try {
            Origin.callVoidThrowOne();
            // Origin.callVoid(); // Same
        } catch (Exception e) {
            Log.i(TAG, "hookExceptionTest: " + e.getMessage());
        }
        // 2023-05-06 16:06:50.725 12860-12860/com.example.test1 I/LancetHook: hookExceptionTest: IllegalAccessException in ExceptionThrow#test()!
    }

    // 读写字段
    // 可通过This.putField()和This.getField()对字段进行写和读，不管该字段的访问修饰符如何
    // 如果目标字段不存在，则自动创建
    // 注意：
    //     Proxy代理方式不能使用This；
    //     不能读写目标类父类的字段：当尝试读写父类字段时，还是会创建新的字段。
    @Insert("print")
    @TargetClass(value = "com.example.test1.hook.hookCase.ClassWithField")
    public void hookPrint() {
        // 读字段
        Object field0Value = This.getField("field0");
        Object field1Value = This.getField("field1");
        Log.i(TAG, "hookPrint: field0Value is " + field0Value + ", field1Value is " + field1Value);
        // 2023-05-06 16:24:26.352 17078-17078/com.example.test1 I/LancetHook: hookPrint: field0Value is 0, field1Value is

        Origin.callVoid();
        // 写字段
        This.putField(1, "field0");
        This.putField("value", "field1");
        Origin.callVoid();

        /*
        2023-05-06 16:24:26.352 17078-17078/com.example.test1 I/ClassWithField: print: ClassWithField{field0=0, field1=} by hookLogI
        2023-05-06 16:24:26.352 17078-17078/com.example.test1 I/ClassWithField: print: ClassWithField{field0=1, field1=value} by hookLogI
         */
    }
}
