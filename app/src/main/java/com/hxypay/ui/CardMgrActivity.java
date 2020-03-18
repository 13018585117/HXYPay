package com.hxypay.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.customview.CardSettingPop;
import com.hxypay.customview.IToast;
import com.hxypay.customview.TipDialog;
import com.hxypay.messageEvents.MessageEvent;
import com.hxypay.response.CreditRes;
import com.hxypay.response.DepositRes;
import com.hxypay.response.PublicRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardMgrActivity extends BaseActivity {

    private List<CreditRes.Info1> mData = new ArrayList<CreditRes.Info1>();
    private MZBannerView mMZBanner;
    private CreditRes.Info1 card;

    private LinearLayout mRepaymentLayout, mTradeLayout, mRechargeLayout, mCardSettingLayot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_mgr);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        card.setType(event.getType());
    };

    private void initView() {
        card = (CreditRes.Info1) getIntent().getSerializableExtra("card");
        initHeadView(this, R.drawable.icon_back_r, card.getBankName(), 0,null);
        mMZBanner = (MZBannerView) findViewById(R.id.banner);

        mRepaymentLayout = (LinearLayout) findViewById(R.id.repayment_layout);
        mTradeLayout = (LinearLayout) findViewById(R.id.trade_layout);
        mRechargeLayout = (LinearLayout) findViewById(R.id.recharge_layout);
        mCardSettingLayot = (LinearLayout) findViewById(R.id.cardsetting_layout);

        showBanner();

        mRepaymentLayout.setOnClickListener(this);
        mTradeLayout.setOnClickListener(this);
        mRechargeLayout.setOnClickListener(this);
        mCardSettingLayot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.repayment_layout:
                if(card.getType().equals("1")){ //1：还款中 0：可设置还款
                    IToast.getIToast().showIToast("正在还款中...");
                }
                if(card.getType().equals("0")){ //1：还款中 0：可设置还款
                    mIntent = new Intent(context, HKChannelActivity.class);
                    mIntent.putExtra("card", card);
                    startActivity(mIntent);
                }
                break;
            case R.id.trade_layout:
                mIntent = new Intent(context, TradeBillActivity.class);
                mIntent.putExtra("cards", (Serializable) mData);
                startActivity(mIntent);
                break;
            case R.id.recharge_layout:
                depositCardData();
                break;
            case R.id.cardsetting_layout:
                CardSettingPop mCardSettingPop = new CardSettingPop(context, new CardSettingPop.ICardSettingListener() {
                    @Override
                    public void updateCard(CardSettingPop dialog) {
                        dialog.dismiss();
                        Intent mIntent = new Intent(context, CardUpdateActivity.class);
                        mIntent.putExtra("card", card);
                        startActivity(mIntent);
                    }

                    @Override
                    public void deleteCard(CardSettingPop dialog) {
                        dialog.dismiss();
                        delCardTipDilog(card.getAccount());
                    }
                });
                mCardSettingPop.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    //获取储蓄卡传过去
    private void depositCardData() {
        OkHttpUtils.post()
                .url(Constants.DEPOSIT_CARD_LIST)
                .headers(PublicParam.getParam())
                .params(Params.myMerchantParams())
                .build().execute(new GenericsCallback<DepositRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(DepositRes response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if(response.getData()!=null&&response.getData().size()>0){
                            Intent mIntent = new Intent(context, FastPayActivity.class);
                            mIntent.putExtra("creditCard", card);
                            mIntent.putExtra("depositCard", response.getData().get(0));
                            startActivity(mIntent);
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

    private void delCardTipDilog(String cardNo){
        TipDialog tipDialog = new TipDialog(context, "提示", "您确定删除卡片：" + cardNo, new String[]{"取消", "确认"}, new TipDialog.OnCustomDialogListener() {
            @Override
            public void ok(TipDialog dialog) {
                dialog.dismiss();
                deleteCard();
            }

            @Override
            public void cancle(TipDialog dialog) {
                dialog.dismiss();
            }
        });
        tipDialog.show();
    }

    private void deleteCard(){
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.DEL_CARD)
                .headers(PublicParam.getParam())
                .params(Params.delCardParams(card.getId()))
                .build().execute(new GenericsCallback<PublicRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(PublicRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        IToast.getIToast().showIToast("删除成功");
                        finish();
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

    private void showBanner() {
        mMZBanner.setIndicatorVisible(false);
        mMZBanner.setCanLoop(false);
        mData.clear();
        mData.add(card);
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
            String cardNo = datas.getAccount();
            getItemBankNo.setText(" ****    ****    ****    " + cardNo.substring(cardNo.length() - 4, cardNo.length()));
            itemUpdate.setVisibility(View.GONE);
        }
    }
}
