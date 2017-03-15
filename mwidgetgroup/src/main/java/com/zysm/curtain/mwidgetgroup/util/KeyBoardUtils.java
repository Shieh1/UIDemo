package com.zysm.curtain.mwidgetgroup.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Time:2017/3/15 11:48
 * Created by Curtain.
 */

public class KeyBoardUtils {

    /** 
     * 打开软键盘 
     * 
     * @param editText 
     * @param context 
     */
     public static void openKeybord(EditText editText, Context context) {
         InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
         imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
         imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
     }

     /** 
      * 关闭软键盘 
      * @param editText 
      * @param context 
      */
     public static void closeKeybord(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
     }
}
