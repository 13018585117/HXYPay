package com.hxypay.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by loptop on 2017/4/8.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    //fragment列表  
    private List<Fragment> list_fragment;
    //tab名的列表
    private List<String> list_Title;

    public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
        super(fm);
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
    }


//    public void setBuy(String coidId){
//        FBTradeBuyFragment f = (FBTradeBuyFragment) super.instantiateItem(container, position);
//    }
//
//    public  void setSell(String coidId){
//
//    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_Title.size();
    }

    //显示tab上的字
    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position);
    }
}
