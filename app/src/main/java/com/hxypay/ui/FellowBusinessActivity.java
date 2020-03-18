package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.FragmentBusinessExpandableListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.response.FellowBusinessRes;
import com.hxypay.response.PublicRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.io.Serializable;
import java.util.List;

public class FellowBusinessActivity extends BaseActivity {

    private ExpandableListView elv_friends;
    private List<FellowBusinessRes.Inif.Friend> friend;
    private List<FellowBusinessRes.Inif.MonthTotal> monthTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fellow_business);
        initView();
        initData();
    }

    private void initData() {
        requestData();
        elv_friends.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition==0){
                    if (monthTotal!=null){
                        mIntent = new Intent(FellowBusinessActivity.this,FellowBusinessDetailedDataActivity.class);
                        mIntent.putExtra("type",0);  //0为每月交易数据，1位好友交易数据；
                        mIntent.putExtra("data", (Serializable) monthTotal.get(childPosition).getDetail());
                        startActivity(mIntent);
                    }
                }else if (groupPosition == 1){
                    if (friend!=null){
                        mIntent = new Intent(FellowBusinessActivity.this,FellowBusinessDetailedDataActivity.class);
                        mIntent.putExtra("type",1);
                        mIntent.putExtra("data", (Serializable) friend.get(childPosition).getDetail());
                        startActivity(mIntent);
                    }
                }
                return true;
            }
        });
    }

    private void initView() {
        initHeadView(this,R.drawable.icon_back_r,"好友交易数据",0,null);
        elv_friends = findViewById(R.id.elv_friends);
    }

    private void requestData() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.TRANSACTIONDETAILS)
                .headers(PublicParam.getParam())
                .params(Params.exitParams())
                .build().execute(new GenericsCallback<FellowBusinessRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(FellowBusinessRes response, int id) {
                dismissDialog();
                if (response!=null) {
                    if (response.getCode().equals("0000")) {
                        friend = response.getData().getFriend();
                        monthTotal = response.getData().getMonthTotal();
                        FragmentBusinessExpandableListAdapter fragmentBusinessExpandableListAdapter = new FragmentBusinessExpandableListAdapter(FellowBusinessActivity.this, friend, monthTotal);
                        elv_friends.setAdapter(fragmentBusinessExpandableListAdapter);
                        elv_friends.expandGroup(0);
                        elv_friends.expandGroup(1);
                    } else {
                        IToast.getIToast().showIToast("数据异常！！！");
                    }
                }

            }
        });
    }
}
