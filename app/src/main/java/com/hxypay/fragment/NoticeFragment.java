package com.hxypay.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.R;
import com.hxypay.adapter.NoticeListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.response.NoticeRes;
import com.hxypay.ui.H5Activity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.List;


public class NoticeFragment extends BaseFragment {

    private ListView mListView;
    private NoticeListAdapter mAdapter;
    private List<NoticeRes.Info1> mData = new ArrayList<NoticeRes.Info1>();

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initViews() {
        mListView = (ListView) findViewById(R.id.list_data);

        mAdapter = new NoticeListAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(getActivity(),H5Activity.class);
                mIntent.putExtra("title","公告详情");
                mIntent.putExtra("h5",mData.get(position).getContent());
                startActivity(mIntent);
            }
        });

        initData();
    }

    private void initData(){
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.NOTICE)
                .headers(PublicParam.getParam())
                .params(Params.newsOrNotice())
                .build().execute(new GenericsCallback<NoticeRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(NoticeRes response, int id) {
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
