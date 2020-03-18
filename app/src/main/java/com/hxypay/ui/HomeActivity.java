package com.hxypay.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.App;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.bean.KeyConfig;
import com.hxypay.customview.IToast;
import com.hxypay.customview.KeyPop;
import com.hxypay.customview.Mem1Dialog;
import com.hxypay.customview.NewDialog;
import com.hxypay.dialogs.TiShiDialog;
import com.hxypay.response.AnnouncementRes;
import com.hxypay.response.BannerRes;
import com.hxypay.response.CreditRes;
import com.hxypay.response.DepositRes;
import com.hxypay.response.HomePlainRes;
import com.hxypay.response.MemberNewRes;
import com.hxypay.response.MerchantInfoRes;
import com.hxypay.update.UpdateDialog;
import com.hxypay.util.Constants;
import com.hxypay.util.CustomRoundAngleImageView;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;
import com.hxypay.util.SharedPreStorageMgr;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;



public class HomeActivity extends BaseActivity {

    private ScrollView sc;

    private MZBannerView mMZBanner, mMZBanner2;
    private List<BannerRes.Info1> bannerListUrl = new ArrayList<BannerRes.Info1>();

    private List<HomePlainRes.Info1> homePlainList = new ArrayList<HomePlainRes.Info1>();
    private HomePlainRes.Info1 card;

    private LinearLayout mRepaymentLayout, mRechargeLayout;

    private KeyPop keyPop;



    private int maxMoney = 200000;
    private int defMoney = 10000;
    private int defCount = 2;
    private int defDay = 1;
    private int currentPro = 0;

    private List<String> countList = new ArrayList<String>();
    private List<String> dayList = new ArrayList<String>();
    private OptionsPickerView pvOptions;



