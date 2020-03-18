package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.TabFragmentPagerAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.dialogs.TiShiDialog;
import com.hxypay.fragment.CreditCardFragment;
import com.hxypay.fragment.DepositCardFragment;
import com.hxypay.response.MerchantInfoRes;
import com.hxypay.tab.OTabHost;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.List;

public class CardBagActivity extends BaseActivity {

    private TabLayout mViewpagerTab;
    private ViewPager mNewsViewpager;
    private CreditCardFragment f1;
    private DepositCardFragment f2;
    private TiShiDialog shimingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardbag);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        merchantInfo();
    }

    private void initView() {
        initHeadView(this, 0, "卡包", R.drawable.add,null);
        initFragmentView();
    }

    @Override
    public void onRightListener() {
        super.onRightListener();
        mRightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(context, CardAddOcrActivity.class);
                startActivity(mIntent);
            }
        });
    }

    private void initFragmentView() {
        //找到控件
        mViewpagerTab = findViewById(R.id.home_viewpager_tab);
        mNewsViewpager = findViewById(R.id.home_viewpager);
        //fragment列表  
        List<Fragment> list_fragment = new ArrayList<>();
        //tab名的列表
        List<String> list_Title = new ArrayList<>();
        f1 = new CreditCardFragment();
        f2 = new DepositCardFragment();
        list_fragment.add(f1);
        list_fragment.add(f2);

        list_Title.add(" 信用卡");
        list_Title.add("储蓄卡");

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

        mViewpagerTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    initHeadView(CardBagActivity.this, 0, "卡包", R.drawable.add,null);
                }
                if (tab.getPosition() == 1) {
                    initHeadView(CardBagActivity.this, 0, "卡包", 0,null);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void merchantInfo() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.MERCHANT_INFO)
                .headers(PublicParam.getParam())
                .params(Params.merchantInfoParams())
                .build().execute(new GenericsCallback<MerchantInfoRes>(new JsonGenericsSerializator()) {

            private String userCurrLevelId;
            private String memState;

            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                memState = "2";
                dismissDialog();
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(MerchantInfoRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        memState = response.getData().getIsReal();
                        userCurrLevelId = response.getData().getType_id();
                        if (memState.equals("0")){
                            isShiMing();
                        }
                    } else {
                        memState = "2";
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    memState = "2";
                }
            }
        });
    }


    private void isShiMing() {
        if (shimingDialog == null) {
            shimingDialog = new TiShiDialog(CardBagActivity.this, "温馨提示", "商户未认证，请实名认证\n请先进行实名", "前往实名", "取消",false);
        }
        shimingDialog.setClickListener(new TiShiDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                shimingDialog.cancel();
                mIntent = new Intent(context, RealNameOCRActivity.class);
                startActivity(mIntent);
            }

            @Override
            public void doCancel() {
                OTabHost.mTabHost.setCurrentTab(0);
                if(shimingDialog!=null&&shimingDialog.isShowing()) {
                    shimingDialog.cancel();
                }
            }
        });
        shimingDialog.show();
    }
}
