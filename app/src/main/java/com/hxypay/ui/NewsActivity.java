package com.hxypay.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.TabFragmentPagerAdapter;
import com.hxypay.fragment.NewsFragment;
import com.hxypay.fragment.NoticeFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends BaseActivity {

    private TabLayout mViewpagerTab;
    private ViewPager mNewsViewpager;

    private NewsFragment f1;
    private NoticeFragment f2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initView();
    }

    private void initView() {
        initHeadView(this,0,"资讯",0,null);

        initFragmentView();
    }

    private void initFragmentView() {
        //找到控件
        mViewpagerTab = findViewById(R.id.home_viewpager_tab);
        mNewsViewpager = findViewById(R.id.home_viewpager);
        //fragment列表  
        List<Fragment> list_fragment = new ArrayList<>();
        //tab名的列表
        List<String> list_Title = new ArrayList<>();
        f1 = new NewsFragment();
        f2 = new NoticeFragment();
        list_fragment.add(f1);
        list_fragment.add(f2);

        list_Title.add("新闻资讯");
        list_Title.add("公司公告");

        //设置名称
        for (int i = 0; i < list_Title.size(); i++) {
            mViewpagerTab.addTab(mViewpagerTab.newTab().setText(list_Title.get(i)));
        }
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(
                getSupportFragmentManager(), list_fragment, list_Title
        );
        //viewpager 加载adapter
        mNewsViewpager.setAdapter(adapter);
        //TableLayout加载viewpager
        mViewpagerTab.setupWithViewPager(mNewsViewpager);
    }

}
