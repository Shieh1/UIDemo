package com.zysm.curtain.mwidgetgroup.dialog.holder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zysm.curtain.mwidgetgroup.R;
import com.zysm.curtain.mwidgetgroup.dialog.BuildBean;
import com.zysm.curtain.mwidgetgroup.dialog.DialogUIUtils;
import com.zysm.curtain.mwidgetgroup.dialog.adapter.TieAdapter;
import com.zysm.curtain.mwidgetgroup.dialog.listener.OnItemClickListener;
import com.zysm.curtain.mwidgetgroup.dialog.utils.ToolUtils;
import com.zysm.curtain.mwidgetgroup.dialog.widget.DividerGridItemDecoration;
import com.zysm.curtain.mwidgetgroup.dialog.widget.DividerItemDecoration;

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
 * 创建日期：2016/11/25 16:54
 * <p/>
 * 描 述：列表holder
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class SheetHolder extends SuperHolder {

    private TextView tvTitle;
    private RecyclerView rView;
    private TextView tvBottom;

    public SheetHolder(Context context) {
        super(context);
    }

    @Override
    protected void findViews() {
        tvTitle = (TextView) rootView.findViewById(R.id.dialogui_tv_title);
        rView = (RecyclerView) rootView.findViewById(R.id.rlv);
        tvBottom = (TextView) rootView.findViewById(R.id.dialogui_tv_bottom);


    }

    @Override
    protected int setLayoutRes() {
        return R.layout.dialogui_holder_sheet;
    }

    @Override
    public void assingDatasAndEvents(final Context context, final BuildBean bean) {
        if (TextUtils.isEmpty(bean.title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(bean.title);
        }
        if (TextUtils.isEmpty(bean.bottomTxt)) {
            tvBottom.setVisibility(View.GONE);
        } else {
            tvBottom.setVisibility(View.VISIBLE);
            tvBottom.setText(bean.bottomTxt);
            tvBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUIUtils.dismiss(bean.dialog, bean.alertDialog);
                    bean.itemListener.onBottomBtnClick();

                }
            });
        }
        if (bean.isVertical) {
            rView.setLayoutManager(new LinearLayoutManager(bean.mContext));
            rView.addItemDecoration(new DividerItemDecoration(bean.mContext, ToolUtils.dip2px(bean.mContext, 8), true, Color.TRANSPARENT, true, true, false));
        } else {
            rView.setLayoutManager(new GridLayoutManager(bean.mContext, bean.gridColumns));// 布局管理器。
            rView.addItemDecoration(new DividerGridItemDecoration(bean.mContext, bean.gridColumns));
        }
        rView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        rView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        if (bean.mAdapter == null) {
            TieAdapter adapter = new TieAdapter(bean.mContext, bean.mLists);
            bean.mAdapter = adapter;
        }
        rView.setAdapter(bean.mAdapter);
        bean.mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                DialogUIUtils.dismiss(bean.dialog, bean.alertDialog);
                bean.itemListener.onItemClick(bean.mLists.get(position).getTitle(), position);
            }
        });
    }


}
