package com.hxypay.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.R;
import com.hxypay.adapter.ChargeTxBillListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.pulltorefresh.BaseRefreshListener;
import com.hxypay.pulltorefresh.PullToRefreshLayout;
import com.hxypay.response.ChargeTxBillRes;
import com.hxypay.ui.BillDetailActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.List;


public class ChargeTxBillFragment extends BaseFragment {

    private ListView mListView;
    private ChargeTxBillListAdapter mAdapter;
    private List<ChargeTxBillRes.Info1> mData = new ArrayList<ChargeTxBillRes.Info1>();
    private PullToRefreshLayout mPullToRefreshLayout;

    private int currPageNum = 0;
    private boolean isHaveDataFlag = false;//代表有数据


    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_bills;
    }

    @Override
    protected void initViews() {
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.layout_pulltorefresh);
        mListView = (ListView) findViewById(R.id.list_data);

        mAdapter = new ChargeTxBillListAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(getActivity(), BillDetailActivity.class);
                mIntent.putExtra("type", "2");//充值
                mIntent.putExtra("bills", mData.get(position));
                startActivity(mIntent);
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
                getHKBills(currPageNum + "");
            }

            @Override
            public void loadMore() {
                if (isHaveDataFlag) {
                    mPullToRefreshLayout.finishLoadMore();
                    IToast.getIToast().showIToast("已经没有数据了");
                    return;
                }
                currPageNum++;
                getHKBills(currPageNum + "");
            }
        });
    }

    public void getHKBills(String pageNum) {
        OkHttpUtils.post()
                .url(Constants.CHARGE_TX_BILLS)
                .headers(PublicParam.getParam())
                .params(Params.chargeTxBill("", pageNum))
                .build().execute(new GenericsCallback<ChargeTxBillRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                mPullToRefreshLayout.finishRefresh();
            }

            @Override
            public void onResponse(ChargeTxBillRes response, int id) {
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
