package com.hxypay.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.pulltorefresh.BaseRefreshListener;
import com.hxypay.pulltorefresh.PullToRefreshLayout;

public class ProfitActivity_Backup extends BaseActivity {

    private ListView mDataList;
    private PullToRefreshLayout mPullToRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        initView();
    }

    private void initView(){
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.layout_pulltorefresh);
        mDataList = (ListView) findViewById(R.id.list_data);

        loadData();
    }

    private void loadData() {
        mPullToRefreshLayout.autoRefresh();
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

}
