package com.hxypay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.MoreZBListAdapter;
import com.hxypay.bean.MoreZBBean;
import com.hxypay.customview.IToast;

import java.util.ArrayList;
import java.util.List;

public class MoreZBActivity extends BaseActivity {

    private ListView mListView;
    private MoreZBListAdapter mAdapter;
    private List<MoreZBBean> mData = new ArrayList<MoreZBBean>();

    private int imgId[] = {R.drawable.mb, R.drawable.lj};

    private String desc[] = {"名表名匠", "我的老家"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_zb);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "好信用企业", 0,null);
        mListView = (ListView) findViewById(R.id.list_data);
        mAdapter = new MoreZBListAdapter(context, initData());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IToast.getIToast().showIToast("暂无链接");
            }
        });

    }

    private List<MoreZBBean> initData() {
        mData.clear();
        MoreZBBean mb;
        for (int i = 0; i < imgId.length; i++) {
            mb = new MoreZBBean();
            mb.setImgId(imgId[i]);
            mb.setDesc(desc[i]);
            mData.add(mb);
        }
        return mData;
    }

}
