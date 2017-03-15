package com.zysm.curtain.mwidgetgroup.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Time:2017/3/15 13:41
 * Created by Curtain.
 */

public class ToastUtils {
    public static void showLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    }

}
