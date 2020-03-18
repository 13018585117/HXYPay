package com.hxypay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.ProfitDayListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.pulltorefresh.BaseRefreshListener;
import com.hxypay.pulltorefresh.PullToRefreshLayout;
import com.hxypay.response.ProfitDayRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.List;

public class ProfitDayActivity extends BaseActivity {

    private ListView mDataList;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProfitDayListAdapter mAdapter;
    private List<ProfitDayRes.Info1> mData = new ArrayList<ProfitDayRes.Info1>();

    private int currPageNum = 0;
    private boolean isHaveDataFlag = false;//代表有数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_day);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "每日收益", 0,null);
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.layout_pulltorefresh);
        mDataList = (ListView) findViewById(R.id.list_data);
        mAdapter = new ProfitDayListAdapter(context, mData);
        mDataList.setAdapter(mAdapter);

        mDataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        loadData();
    }

    private void loadData() {
        mPullToRefreshLayout.autoRefresh();
        mPullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mPullToRefreshLayout.finishRefresh();
//                    }
//                }, 2000);
                currPageNum = 0;
                isHaveDataFlag = false;
                mData.clear();
                getProfit(currPageNum + "");
            }

            @Override
            public void loadMore() {
                if (isHaveDataFlag) {
                    mPullToRefreshLayout.finishLoadMore();
                    IToast.getIToast().showIToast("已经没有数据了");
                    return;
                }
                currPageNum++;
                getProfit(currPageNum + "");
            }
        });
    }

    public void getProfit(String pageNum) {
        OkHttpUtils.post()
                .url(Constants.MERCHANT_PROFIT_DAY)
                .headers(PublicParam.getParam())
                .params(Params.profitDayParams(getIntent().getExtras().getString("day"), pageNum))
                .build().execute(new GenericsCallback<ProfitDayRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                mPullToRefreshLayout.finishRefresh();
            }

            @Override
            public void onResponse(ProfitDayRes response, int id) {
                mPullToRefreshLayout.finishRefresh();
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData() != null && response.getData().size() > 0) {//有数据
                            mData.addAll(response.getData());
                            mAdapter.setData(mData);
                            if (response.getData() == null) {
                                isHaveDataFlag = true;//没有数据了
                            }
                            mPullToRefreshLayout.finishLoadMore();
                        } else {//没有数据
                            if (currPageNum == 0) {
                                mPullToRefreshLayout.finishLoadMore();
                            } else {
                                if (response.getData() == null) {
                                    mPullToRefreshLayout.finishLoadMore();
                                    isHaveDataFlag = true;//没有数据了
                                }
                            }
                        }
                    } else {
                        mPullToRefreshLayout.finishLoadMore();
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
