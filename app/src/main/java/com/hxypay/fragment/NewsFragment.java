package com.hxypay.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.R;
import com.hxypay.adapter.NewsListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.response.NewsRes;
import com.hxypay.ui.H5Activity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends BaseFragment {

    private ListView mListView;
    private NewsListAdapter mAdapter;
    private List<NewsRes.Info1> mData = new ArrayList<NewsRes.Info1>();

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initViews() {
        mListView = (ListView) findViewById(R.id.list_data);

        mAdapter = new NewsListAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(getActivity(),H5Activity.class);
                mIntent.putExtra("title","资讯详情");
                mIntent.putExtra("h5",mData.get(position).getContent());
                startActivity(mIntent);
            }
        });

        initData();
    }

    private void initData(){
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.NEWS)
                .headers(PublicParam.getParam())
                .params(Params.newsOrNotice())
                .build().execute(new GenericsCallback<NewsRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(NewsRes response, int id) {
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
