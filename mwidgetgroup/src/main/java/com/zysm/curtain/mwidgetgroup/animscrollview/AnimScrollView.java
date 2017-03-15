package com.zysm.curtain.mwidgetgroup.animscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.zysm.curtain.mwidgetgroup.minterface.DiscrollInterface;

/**
 * Time:2017/3/14 17:26
 * Created by Curtain.
 */

public class AnimScrollView extends ScrollView {

    private AnimLinearLayout mContent;

    public AnimScrollView(Context context) {
        super(context);
    }

    public AnimScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View content = getChildAt(0);//拿到自定义的LinearLayout
        mContent = (AnimLinearLayout) content;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View first = mContent.getChildAt(0);
        first.getLayoutParams().height = getHeight();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int scrollviewHeight = getHeight();
        //控制动画执行
        //拿到LinearLayout里面每一个子控件，控制其动画
        //动画的执行百分比来控制每一个子控件的执行百分比
        for(int i = 0 ; i < mContent.getChildCount();++i){
            View child = mContent.getChildAt(i);
            if(!(child instanceof DiscrollInterface)) continue;

            DiscrollInterface discrollInterface = (DiscrollInterface) child;
            //child离parent顶部的高度
            int childTop = child.getTop();
            //什么时候执行动画呢，当动画滑进屏幕的时候
            int childHeight = child.getHeight();
            //t:就是滑出去的高度
            //child离屏幕顶部的高度
            int absoluteTop = (childTop -t);
            if(absoluteTop <= scrollviewHeight){
                //child浮现的高度 = ScrollView的高度 - child离屏幕顶部的高度
                int visibleGap = scrollviewHeight -absoluteTop;
                //float ratio = child浮现的高度/child的高度
                float ratio = visibleGap/(float)childHeight;
                //确保ratio是在0~1,
                discrollInterface.onDiscroll(clamp(ratio,1f,0f));
            }else{
                discrollInterface.onResetDiscroll();
            }
        }
    }

    //求三个住中间大小的一个数
    public static float clamp(float value , float max, float min){
        return Math.max(Math.min(value,max),min);
    }
}
