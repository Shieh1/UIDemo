package com.zysm.curtain.mwidgetgroup.dialog.listener;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.zysm.curtain.mwidgetgroup.dialog.BuildBean;
import com.zysm.curtain.mwidgetgroup.dialog.TieBean;

import java.util.List;

/**
 * ========================================
 * <p/>
 * 版 权：dou361.com 版权所有 （C） 2015
 * <p/>
 * 作 者：陈冠明
 * <p/>
 * 个人网站：http://www.dou361.com
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期：2016/11/1 15:08
 * <p/>
 * 描 述：构建弹出框样式方法
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public interface Assignable {

    /**
     * 横向加载框
     */
    BuildBean assignLoadingHorizontal(Context context, CharSequence msg, boolean cancleable, boolean outsideTouchable, boolean isWhiteBg);

    /**
     * 竖向加载框
     */
    BuildBean assignLoadingVertical(Context context, CharSequence msg, boolean cancleable, boolean outsideTouchable, boolean isWhiteBg);

    /**
     * 横向加载框
     */
    BuildBean assignMdLoadingHorizontal(Context context, CharSequence msg, boolean cancleable, boolean outsideTouchable, boolean isWhiteBg);

    /**
     * 竖向加载框
     */
    BuildBean assignMdLoadingVertical(Context context, CharSequence msg, boolean cancleable, boolean outsideTouchable, boolean isWhiteBg);

    /**
     * md风格弹出框
     */
    BuildBean assignMdAlert(Activity activity, CharSequence title, CharSequence msg, boolean cancleable, boolean outsideTouchable, final DialogUIListener listener);

    /**
     * md风格多选框
     */
    BuildBean assignMdMultiChoose(Activity context, CharSequence title, final CharSequence[] words, final boolean[] checkedItems, boolean cancleable, boolean outsideTouchable,
                                  final DialogUIListener btnListener);

    /**
     * 单选框
     */
    BuildBean assignSingleChoose(Activity context, CharSequence title, final int defaultChosen, final CharSequence[] words, boolean cancleable, boolean outsideTouchable,
                                 final DialogUIItemListener listener);

    /**
     * 提示弹出框
     */
    BuildBean assignAlert(Context activity, CharSequence title, CharSequence msg, CharSequence hint1, CharSequence hint2,
                          CharSequence firstTxt, CharSequence secondTxt, boolean isVertical, boolean cancleable, boolean outsideTouchable, final DialogUIListener listener);

    /**
     * 中间弹出列表
     */
    BuildBean assignCenterSheet(Context context, List<TieBean> datas, boolean cancleable, boolean outsideTouchable, final DialogUIItemListener listener);

    /**
     * md风格弹出列表
     */
    BuildBean assignMdBottomSheet(Context context, boolean isVertical, CharSequence title, List<TieBean> datas, CharSequence bottomTxt, int columnsNum, boolean cancleable, boolean outsideTouchable, DialogUIItemListener listener);

    /**
     * 自定义弹出框
     */
    BuildBean assignCustomAlert(Context context, View contentView, int gravity, boolean cancleable, boolean outsideTouchable);

    /**
     * 自定义底部弹出框
     */
    BuildBean assignCustomBottomAlert(Context context, View contentView, boolean cancleable, boolean outsideTouchable);


}
