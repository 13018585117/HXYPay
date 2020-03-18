package com.hxypay.ui;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.widget.TextView;

import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.TabFragmentPagerAdapter;
import com.hxypay.fragment.ActivationFragment;
import com.hxypay.fragment.NotactivationFragment;
import com.hxypay.fragment.SeniorSVIPFragment;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends BaseActivity {

    private TabLayout mViewpagerTab;
    private ViewPager mNewsViewpager;

    public static TextView mNumberText,mActivationText,mCodeNumberText;

    private ActivationFragment f1;
    private NotactivationFragment f2;
    private SeniorSVIPFragment f3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        initView();
    }

    private void initView(){
        initHeadView(this,R.drawable.icon_back_r,"好友列表",0,null);
     /*   mNumberText = (TextView)findViewById(R.id.number_text);
        mActivationText = (TextView)findViewById(R.id.activation_text);
        mCodeNumberText = (TextView)findViewById(R.id.code_number_text);*/
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
        f1 = new ActivationFragment();
        f2 = new NotactivationFragment();
        f3 = new SeniorSVIPFragment();
        list_fragment.add(f1);
        list_fragment.add(f2);
        list_fragment.add(f3);
        list_Title.add("用户");
        list_Title.add("会员");
        list_Title.add("合伙人");

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
