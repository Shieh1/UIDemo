package com.zysm.curtain.mwidgetgroup.minterface;

/**
 * Time:2017/3/14 16:52
 * Created by Curtain.
 */

public interface DiscrollInterface {

    /**
     * 当滑动时候掉哦那个该方法，用来控制里面控件执行相应的动画
     * @param  ratio : 0-1的范围，动画执行百分比
     * */

    public void onDiscroll(float ratio);

    /**
     * 重置动画，让view所有属性回复原来的值
     * */
    public void onResetDiscroll();
}
