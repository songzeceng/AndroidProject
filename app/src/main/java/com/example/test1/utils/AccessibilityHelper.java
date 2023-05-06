package com.example.test1.utils;

import android.view.View;

import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

public class AccessibilityHelper {
    public static void setAccessibleDelegate(View view, String desc) {
        if (view == null || desc == null) {
            return;
        }

        ViewCompat.setAccessibilityDelegate(view, new AccessibilityDelegateCompat() {
            @Override
            public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                if (info == null) {
                    return;
                }
                info.setContentDescription(desc);
                info.setClassName(view.getClass().getName());
            }
        });
    }
}
