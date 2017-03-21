package com.zysm.curtain.uidemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Curtain on 2017/3/21.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private String[] mTitles;



    public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> mFragments, String[] mTitles) {

        super(fm);

        this.mFragments = mFragments;

        this.mTitles = mTitles;

    }



    @Override

    public int getCount() {

        return mFragments.size();

    }



    @Override

    public CharSequence getPageTitle(int position) {

        return mTitles[position];

    }



    @Override

    public Fragment getItem(int position) {

        return mFragments.get(position);

    }
}