    private String memState = "0";//商户的状态  //认证状态  1 认证  0 未认证
    private String userCurrLevelId;//用户当前等级
    private String type = "1";
    private UpdateDialog updateDialog;
    private Mem1Dialog memDialog;
    private NewDialog newDialog;
    private TiShiDialog shimingDialog;
    private ImageView iv_banka,iv_cardCP,iv_cardCP1,iv_jisudaikuan,left_img;
    private LinearLayout ll_kf,ll_noviciate,ll_jymx,ll_IntegralBus,ll_zxcq;
    private List<AnnouncementRes.Info1> data_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        bannerImg();
    }



    private void initView() {
        initHeadView(this, 0, "首页", R.drawable.notice,null);
        mMZBanner = (MZBannerView) findViewById(R.id.banner);
        mRepaymentLayout = (LinearLayout) findViewById(R.id.repayment_layout);
        mRechargeLayout = (LinearLayout) findViewById(R.id.recharge_layout);
        iv_banka = findViewById(R.id.iv_banka);
        iv_cardCP = findViewById(R.id.iv_cardCP);
        iv_cardCP1 = findViewById(R.id.iv_cardCP1);
        iv_jisudaikuan = findViewById(R.id.iv_jisudaikuan);
        left_img = findViewById(R.id.left_kf);
        ll_kf = findViewById(R.id.ll_kf);
        ll_noviciate = findViewById(R.id.ll_noviciate);
        ll_jymx = findViewById(R.id.ll_jymx);
        ll_IntegralBus = findViewById(R.id.ll_IntegralBus);
        ll_zxcq = findViewById(R.id.ll_zxcq);

        moneySetting(defMoney);



        mRepaymentLayout.setOnClickListener(this);
        mRechargeLayout.setOnClickListener(this);
        iv_banka.setOnClickListener(this);
        iv_cardCP.setOnClickListener(this);
        iv_jisudaikuan.setOnClickListener(this);
        sc = (ScrollView) findViewById(R.id.sc);
        ll_kf.setOnClickListener(this);
        ll_noviciate.setOnClickListener(this);
        ll_jymx.setOnClickListener(this);
        ll_IntegralBus.setOnClickListener(this);
        ll_zxcq.setOnClickListener(this);


        getPopNew(); //信息；
    }




    private void moneySetting(int m) {
        int okMoney;
        if (m == defMoney) {
            okMoney = defMoney;
            currentPro = 0;
        } else if (m == maxMoney) {
            okMoney = maxMoney;
            currentPro = 180;
        } else {
            okMoney = m;
            float a1 = m - defMoney;
            float a2 = maxMoney - defMoney;
            float a3 = a2 / 180;
            float tempCurrPro = a1 / a3;
            Float f = new Float(tempCurrPro);
            currentPro = f.intValue();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        merchantInfo();
        getHomePlain();
    }


    private void merchantInfo() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.MERCHANT_INFO)
                .headers(PublicParam.getParam())
                .params(Params.merchantInfoParams())
                .build().execute(new GenericsCallback<MerchantInfoRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                memState = "2";
                dismissDialog();
                IToast.getIToast().showIToast();
            }

            @Override
            public void onResponse(MerchantInfoRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        memState = response.getData().getIsReal();
                        userCurrLevelId = response.getData().getType_id();
                        SharedPreStorageMgr.getIntance().saveStringValue(KeyConfig.USERCURRLEVELID,userCurrLevelId); //保存等级；
                        SharedPreStorageMgr.getIntance().saveStringValue(KeyConfig.USERCURRLEVELNAME,response.getData().getLevel()); //保存等级；
                        if (App.getApp().isMemDialogFlag()) {
                            getMemberPopNew();
                        }
                    } else {
                        memState = "2";
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    memState = "2";
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.repayment_layout:
                if (memState.equals("0")) {
//                    IToast.getIToast().showIToast("商户未认证，请实名认证");
                    isShiMing();
                } else if (memState.equals("1")) {
                    mIntent = new Intent(context, HKCardListActivity.class);
                    mIntent.putExtra("level",userCurrLevelId);
                    startActivity(mIntent);
                } else if (memState.equals("2")) {
                    IToast.getIToast().showIToast("商户未知状态，请重试");
                }
                break;
            case R.id.recharge_layout:
                if (memState.equals("0")) {
//                    IToast.getIToast().showIToast("商户未认证，请实名认证");
                    isShiMing();
                } else if (memState.equals("1")) {
                    depositCardData();
                } else if (memState.equals("2")) {
                    IToast.getIToast().showIToast("商户未知状态，请重试");
                }
                break;
            case R.id.iv_banka:
                banka();
                break;
            case R.id.iv_cardCP:
                cardCP();
                break;
            case R.id.iv_jisudaikuan:
                daikuan();
                break;
            case R.id.ll_kf:
                mIntent = new Intent(context, ServerMainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.ll_noviciate:
                //新手帮助；
                requestNoviciate();
                break;
            case R.id.ll_jymx:
                mIntent = new Intent(HomeActivity.this,BillActivity.class);
                startActivity(mIntent);
                break;
                //积分巴士；
            case R.id.ll_IntegralBus:
                mIntent = new Intent(HomeActivity.this,IntegralBusActivity.class);
                startActivity(mIntent);
                break;
            case R.id.ll_zxcq:  //卡包
//                IToast.getIToast().showIToast("征信查询未开通");
                mIntent = new Intent(this, CardBagActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    /**
     * 新手帮助；
     */
    private void requestNoviciate() {
        mIntent = new Intent(context, NewHandActivity.class);
        startActivity(mIntent);
    }

    //贷款；
    private void daikuan() {
        String url = mPs.getStringValue(Constants.DK_URL);
        if (TextUtils.isEmpty(url)) {
            IToast.getIToast().showIToast("暂无链接");
            return;
        }
        mIntent = new Intent(context, QQWBViewActivity.class);
        mIntent.putExtra("title", "贷款申请");
        mIntent.putExtra("url", url);
        startActivity(mIntent);
    }

    //卡测评；
    private void cardCP() {
        mIntent = new Intent(context,KaCePing_head_Activity.class);
        startActivity(mIntent);
    }

    //办信用卡
    private void banka() {
        String url = mPs.getStringValue(Constants.XYK_URL);
        if (TextUtils.isEmpty(url)) {
            IToast.getIToast().showIToast("暂无链接");
            return;
        }
        mIntent = new Intent(context, QQWBViewActivity.class);
        mIntent.putExtra("title", "信用卡申请");
        mIntent.putExtra("url", url);
        startActivity(mIntent);

    }


    private void getPopNew() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.ANNOUNCEMENT)
                .headers(PublicParam.getParam())
                .params(Params.popNew())
                .build().execute(new GenericsCallback<AnnouncementRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                dismissDialog();
            }

            @Override
            public void onResponse(AnnouncementRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData()!=null) {
                            data_info = response.getData();
                            String inter = SharedPreStorageMgr.getIntance().getStringValue(KeyConfig.NewInfo);
                            if (data_info.get(0).getCreated().equals(inter)){
                                show_right(false);
                            }else {
                                show_right(true);
                                String state = response.getData().get(0).getState(); // 1弹，2不弹
                                if (state.equals("1")){
                                    newDialog = new NewDialog(context,data_info,0 );
                                    newDialog.show();
                                }
                            }
                        }
                    } else {
                        GoLogin.goLogin(response.getCode());
                    }
                }
            }
        });
    }

    private void getMemberPopNew() {
        OkHttpUtils.post()
                .url(Constants.MEMBER_POP_NEW)
                .headers(PublicParam.getParam())
                .params(Params.memberPopNew())
                .build().execute(new GenericsCallback<MemberNewRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                dismissDialog();
            }

            @Override
            public void onResponse(MemberNewRes response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (!TextUtils.isEmpty(response.getData().getContent())) {
                            memDialog = new Mem1Dialog(context, response.getData().getContent(), memState, userCurrLevelId,"升级会员","返回");
                            memDialog.show();
                            App.getApp().setMemDialogFlag(false);

                        }
                    } else {
                        GoLogin.goLogin(response.getCode());
                    }
                }
            }
        });
    }

    @Override
    public void onRightListener() {
        super.onRightListener();
        mRightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data_info!=null) {
                    newDialog = new NewDialog(context, data_info,1);
                    newDialog.show();
                    show_right(false);
                    SharedPreStorageMgr.getIntance().saveStringValue(KeyConfig.NewInfo,data_info.get(0).getCreated());
                }
                else {
                    IToast.getIToast().showIToast("暂时没有消息");
                }
            }
        });
    }

    private void bannerImg() {
        OkHttpUtils.post()
                .url(Constants.BANNER)
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
                    } else {
                        GoLogin.goLogin(response.getCode());
                    }
                }
            }
        });
    }


    private void getHomePlain() {
        OkHttpUtils.post()
                .url(Constants.HOME_PLAIN)
                .headers(PublicParam.getParam())
                .params(Params.homePlain())
                .build().execute(new GenericsCallback<HomePlainRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(HomePlainRes response, int id) {
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData() != null && response.getData().size() > 0) {
                            homePlainList.clear();
                            homePlainList = response.getData();
                            //有数据
                            card = homePlainList.get(0);
                        } else {
                            //隐藏
                        }
                    } else {
                        //隐藏
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    //隐藏
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
        private CustomRoundAngleImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (CustomRoundAngleImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, BannerRes.Info1 data) {
            // 数据绑定
            Glide.with(context).load(data.getImg()).into(mImageView);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10010:
                //有注册权限且用户允许安装
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (updateDialog!=null) {
                        updateDialog.openFile();
                    }
                } else {
                    //将用户引导至安装未知应用界面。
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, 12345);
                }
                break;

        }
    }

    private void isShiMing() {
        if (shimingDialog == null) {
            shimingDialog = new TiShiDialog(HomeActivity.this, "温馨提示", "商户未认证，请实名认证\n请先进行实名", "前往实名", "取消",false);
        }
        shimingDialog.setClickListener(new TiShiDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                shimingDialog.cancel();
                mIntent = new Intent(context, RealNameOCRActivity.class);
                startActivity(mIntent);
            }

            @Override
            public void doCancel() {
                shimingDialog.cancel();
            }
        });
        shimingDialog.show();
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
                        try {
                            if (response.getData() != null && response.getData().size() > 0) {
                                CreditRes cr = new CreditRes();
                                CreditRes.Info1 tempCard = cr.new Info1();
                                if (card!=null) {
                                    tempCard.setId(card.getId());
                                    tempCard.setAccount(card.getAccount());
                                    tempCard.setAccountName(card.getAccountName());
                                    tempCard.setBankName(card.getBankName());
                                    tempCard.setBankLogo(card.getBankLogo());
                                    tempCard.setType(card.getType());
                                    tempCard.setRepaymentDay(card.getRepaymentDay());
                                    tempCard.setBillDay(card.getBillDay());
                                    tempCard.setBankBack(card.getBankBack());
                                }
                                Intent mIntent = new Intent(context, FastPayActivity.class);
                                mIntent.putExtra("creditCard", tempCard);
                                mIntent.putExtra("depositCard", response.getData().get(0));
                                startActivity(mIntent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
