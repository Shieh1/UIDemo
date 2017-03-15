package com.zysm.curtain.mwidgetgroup.util;

import android.util.Log;

/**
 * Time:2017/3/15 13:40
 * Created by Curtain.
 */

public class LogUtils {

    public static boolean isDebug = true;

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }

    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }


    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

}
