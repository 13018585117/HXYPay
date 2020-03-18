package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.HKChannelListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.response.ChannelRes;
import com.hxypay.response.CreditRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.List;

public class HKChannelActivity extends BaseActivity {

    private ListView mListView;
    private HKChannelListAdapter mAdapter;
    private List<ChannelRes.Info1> mData = new ArrayList<ChannelRes.Info1>();
    private CreditRes.Info1 card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hk_channel);

        initView();
        initData();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "智能还款", 0,null);
        card = (CreditRes.Info1) getIntent().getSerializableExtra("card");
        mListView = (ListView) findViewById(R.id.list_data);
        mAdapter = new HKChannelListAdapter(context, null);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mData != null && mData.size() > 0) {
                    if(mData.get(position).getState().equals("1")){
                        Intent mIntent = new Intent(context, HKPlainDEActivity.class);
                        mIntent.putExtra("card", card);
                        mIntent.putExtra("channel", mData.get(position));
                        startActivity(mIntent);
                    }
                    if(mData.get(position).getState().equals("2")){
                        Intent mIntent = new Intent(context, HKPlainXEActivity.class);
                        mIntent.putExtra("card", card);
                        mIntent.putExtra("channel", mData.get(position));
                        startActivity(mIntent);
                    }
                }
            }
        });
    }

    private void initData() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.FASTPAY2)
                .headers(PublicParam.getParam())
                .params(Params.channelParams())
                .addParams("passageway","2")
                .build().execute(new GenericsCallback<ChannelRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(ChannelRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        mData.clear();
                        mData = response.getData();
//                        Log.i("智能还款",response.getData().get(0).array);
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
