package com.hxypay.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.IToast;
import com.hxypay.response.BannerRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MoreActivity extends BaseActivity {

    private MZBannerView mMZBanner;
    private List<BannerRes.Info1> bannerListUrl = new ArrayList<BannerRes.Info1>();

    private LinearLayout mCreditLayout, mDkLayout, mCardCpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        initView();
        bannerImg();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "更多", 0,null);
        mMZBanner = (MZBannerView) findViewById(R.id.banner);

        mCreditLayout = (LinearLayout) findViewById(R.id.credit_layout);
        mDkLayout = (LinearLayout) findViewById(R.id.dk_layout);
        mCardCpLayout = (LinearLayout) findViewById(R.id.card_cp_layout);


        mCreditLayout.setOnClickListener(this);
        mDkLayout.setOnClickListener(this);
        mCardCpLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.credit_layout:
                String url = mPs.getStringValue(Constants.XYK_URL);
                if (TextUtils.isEmpty(url)) {
                    IToast.getIToast().showIToast("暂无链接");
                    return;
                }
                mIntent = new Intent(context, QQWBViewActivity.class);
                mIntent.putExtra("title", "信用卡申请");
                mIntent.putExtra("url", url);
                startActivity(mIntent);
                break;
            case R.id.dk_layout:
                IToast.getIToast().showIToast("暂无链接");
                break;
            case R.id.card_cp_layout:
                mIntent = new Intent(context,CardTestOCRActivity.class);
                startActivity(mIntent);
                break;
        }
    }


    private void bannerImg() {
        OkHttpUtils.post()
                .url(Constants.MORE_BANNER)
                .headers(PublicParam.getParam())
                .params(Params.banner())
                .build().execute(new GenericsCallback<BannerRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(BannerRes response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        bannerListUrl.clear();
                        bannerListUrl = response.getData();
                        if (bannerListUrl != null && bannerListUrl.size() > 0) {
                            showBanner(bannerListUrl);
                        }
                    }else{
                        GoLogin.goLogin(response.getCode());
                    }
                }
            }
        });
    }


    private void showBanner(List<BannerRes.Info1> bannerListUrl) {
        // 设置数据
        mMZBanner.setPages(bannerListUrl, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        mMZBanner.start();
    }


    public class BannerViewHolder implements MZViewHolder<BannerRes.Info1> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, BannerRes.Info1 data) {
            // 数据绑定
            Glide.with(context).load(data.getImg()).into(mImageView);
        }
    }

}
