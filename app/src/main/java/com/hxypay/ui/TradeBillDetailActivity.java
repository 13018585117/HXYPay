package com.hxypay.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.HKTradeBillDetailListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.pulltorefresh.BaseRefreshListener;
import com.hxypay.pulltorefresh.PullToRefreshLayout;
import com.hxypay.response.HKTradeBillDetailRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.List;

public class TradeBillDetailActivity extends BaseActivity {

    private ListView mDataList;
    private PullToRefreshLayout mPullToRefreshLayout;
    private HKTradeBillDetailListAdapter mAdapter;
    private List<HKTradeBillDetailRes.Info1> mData = new ArrayList<HKTradeBillDetailRes.Info1>();
    private String bankStr;
    private int currPageNum = 0;
    private boolean isHaveDataFlag = false;//代表有数据

    private String plainId, cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_bill_detail);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "详情", 0,null);
        plainId = getIntent().getExtras().getString("plainId");
        cardId = getIntent().getExtras().getString("cardId");
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.layout_pulltorefresh);
        mDataList = (ListView) findViewById(R.id.list_data);
        mAdapter = new HKTradeBillDetailListAdapter(context, bankStr, mData);
        mDataList.setAdapter(mAdapter);

        loadData();
        getHKBills();
    }

    private void loadData() {
        mPullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshLayout.finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
    }

    public void getHKBills() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.HK_TRADE_BILLS_DETAIL)
                .headers(PublicParam.getParam())
                .params(Params.hkBillDetail(plainId, cardId))
                .build().execute(new GenericsCallback<HKTradeBillDetailRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                dismissDialog();
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(HKTradeBillDetailRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData() != null && response.getData().size() > 0) {
                            bankStr = response.getBankName() + " 尾号(" + response.getAccount() + ")";
                            mData.clear();
                            mData = response.getData();
                            mAdapter.setData( bankStr, mData);
                        }
                    } else {
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }


}
