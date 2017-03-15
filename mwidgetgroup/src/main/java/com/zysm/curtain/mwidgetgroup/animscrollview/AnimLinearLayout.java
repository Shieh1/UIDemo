package com.zysm.curtain.mwidgetgroup.animscrollview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zysm.curtain.mwidgetgroup.R;


/**
 * Time:2017/3/14 16:18
 * Created by Curtain.
 */

public class AnimLinearLayout extends LinearLayout {

    public AnimLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //return super.generateLayoutParams(attrs);
        return new AnimLayoutParams(getContext(),attrs);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {

        AnimLayoutParams p = (AnimLayoutParams) params;
        if(!isDiscrollvable(p)){
            super.addView(child,index,params);
        }else{
            AnimFrameLayout af = new AnimFrameLayout(getContext());
            af.setmDiscrollveAlpha(p.mDiscrollveAlpha);
            af.setmDiscrollveFromBgColor(p.mDiscrollveFromBgColor);
            af.setmDiscrollveToBgColor(p.mDiscrollveToBgColor);
            af.setmDiscrollveScaleX(p.mDiscrollveScaleX);
            af.setmDiscrollveScaleY(p.mDiscrollveScaleY);
            af.setmDiscrollveTranslation(p.mDiscrollveTranslation);
            af.addView(child);
            super.addView(af, index, p);
        }

    }

    /**判断是否有自定义属性*/
    private boolean isDiscrollvable(AnimLayoutParams p){
        return p.mDiscrollveAlpha||
                p.mDiscrollveScaleX||
                p.mDiscrollveScaleY||
                p.mDiscrollveTranslation != -1||
                (p.mDiscrollveFromBgColor != -1 &&
                p.mDiscrollveToBgColor != -1);
    }


    public static class AnimLayoutParams extends LinearLayout.LayoutParams{

        public int mDiscrollveFromBgColor;//背景颜色变化开始值
        public int mDiscrollveToBgColor;//背景颜色变化结束值
        public boolean mDiscrollveAlpha;//是否需要透明度动画
        public int mDiscrollveTranslation;//平移值
        public boolean mDiscrollveScaleX;//x轴方向缩放
        public boolean mDiscrollveScaleY;//y轴方向缩放

        public AnimLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            //从child里面拿到我自定义的属性
            TypedArray ty = c.obtainStyledAttributes(attrs, R.styleable.DiscrollView_LayoutParams);
            mDiscrollveFromBgColor = ty.getColor(R.styleable.DiscrollView_LayoutParams_discrollve_fromBgColor,-1);
            mDiscrollveToBgColor = ty.getColor(R.styleable.DiscrollView_LayoutParams_discrollve_toBgColor,-1);
            mDiscrollveAlpha = ty.getBoolean(R.styleable.DiscrollView_LayoutParams_discrollve_alpha,false);
            mDiscrollveTranslation = ty.getInt(R.styleable.DiscrollView_LayoutParams_discrollve_translation,-1);
            mDiscrollveScaleX = ty.getBoolean(R.styleable.DiscrollView_LayoutParams_discrollve_scaleX,false);
            mDiscrollveScaleY = ty.getBoolean(R.styleable.DiscrollView_LayoutParams_discrollve_scaleY,false);
            ty.recycle();
        }
    }

}
