package com.zysm.curtain.mwidgetgroup.animscrollview;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.zysm.curtain.mwidgetgroup.minterface.DiscrollInterface;

/**
 * Time:2017/3/14 16:34
 * Created by Curtain.
 */

public class AnimFrameLayout extends FrameLayout implements DiscrollInterface{

    //定义很多的自定义属性
    /**
     *
     *
     * */
    private static final int TRANSLATION_FROM_TOP = 0x01;
    private static final int TRANSLATION_FROM_BOTTOM = 0x02;
    private static final int TRANSLATION_FROM_LEFT = 0x04;
    private static final int TRANSLATION_FROM_RIGHT = 0x08;

    //颜色估值器
    private static ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();
    /**
     * 一些自定义的属性
     * */
    private int mDiscrollveFromBgColor;//背景颜色变化开始值
    private int mDiscrollveToBgColor;//背景颜色变化结束值
    private boolean mDiscrollveAlpha;//是否需要透明度动画
    private int mDiscrollveTranslation;//平移值
    private boolean mDiscrollveScaleX;//是否需要x轴缩放
    private boolean mDiscrollveScaleY;//是否需要y轴缩放
    private int mHeight;//本view高度
    private int mWidth;//本view宽度

    public AnimFrameLayout(@NonNull Context context) {
        super(context);
    }

    public AnimFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    @Override
    public void onDiscroll(float ratio) {
        //ratio : 0-1
        if(mDiscrollveAlpha){
            setAlpha(ratio);
        }
        if(mDiscrollveScaleX){
           setScaleX(ratio);
        }
        if(mDiscrollveScaleY){
            setScaleY(ratio);
        }
        //判断平移--int
        //int == fromLeft|fromBottom
        if(isTranslationFrom(TRANSLATION_FROM_BOTTOM)){
            Log.d("TEST","bottom");
            setTranslationY(mHeight*(1-ratio));//height-->0 (0代表原来的位置)
        }
        if(isTranslationFrom(TRANSLATION_FROM_TOP)){
            Log.d("TEST","top");
            setTranslationY(-mHeight*(1-ratio));
        }
        if(isTranslationFrom(TRANSLATION_FROM_LEFT)){
            Log.d("TEST","left");
            setTranslationX(-mWidth*(1-ratio));
        }
        if(isTranslationFrom(TRANSLATION_FROM_RIGHT)){
            Log.d("TEST","right");
            setTranslationX(mWidth*(1-ratio));
        }

        if(mDiscrollveFromBgColor !=-1 && mDiscrollveToBgColor != -1){
            setBackgroundColor((int)sArgbEvaluator.evaluate(ratio,mDiscrollveFromBgColor,mDiscrollveToBgColor));
        }
    }

    private boolean isTranslationFrom(int translationMask){

        if(mDiscrollveTranslation == -1){
            return false;
        }
        return (mDiscrollveTranslation & translationMask) == translationMask;
    }

    @Override
    public void onResetDiscroll() {
        //ratio : 0-1
        if(mDiscrollveAlpha){
            setAlpha(0);
        }
        if(mDiscrollveScaleX){
            setScaleX(0);
        }
        if(mDiscrollveScaleY){
            setScaleY(0);
        }
        //判断平移--int
        //int == fromLeft|fromBottom
        if(isTranslationFrom(TRANSLATION_FROM_BOTTOM)){
            setTranslationY(mHeight);//height-->0 (0代表原来的位置)
        }
        if(isTranslationFrom(TRANSLATION_FROM_TOP)){
            setTranslationY(-mHeight);
        }
        if(isTranslationFrom(TRANSLATION_FROM_LEFT)){
            setTranslationX(-mWidth);
        }
        if(isTranslationFrom(TRANSLATION_FROM_RIGHT)){
            setTranslationX(mWidth);
        }
    }

    public int getmDiscrollveFromBgColor() {
        return mDiscrollveFromBgColor;
    }

    public void setmDiscrollveFromBgColor(int mDiscrollveFromBgColor) {
        this.mDiscrollveFromBgColor = mDiscrollveFromBgColor;
    }

    public int getmDiscrollveToBgColor() {
        return mDiscrollveToBgColor;
    }

    public void setmDiscrollveToBgColor(int mDiscrollveToBgColor) {
        this.mDiscrollveToBgColor = mDiscrollveToBgColor;
    }

    public boolean ismDiscrollveAlpha() {
        return mDiscrollveAlpha;
    }

    public void setmDiscrollveAlpha(boolean mDiscrollveAlpha) {
        this.mDiscrollveAlpha = mDiscrollveAlpha;
    }

    public int getmDiscrollveTranslation() {
        return mDiscrollveTranslation;
    }

    public void setmDiscrollveTranslation(int mDiscrollveTranslation) {
        this.mDiscrollveTranslation = mDiscrollveTranslation;
    }

    public boolean ismDiscrollveScaleX() {
        return mDiscrollveScaleX;
    }

    public void setmDiscrollveScaleX(boolean mDiscrollveScaleX) {
        this.mDiscrollveScaleX = mDiscrollveScaleX;
    }

    public boolean ismDiscrollveScaleY() {
        return mDiscrollveScaleY;
    }

    public void setmDiscrollveScaleY(boolean mDiscrollveScaleY) {
        this.mDiscrollveScaleY = mDiscrollveScaleY;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }
}
