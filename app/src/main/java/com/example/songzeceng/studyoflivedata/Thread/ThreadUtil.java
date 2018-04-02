package com.example.songzeceng.studyoflivedata.Thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by songzeceng on 2018/3/20.
 */

public class ThreadUtil {
    private static final int core_number = Runtime.getRuntime().availableProcessors();
    private static final int keep_alive_time = 3;
    private static final TimeUnit timeUnit = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> taskQueue = new LinkedBlockingDeque<>();
    private static final ExecutorService excutor = new ThreadPoolExecutor(core_number, core_number * 2,
            keep_alive_time, timeUnit, taskQueue, Executors.defaultThreadFactory());

    public static void executeRunnable(Runnable runnable) {
        excutor.execute(runnable);
    }
}
