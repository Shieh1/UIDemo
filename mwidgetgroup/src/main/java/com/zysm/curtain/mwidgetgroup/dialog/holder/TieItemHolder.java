package com.zysm.curtain.mwidgetgroup.dialog.holder;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zysm.curtain.mwidgetgroup.R;
import com.zysm.curtain.mwidgetgroup.dialog.TieBean;
import com.zysm.curtain.mwidgetgroup.dialog.listener.OnItemClickListener;


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
 * 创建日期：2016/10/5
 * <p/>
 * 描 述：直播间消息的item
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class TieItemHolder extends SuperItemHolder<TieBean> {


    LinearLayout llBg;
    TextView tvTitle;

    public TieItemHolder(Context mContext, OnItemClickListener listener, View itemView) {
        super(mContext, listener, itemView);
        llBg = (LinearLayout) itemView.findViewById(R.id.ll_bg);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
    }

    @Override
    public void refreshView() {
        TieBean data = getData();
        llBg.setSelected(data.isSelect());
        tvTitle.setText("" + data.getTitle());
    }
}
