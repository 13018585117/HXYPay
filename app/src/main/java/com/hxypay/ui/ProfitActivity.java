package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.ProfitListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.pulltorefresh.BaseRefreshListener;
import com.hxypay.pulltorefresh.PullToRefreshLayout;
import com.hxypay.response.ProfitRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.NumAnim;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProfitActivity extends BaseActivity {

    private ListView mDataList;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProfitListAdapter mAdapter;
    private List<ProfitRes.Info1> mData = new ArrayList<ProfitRes.Info1>();

    private int currPageNum = 0;
    private boolean isHaveDataFlag = false;//代表有数据

    private TextView mKtxText, mZsyText, mYtxText;//可提现，总收益，已提现
    private Button mTxBtn;//提现

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "我的收益", 0,null);
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.layout_pulltorefresh);
        mDataList = (ListView) findViewById(R.id.list_data);
        mAdapter = new ProfitListAdapter(context, mData);
        mDataList.setAdapter(mAdapter);

        mKtxText = (TextView) findViewById(R.id.ktx_tx);
        mZsyText = (TextView) findViewById(R.id.zsy_tx);
        mYtxText = (TextView) findViewById(R.id.ytx_tx);
        mTxBtn = (Button) findViewById(R.id.tx_bt);
        mTxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ktxMoney = mKtxText.getText().toString();
                if (TextUtils.isEmpty(ktxMoney)) {
                    IToast.getIToast().showIToast("暂无可提现金额");
                    return;
                }
                if (new BigDecimal(ktxMoney).compareTo(new BigDecimal(100)) == -1) {
                    IToast.getIToast().showIToast("可提现金额需超过100元");
                    return;
                }
                mIntent = new Intent(context, TXActivity.class);
                mIntent.putExtra("ktxMoney", mKtxText.getText().toString());
                startActivity(mIntent);
            }
        });

        mDataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIntent = new Intent(context, ProfitDayActivity.class);
                mIntent.putExtra("day", mData.get(position).getDays());
                startActivity(mIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
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
                .url(Constants.MERCHANT_PROFIT)
                .headers(PublicParam.getParam())
                .params(Params.profitParams(pageNum))
                .build().execute(new GenericsCallback<ProfitRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                mPullToRefreshLayout.finishRefresh();
            }

            @Override
            public void onResponse(ProfitRes response, int id) {
                mPullToRefreshLayout.finishRefresh();
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
//                        Toast.makeText(context, "进来了"+response.getCode(), Toast.LENGTH_SHORT).show();
                        try {
                            NumAnim.startAnim(mKtxText,Float.parseFloat(response.getNotCash()) );
                            NumAnim.startAnim(mZsyText,Float.parseFloat(response.getDeposit()));
                            NumAnim.startAnim(mYtxText,Float.parseFloat(response.getCash()));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            mKtxText.setText(response.getNotCash());
                            mZsyText.setText(response.getDeposit());
                            mYtxText.setText(response.getCash());
                        }
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
