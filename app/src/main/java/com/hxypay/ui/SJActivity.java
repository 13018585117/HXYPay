package com.hxypay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daoqidata.okhttputils.OkHttpUtils;
import com.daoqidata.okhttputils.callback.GenericsCallback;
import com.hxypay.BaseActivity;
import com.hxypay.R;
import com.hxypay.adapter.CommonAdapter;
import com.hxypay.adapter.MyViewHolder;
import com.hxypay.customview.IToast;
import com.hxypay.response.MerchantInfoRes;
import com.hxypay.response.SJ_ImgRes;
import com.hxypay.util.Constants;
import com.hxypay.util.GoLogin;
import com.hxypay.util.JsonGenericsSerializator;
import com.hxypay.util.Params;
import com.hxypay.util.PublicParam;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SJActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private String sjLevel, sjMoney;
    private String tele;
    private String merState = "0";
    private String base64FontStr = "data:image/jpg;base64,";
    private String headImgBase64;
    private String userLevelId;
    @BindView(R.id.lv_sj)
    ListView lv_sj;
    List<SJ_ImgRes.Info> datas =null;
    private CommonAdapter<SJ_ImgRes.Info> infoCommonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sj);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getImg();
        merchantInfo();
    }

    private void getImg() {
        showDialog("加载中...");
        OkHttpUtils.post()
                .url(Constants.SJ1)
                .headers(PublicParam.getParam())
                .params(Params.merchantInfoParams())
                .build().execute(new GenericsCallback<SJ_ImgRes>(new JsonGenericsSerializator()) {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                IToast.getIToast().showIToast();
                dismissDialog();
            }

            @Override
            public void onResponse(SJ_ImgRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        if (response.getData()!=null && response.getData().size()>0){
                            if (datas!=null) {
                                datas.clear();
                            }
                           datas = response.getData();
                            infoCommonAdapter.setUpdateData(datas);

                        }
                    }else {
                        IToast.getIToast().showIToast(response.getMsg());
                    }
                }
            }
        });
    }


    private void initView() {

        initHeadView(this, 0, "会员升级", 0,null);
        infoCommonAdapter = new CommonAdapter<SJ_ImgRes.Info>(this, datas, R.layout.item_sj) {
            @Override
            public void convert(MyViewHolder holder, SJ_ImgRes.Info info, int position) {
                holder.getImageUrl(R.id.iv_vip,info.getImg_src());
            }
        };
        lv_sj.setAdapter(infoCommonAdapter);
        lv_sj.setOnItemClickListener(this);
    }



    private void sj(String type,String Money,String name) {
        if (TextUtils.isEmpty(sjLevel) || TextUtils.isEmpty(sjMoney) || TextUtils.isEmpty(userLevelId)) {
            IToast.getIToast().showIToast("请登录重试！");
            return;
        }

        Intent mIntent = null;
        try {
            if (Integer.parseInt(userLevelId) >= 4) {
                IToast.getIToast().showIToast("您已为当前等级");
                return;
            }

            mIntent = new Intent(context, MemBerSJActivity.class);
            mIntent.putExtra("userLevelId", type);
            mIntent.putExtra("money", Money);
            mIntent.putExtra("level", name);
            context.startActivity(mIntent);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
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
                IToast.getIToast().showIToast();
                merState = "2";
                dismissDialog();
            }

            @Override
            public void onResponse(MerchantInfoRes response, int id) {
                dismissDialog();
                if (response != null) {
                    if (response.getCode().equals("0000")) {
                        merState = response.getData().getIsReal();
                        userLevelId = response.getData().getType_id();
                    } else {
                        merState = "2";
                        IToast.getIToast().showIToast(response.getMsg());
                        GoLogin.goLogin(response.getCode());
                    }
                } else {
                    merState = "2";
                    IToast.getIToast().showIToast("数据异常！！！");
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (datas != null) {
            String type = datas.get(position).getType();
            sjMoney = datas.get(position).getPay();
            sjLevel = datas.get(position).getName();
            sj(type,sjMoney,sjLevel);
        }
    }
}
