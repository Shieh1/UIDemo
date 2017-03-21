package com.zysm.curtain.uidemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

public class CoordinatorTabActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.coordinatortablayout)
    CoordinatorTabLayout coordinatortablayout;
    private int[] imageArray,mColorArray ;
    private ArrayList<Fragment> mFragments;
    private final String[] mTitles = {"Android", "iOS", "前端", "拓展资源"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_coordinator_tab);
        ButterKnife.bind(this);
        initFragments();
        initViewPager();
        imageArray = new int[]{
                R.mipmap.test1,
                R.mipmap.test_pic1,
                R.mipmap.test_pic2,
                R.mipmap.test_pic3};
        mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light};

        coordinatortablayout.setTitle("这个是Title")
                .setBackEnable(true)
                .setImageArray(imageArray, mColorArray)
                .setupWithViewPager(viewPager);
    }

    private void initFragments(){
        mFragments = new ArrayList<>();

        for (String title : mTitles) {

            mFragments.add(MainFragment.getInstance(title));

        }
    }

    private void initViewPager(){
        viewPager.setOffscreenPageLimit(4);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
    }


}
