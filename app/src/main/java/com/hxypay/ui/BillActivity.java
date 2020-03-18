package com.hxypay.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.TabFragmentPagerAdapter;
import com.hxypay.fragment.ChargeTxBillFragment;
import com.hxypay.fragment.HKBillFragment;

import java.util.ArrayList;
import java.util.List;

public class BillActivity extends BaseActivity {

    private TabLayout mViewpagerTab;
    private ViewPager mNewsViewpager;

    private HKBillFragment f1;
    private ChargeTxBillFragment f2;
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        initView();
    }

    private void initView(){
        initHeadView(this,R.drawable.icon_back_r,"账单明细",0,null);
        currentItem = getIntent().getIntExtra("currentItem", 0);
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
        f1 = new HKBillFragment();
        f2 = new ChargeTxBillFragment();
        list_fragment.add(f1);
        list_fragment.add(f2);

        list_Title.add("还款计划");
        list_Title.add("个人收款");

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
        mNewsViewpager.setCurrentItem(currentItem);
    }


}
