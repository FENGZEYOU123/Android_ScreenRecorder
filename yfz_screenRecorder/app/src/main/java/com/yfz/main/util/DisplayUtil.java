package com.yfz.main.util;

import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import static android.view.View.GONE;

public class DisplayUtil {
    /**
     * 沉浸式弹窗
     * @param dialog
     */
    public static void setFullScreenImmersive(Dialog dialog) {

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions =    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

            dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }else{
            dialog.getWindow().getDecorView().setSystemUiVisibility(GONE);
        }
    }
}
