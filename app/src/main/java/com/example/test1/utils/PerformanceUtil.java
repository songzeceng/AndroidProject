package com.example.test1.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Process;
import android.util.Log;

import java.util.Scanner;

public class PerformanceUtil {
    public static final String TAG = "PerformanceUtil";

    private static final String COMMAND_OTHER = "pm list packages";
    private static final String COMMAND_TOP = "top -n 1 -m 1000";
    private static final String[] COMMAND_TOP_LIST = new String[]{ "/bin/sh", "-c", COMMAND_TOP };

    public static void testBashCommand() {
        java.lang.Process process = null;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            process = builder.command(COMMAND_TOP_LIST).start();
//            process = Runtime.getRuntime().exec("top -n 1 -d 1 -m 1000");
            int ret = process.waitFor();
            Log.i(TAG, "testBashCommand: ret = " + ret);
            Scanner scanner = new Scanner(process.getInputStream());
            while (scanner.hasNext()) {
                Log.i(TAG, scanner.nextLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    public static float getTemperature(Context context) {
        if (context == null) {
            return -1f;
        }

        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (intent == null) {
            return -1f;
        }

        return intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10f;
    }

    public static float getCpuUsage() {
        try {
            int pid = Process.myPid();
            String[] cmd = new String[] {
                    "sh",
                    "-c",
                    "top -m 1000 -d 1 -n 1 | grep " + pid
            };
            java.lang.Process p = Runtime.getRuntime().exec(cmd);
            Scanner scanner = new Scanner(p.getInputStream());

            String line = scanner.nextLine();
            String s = line.split("\\s+")[8];

            p.destroy();
            scanner.close();

            float v = Float.parseFloat(s);
            int processors = Runtime.getRuntime().availableProcessors();
            return v / processors;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1F;
    }
}
