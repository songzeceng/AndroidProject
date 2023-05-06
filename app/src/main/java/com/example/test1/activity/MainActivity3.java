package com.example.test1.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.example.test1.R;
import com.example.test1.utils.RomUtils;
import com.example.test1.service.RecordScreenService;

import java.lang.reflect.Method;

public class MainActivity3 extends Activity {
    private static final String TAG = "MainActivity3";
    private static final int REQUEST_CODE = 1;

    private LifecycleRegistry mLifecycleRegistry;
    private final LifecycleOwner mLifecycleOwner = new LifecycleOwner() {
        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return mLifecycleRegistry;
        }
    };

    private String mAppPackageName;
    private MediaProjectionManager mMediaProjectionManager;
    private DisplayManager mDisplayManager;
    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        @Override
        public void onDisplayAdded(int displayId) {
            Log.i(TAG, "onDisplayAdded: displayId = " + displayId);
        }

        @Override
        public void onDisplayRemoved(int displayId) {
            Log.i(TAG, "onDisplayRemoved: displayId = " + displayId);
        }

        @Override
        public void onDisplayChanged(int displayId) {
            Log.i(TAG, "onDisplayChanged: displayId = " + displayId);
            if (mDisplayManager == null) {
                return;
            }

            Display display = mDisplayManager.getDisplay(displayId);
            if (display == null) {
                return;
            }

            try {
                Class<Display> displayClass = Display.class;
                @SuppressLint({"SoonBlockedPrivateApi", "DiscouragedPrivateApi"})
                Method getTypeMethod = displayClass.getDeclaredMethod("getType");
                getTypeMethod.setAccessible(true);
                int type = (int) getTypeMethod.invoke(display);
                Log.i(TAG, "onDisplayChanged: display type is " + type);
                if (type != 5) {
                    return;
                }

                @SuppressLint({"SoonBlockedPrivateApi", "DiscouragedPrivateApi"})
                Method getOwnerPackageNameMethod = displayClass.getDeclaredMethod("getOwnerPackageName");
                getOwnerPackageNameMethod.setAccessible(true);
                String ownerPackageName = (String) getOwnerPackageNameMethod.invoke(display);
                Log.i(TAG, "onDisplayChanged: owner package name is " + ownerPackageName);
                Log.i(TAG, "onDisplayChanged: display is owned by us: " + ownerPackageName.equals(mAppPackageName));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    private Button mStartRecordBtn, mStopRecordBtn;
    private final String[] mPermissions = new String[] {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleRegistry = new LifecycleRegistry(mLifecycleOwner);
        mAppPackageName = getPackageName();
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        checkPermission();

        registerScreenRecord();

        if (RomUtils.INSTANCE.isOnePlus()) {
            Log.i(TAG, "Current device brand is onePlus");
        }

        setContentView(R.layout.activity_main_3);

        mStartRecordBtn = findViewById(R.id.start_record_btn);
        mStartRecordBtn.setOnClickListener(v -> {
            startRecord();
            mStartRecordBtn.setClickable(false);
            mStopRecordBtn.setClickable(true);
        });

        mStopRecordBtn = findViewById(R.id.stop_record_btn);
        mStopRecordBtn.setOnClickListener(v -> {
            stopRecord();
            mStartRecordBtn.setClickable(true);
            mStopRecordBtn.setClickable(false);
        });
    }

    private void checkPermission() {
        int checkPermission = 0;
        for (String permission : mPermissions) {
            checkPermission += ContextCompat.checkSelfPermission(this, permission);
        }

        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, mPermissions, 123);
        }
    }

    private void startRecord() {
        Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, REQUEST_CODE);
    }

    private void stopRecord() {
        stopService(new Intent(this, RecordScreenService.class));
    }

    public void registerScreenRecord() {
        mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        if (mDisplayManager == null) {
            return;
        }

        mDisplayManager.registerDisplayListener(mDisplayListener, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                Toast.makeText(this, "允许录屏", Toast.LENGTH_SHORT).show();

                Intent service = new Intent(this, RecordScreenService.class);
                service.putExtra("resultCode", resultCode);
                service.putExtra("data", data);
                startService(service);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "拒绝录屏", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        mDisplayManager.unregisterDisplayListener(mDisplayListener);
    }
}
