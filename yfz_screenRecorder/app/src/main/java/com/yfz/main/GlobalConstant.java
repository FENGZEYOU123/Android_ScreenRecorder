package com.yfz.main;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.blankj.utilcode.util.BarUtils;

public final class GlobalConstant {

    private static int DEVICE_WIDTH;
    private static int DEVICE_HEIGHT;
    private static float DEVICE_DENSITY;
    private static int STATUS_BAR_HEIGHT;

    public static void initDeviceInfo(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        DEVICE_WIDTH = metrics.widthPixels;
        DEVICE_HEIGHT = metrics.heightPixels;
        DEVICE_DENSITY = metrics.density;
        STATUS_BAR_HEIGHT = BarUtils.getStatusBarHeight();
    }

    public static int getDeviceWidth() {
        return DEVICE_WIDTH;
    }

    public static int getDeviceHeight() {
        return DEVICE_HEIGHT;
    }

    public static float getDeviceDensity() {
        return DEVICE_DENSITY;
    }

    public static int getStatusBarHeight() {
        return STATUS_BAR_HEIGHT;
    }

}
