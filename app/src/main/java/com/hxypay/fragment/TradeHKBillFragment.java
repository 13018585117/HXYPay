package com.hxypay.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.R;
import com.hxypay.adapter.HKTradeBillListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.customview.TipDialog;
import com.hxypay.messageEvents.MessageEvent;
import com.hxypay.pulltorefresh.BaseRefreshListener;
import com.hxypay.pulltorefresh.PullToRefreshLayout;
import com.hxypay.response.HKTradeBillRes;
import com.hxypay.response.StopPlanRes;
import com.hxypay.ui.TradeBillActivity;
import com.hxypay.ui.TradeBillDetailActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class TradeHKBillFragment extends BaseFragment implements HKTradeBillListAdapter.ICancellPlainListener {

    private ListView mListView;
    private HKTradeBillListAdapter mAdapter;
    private List<HKTradeBillRes.Info1> mData = new ArrayList<HKTradeBillRes.Info1>();
    public  PullToRefreshLayout mPullToRefreshLayout;

    private int currPageNum = 0;
    private boolean isHaveDataFlag = false;//代表有数据


    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_trade_bill;
    }

    @Override
    protected void initViews() {
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.layout_pulltorefresh);
        mListView = (ListView) findViewById(R.id.list_data);

        mAdapter = new HKTradeBillListAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
        mAdapter.setCancellPlainListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(getActivity(), TradeBillDetailActivity.class);
                mIntent.putExtra("plainId", mData.get(position).getId());
                mIntent.putExtra("cardId", TradeBillActivity.card.getId());
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
                mAdapter.notifyDataSetChanged();
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
                .url(Constants.HK_TRADE_BILLS)
                .headers(PublicParam.getParam())
                .params(Params.hkBill1(TradeBillActivity.card.getId(), pageNum))
                .build().execute(new GenericsCallback<HKTradeBillRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                mPullToRefreshLayout.finishRefresh();
            }

            @Override
            public void onResponse(HKTradeBillRes response, int id) {
                mPullToRefreshLayout.finishRefresh();
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getInfo() != null && response.getInfo().size() > 0) {//有数据
                            mData.addAll(response.getInfo());
                            mAdapter.setData(mData);
                            if (response.getInfo() == null) {
                                isHaveDataFlag = true;//没有数据了
                            }
                            mPullToRefreshLayout.finishLoadMore();
                        } else {//没有数据
                            if (currPageNum == 0) {
                                mPullToRefreshLayout.finishLoadMore();
                            } else {
                                if (response.getInfo() == null) {
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

    @Override
    public void cancelPlain(final String planId) {
        TipDialog tipDialog = new TipDialog(getActivity(), "提示", "您确定终止该计划吗？",
                new String[]{"取消", "确认"}, new TipDialog.OnCustomDialogListener() {
            @Override
            public void ok(TipDialog dialog) {
                dialog.dismiss();
                deleteCard(planId);
            }

            @Override
            public void cancle(TipDialog dialog) {
                dialog.dismiss();
            }
        });
        tipDialog.show();
    }

    private void deleteCard(String plainId) {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.STOP_PLAN)
                .headers(PublicParam.getParam())
                .params(Params.stopPlanParams(TradeBillActivity.card.getId(), plainId))
                .build().execute(new GenericsCallback<StopPlanRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(StopPlanRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        IToast.getIToast().showIToast("终止成功");
                        MessageEvent messageEvent = new MessageEvent();
                        messageEvent.setType("0");
                        EventBus.getDefault().post(messageEvent);
                        loadData();
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
