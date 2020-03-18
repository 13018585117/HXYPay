package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.PhotoShow.PhotoViewActivity;
import com.hxypay.R;
import com.hxypay.adapter.CommonAdapter;
import com.hxypay.adapter.GridViewAdapter;
import com.hxypay.adapter.MyViewHolder;
import com.hxypay.adapter.ShareMaterialListAdapter;
import com.hxypay.customview.IToast;
import com.hxypay.response.AboutUsRes;
import com.hxypay.response.ShareMaterialRes;
import com.hxypay.tab.OTabActivity;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.hxypay.view.MyGridView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareMaterialActivity extends BaseActivity {
    @BindView(R.id.lv_tg)
    ListView lv_tg;
    List<ShareMaterialRes.Info1> data =null;
    List<String> imgs =null;
    private ShareMaterialListAdapter info1CommonAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_material);
        ButterKnife.bind(this);
        initHeadView(this,R.drawable.icon_back_r,"素材大全",0,null);
        initView();
        shape_material();
    }

    private void initView() {
        info1CommonAdapter = new ShareMaterialListAdapter(this,data);
        lv_tg.setAdapter(info1CommonAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    private void shape_material() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.POPULARIZE_MATERIAL)
                .headers(PublicParam.getParam())
                .params(Params.shareUrl())
                .build().execute(new GenericsCallback<ShareMaterialRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(ShareMaterialRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData() != null && response.getData().size() > 0) {
                            info1CommonAdapter = new ShareMaterialListAdapter(ShareMaterialActivity.this,response.getData());
                            lv_tg.setAdapter(info1CommonAdapter);
                            info1CommonAdapter.setOnGridViewItenClick(new ShareMaterialListAdapter.ItemListener() {
                                @Override
                                public void onAddClick(List<String> urls,int position) {
                                    mIntent = new Intent(ShareMaterialActivity.this,PhotoViewActivity.class);
                                    mIntent.putExtra("currentPosition",position);
                                    mIntent.putExtra("imgs", (Serializable) urls);
                                    startActivity(mIntent);
                                }
                            });
                        }
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
