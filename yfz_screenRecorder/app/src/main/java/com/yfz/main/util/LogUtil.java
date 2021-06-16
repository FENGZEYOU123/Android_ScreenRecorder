package com.yfz.main.util;
import android.util.Log;

/**
 * author:Fengze You
 * Time: 9999/01/01
 * describe:control if need to print log information
 */
public class LogUtil {
    //able to print log info when == true
    private static boolean isOpenLog=true;
    public static void d(String tag,String msg ){
        if(isOpenLog) Log.d(tag, ">> "+msg);
    }
    public static void e(String tag,String msg ){ if(isOpenLog) Log.e(tag+" error!", ">> "+msg); }
    public static void i(String tag,String msg ){
        if(isOpenLog) Log.i(tag, ">> "+msg);
    }
    public static void w(String tag,String msg ) { if(isOpenLog) Log.w(tag,">> "+ msg); }
}
