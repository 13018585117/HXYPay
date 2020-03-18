package com.hxypay.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.R;
import com.hxypay.adapter.DepositListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.response.DepositRes;
import com.hxypay.ui.UpdateCardOCRActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.List;


public class DepositCardFragment extends BaseFragment {

    private ListView mListView;
    private DepositListAdapter mAdapter;
    private List<DepositRes.Info1> mData = new ArrayList<DepositRes.Info1>();

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_cards;
    }

    @Override
    protected void initViews() {
        mListView = (ListView) findViewById(R.id.list_data);

        mAdapter = new DepositListAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(getActivity(), UpdateCardOCRActivity.class);
                startActivity(mIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.DEPOSIT_CARD_LIST)
                .headers(PublicParam.getParam())
                .params(Params.myMerchantParams())
                .build().execute(new GenericsCallback<DepositRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(DepositRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mData.clear();
                        mData = response.getData();
                        mAdapter.setData(mData);
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
