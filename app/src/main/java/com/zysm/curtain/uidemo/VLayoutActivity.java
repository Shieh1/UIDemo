package com.zysm.curtain.uidemo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.zysm.curtain.mwidgetgroup.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VLayoutActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlayout);
        ButterKnife.bind(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        //设置固定大小
        recyclerView.setHasFixedSize(true);
        //创建线性布局
        mLayoutManager = new LinearLayoutManager(this);
        //垂直方向
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
//        mLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        //给recyclerview设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MRecyclerViewAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        LogUtils.d("refreshing....");
    }
}
