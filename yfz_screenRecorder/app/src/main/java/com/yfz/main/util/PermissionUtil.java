package com.yfz.main.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yfz.main.base.BaseApplication;

/**
 * Author: YouFengZe
 * Time: 9999/01/1
 * Describe: check needed permission util
 */
public class PermissionUtil {
    //need 3 permissions,storage,camera,audio
    private static String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA};

    /**
     * 是否已经获取到录制屏幕所需的3个权限
     * @return hasGotAllPermissions
     */
    public static boolean isGetAllNeededPermission(){
        boolean hasGotAllPermissions = true;
        for(int i=0; i<mPermissionList.length; i++){
            if((ContextCompat.checkSelfPermission(BaseApplication.getInstance().getApplicationContext(),mPermissionList[i]) != PackageManager.PERMISSION_GRANTED)){
                hasGotAllPermissions = false;
            }
        }
        return hasGotAllPermissions;
    }

    /**
     * 请求获取录制屏幕所需的3个权限
     * @param activity 当前activity
     * @param requestCode 定义好的返回码
     */
    public static void getRequestAllPermission(Activity activity,int requestCode){
        if(null != activity) {
            ActivityCompat.requestPermissions(activity, mPermissionList, requestCode);
        }
    }
}
