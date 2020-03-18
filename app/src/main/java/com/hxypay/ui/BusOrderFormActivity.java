package com.hxypay.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.fragment.PayFragment;
import com.hxypay.response.BusOrderFormInfoRes;
import com.hxypay.util.Constants;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusOrderFormActivity extends BaseActivity {

    @BindView(R.id.srl_bus)
    SwipeRefreshLayout srl_bus;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.vp_bus)
    ViewPager vp_bus;

    private String[] tabs ={"全部","审核中","审核成功","审核失败"};
    private String[] urlTitle = {"9", "0", "1", "2"};
    private List<BusOrderFormInfoRes.Data> data1;
    private List<BusOrderFormInfoRes.Data> data2 = new ArrayList<>(); //审核中
    private List<BusOrderFormInfoRes.Data> data3 = new ArrayList<>();  //审核通过
    private List<BusOrderFormInfoRes.Data> data4 = new ArrayList<>();  //审核失败

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_order_form);
        initView();
        requestOrderForm();
    }

    private void initView() {
        ButterKnife.bind(this);
        initHeadView(this, R.drawable.icon_back_r,"订单",0,null);
        for (String tabName :tabs) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabName);
            tabLayout.addTab(tab);
        }
        Madapter madapter = new Madapter(getSupportFragmentManager());
        vp_bus.setAdapter(madapter);
        tabLayout.setupWithViewPager(vp_bus);

        srl_bus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestOrderForm();
            }
        });
    }


    private void requestOrderForm() {
        showDialog("加载中...");
        Map<String, String> map = Params.merchantInfoParams();
        map.put("userId","7bd4d7a6-fd2b-4f21-b429-142e2cb7621e");
        map.put("beginTime","");
        map.put("endTime","");
        map.put("orderCode","");
        OkHttpUtils.get()
                .url(Constants.busOrderForm)
                .headers(PublicParam.getParam())
                .params(map)
                .build().execute(new GenericsCallback<BusOrderFormInfoRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                dismissDialog();
                srl_bus.setRefreshing(false);
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(BusOrderFormInfoRes response, int id) {
                dismissDialog();
                srl_bus.setRefreshing(false);
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        data1 = response.getData();
                        BusOrderFormInfoRes.Data data = data1.get(id);
                        String status = data.getStatus();
                        //0和3是审核中、1：审核通过、2、审核失败
                        if (status.equals("0")|| (status.equals("3"))){
                            data2.add(data);
                        }else if (status.equals("1")){
                            data3.add(data);
                        }else if (status.equals("2")){
                            data4.add(data);
                        }
                    } else if (response.getCode().equals("0004")){
                        //没有订单；
                        IToast.getIToast().showIToast(response.getMsg());
                    }
                } else {
                    IToast.getIToast().showIToast(response.getMsg());
                }
            }
        });
    }



    private class Madapter extends FragmentPagerAdapter{
        public Madapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int i) {
            //创建fragment对象并返回
            Bundle bundle = new Bundle();
            if (i == 0) {
                bundle.putSerializable("data", (Serializable) data1);
            } else if (i == 1){
                bundle.putSerializable("data", (Serializable)data2);
            }else if (i == 2){
                bundle.putSerializable("data", (Serializable)data3);
            }else{
                bundle.putSerializable("data", (Serializable)data4);
            }
            //实例化Fragment
            PayFragment payFragment = new PayFragment();
            payFragment.setArguments(bundle);
            return payFragment;
        }

        @Override
        public int getCount() {
            return tabs==null?0:tabs.length;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (tabs==null) {
                return super.getPageTitle(position);
            }else {
                return tabs[position];
            }
        }
    }
}
