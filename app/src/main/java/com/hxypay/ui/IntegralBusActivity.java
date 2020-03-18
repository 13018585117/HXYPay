package com.hxypay.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.App;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.BusCardListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.pulltorefresh.view.HeadView;
import com.hxypay.response.BusCardListInfoRes;
import com.hxypay.response.MerchantInfoRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntegralBusActivity extends BaseActivity {

    private RecyclerView rv_bus;
    private List<BusCardListInfoRes.Data> data;
    private BusCardListAdapter busCardListAdapter;
    private SwipeRefreshLayout srl_bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_bus);
        initView();
        requestCardLists();
    }

    private void requestCardLists() {
        showDialog("加载中...");
        OkHttpUtils.get()
                .url(Constants.busCardList)
                .headers(PublicParam.getParam())
                .params(Params.merchantInfoParams())
                .build().execute(new GenericsCallback<BusCardListInfoRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                dismissDialog();
                srl_bus.setRefreshing(false);
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(BusCardListInfoRes response, int id) {
                dismissDialog();
                srl_bus.setRefreshing(false);
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        data = response.getData();
                        busCardListAdapter.setData(data);
                    } else {

                    }
                } else {
                    IToast.getIToast().showIToast(response.getMsg());
                }
            }
        });
    }

    private void initView() {
        ButterKnife.bind(this);
        initHeadView(this, R.drawable.icon_back_r, "积分巴士", 0,"订单列表");
        srl_bus = findViewById(R.id.srl_bus);
        rv_bus = findViewById(R.id.rv_bus);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        rv_bus.setLayoutManager(gridLayoutManager);
        busCardListAdapter = new BusCardListAdapter(data);
        rv_bus.setAdapter(busCardListAdapter);

        srl_bus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestCardLists();
            }
        });
    }


    @OnClick(R.id.tv_right_img)
    public void tv_Right(){
        mIntent = new Intent(this,BusOrderFormActivity.class);
        startActivity(mIntent);
    }
}
