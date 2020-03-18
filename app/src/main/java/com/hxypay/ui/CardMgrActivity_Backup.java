package com.hxypay.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.response.CreditRes;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CardMgrActivity_Backup extends BaseActivity {

    private List<CreditRes.Info1> mData = new ArrayList<CreditRes.Info1>();
    private MZBannerView mMZBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_mgr);

        initView();
    }

    private void initView() {
        initHeadView(this, R.drawable.icon_back_r, "添加卡", 0,null);
        mMZBanner = (MZBannerView) findViewById(R.id.banner);
        mData.clear();
        mData = (ArrayList<CreditRes.Info1>) getIntent().getSerializableExtra("cardList");
        showBanner();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.yxq_tx:
                break;
            case R.id.zdr_tx:
                break;
            case R.id.hkr_tx:

                break;
        }
    }

    private void showBanner() {
        mMZBanner.setIndicatorVisible(false);
        mMZBanner.setCanLoop(false);
        // 设置数据
        mMZBanner.setPages(mData, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
//        mMZBanner.start();
    }


    public class BannerViewHolder implements MZViewHolder<CreditRes.Info1> {
        ImageView img;
        ImageView itemBankLogo;
        ImageView itemBankLogo1;
        TextView itemUpdate;
        TextView itemBankName;
        TextView itemBankType;
        TextView getItemBankNo;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View convertView = LayoutInflater.from(context).inflate(R.layout.activity_cards_mgr_item, null);
            img = (ImageView) convertView.findViewById(R.id.img);
            itemBankLogo = (ImageView) convertView.findViewById(R.id.bank_logo_img);
            itemBankLogo1 = (ImageView) convertView.findViewById(R.id.bank_logo_img1);
            itemUpdate = (TextView) convertView.findViewById(R.id.update_tx);
            itemBankName = (TextView) convertView.findViewById(R.id.bank_name_tx);
            itemBankType = (TextView) convertView.findViewById(R.id.bank_type_tx);
            getItemBankNo = (TextView) convertView.findViewById(R.id.bank_card_tx);
            return convertView;
        }

        @Override
        public void onBind(Context context, int position, CreditRes.Info1 datas) {
            Glide.with(context).load(datas.getBankBack()).into(img);
            Glide.with(context).load(datas.getBankLogo()).into(itemBankLogo);
            Glide.with(context).load(datas.getBankLogo()).into(itemBankLogo1);
            itemBankName.setText(datas.getBankName());
            itemBankType.setText("信用卡");
            getItemBankNo.setText(datas.getAccount());
            itemUpdate.setVisibility(View.GONE);
        }
    }
}
