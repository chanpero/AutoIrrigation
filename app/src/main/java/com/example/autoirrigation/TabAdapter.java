package com.example.autoirrigation;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String[] tabTitle;

    public TabAdapter(FragmentManager fm, List<Fragment> fragments, String[] tabTitle){
        super(fm);
        this.fragments = fragments;
        this.tabTitle = tabTitle;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    //设置tabLayout标题，方法继承自PagerAdapter,默认时返回null, 不重写tab就没字

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
